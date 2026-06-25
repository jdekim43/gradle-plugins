package kim.jade.gradle.plugin.aws.codeartifact.plugin

import groovy.lang.Closure
import kim.jade.gradle.plugin.aws.codeartifact.CodeArtifactService
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.logging.Logger
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import java.net.URI

internal class RepositoryHandlerConfigurer(
    private val serviceProvider: Provider<CodeArtifactService>,
    private val logger: Logger,
) {

    companion object {
        private const val EXTENSION_NAME = "codeArtifact"
        private val CODEARTIFACT_URL_REGEX = "(?i).+\\.codeartifact\\..+\\.amazonaws\\..+".toRegex()
    }

    fun installTo(repositoryHandler: RepositoryHandler) {
        installCodeArtifactExtension(repositoryHandler)
        configRepositories(repositoryHandler)
    }

    private fun installCodeArtifactExtension(repositoryHandler: RepositoryHandler) {
        val ext = (repositoryHandler as ExtensionAware).extensions.extraProperties
        ext[CodeArtifactService.EXTENSION_NAME] = serviceProvider

        if (ext.has(EXTENSION_NAME)) {
            return
        }

        @Suppress("unused")
        val closure = object : Closure<Any>(repositoryHandler, repositoryHandler) {
            fun doCall(repositoryUrl: String, profile: String = "default", closure: Closure<*>? = null) {
                logger.info("Getting token for $repositoryUrl in profile $profile")

                val token = serviceProvider.get().getToken(repositoryUrl, ProfileCredentialsProvider.create(profile))
                val handler = delegate as RepositoryHandler

                handler.maven { mavenRepo ->
                    mavenRepo.url = URI(repositoryUrl)
                    mavenRepo.credentials { creds ->
                        creds.username = "aws"
                        creds.password = token
                    }

                    closure?.let {
                        it.delegate = mavenRepo
                        it.resolveStrategy = DELEGATE_FIRST
                        it.call()
                    }
                }
            }

            fun doCall(repoUrl: String) {
                doCall(repoUrl, "default", null)
            }

            fun doCall(repoUrl: String, profile: String) {
                doCall(repoUrl, profile, null)
            }
        }
        ext[EXTENSION_NAME] = closure
    }

    private fun configRepositories(repositories: RepositoryHandler) {
        repositories.withType(MavenArtifactRepository::class.java).configureEach { artifactRepository ->
            val repositoryUri = artifactRepository.url

            if (isCodeArtifactUri(repositoryUri) && areCredentialsEmpty(artifactRepository)) {
                val profile = resolveProfile()
                logger.info("Getting token for {} in profile {}", repositoryUri, profile)

                val token =
                    serviceProvider.get().getToken(repositoryUri.toURL(), ProfileCredentialsProvider.create(profile))

                artifactRepository.credentials { creds ->
                    creds.username = "aws"
                    creds.password = token
                }
                artifactRepository.url = repositoryUri
            }
        }
    }

    private fun resolveProfile(): String? {
        return System.getProperty("codeArtifact.profile") ?: System.getenv("CODEARTIFACT_PROFILE")
    }

    private fun areCredentialsEmpty(mavenRepo: MavenArtifactRepository): Boolean {
        return mavenRepo.credentials.password == null && mavenRepo.credentials.username == null
    }

    private fun isCodeArtifactUri(uri: URI): Boolean = uri.toString().matches(CODEARTIFACT_URL_REGEX)
}