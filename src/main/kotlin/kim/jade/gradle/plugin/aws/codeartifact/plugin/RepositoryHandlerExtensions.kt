package kim.jade.gradle.plugin.aws.codeartifact.plugin

import kim.jade.gradle.plugin.aws.codeartifact.CodeArtifactService
import kim.jade.gradle.plugin.aws.codeartifact.plugin.AwsCodeArtifactPlugin.Companion.USE_CODE_ARTIFACT_CREDENTIALS_TAG
import org.gradle.api.Action
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.AuthenticationSupported
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.provider.Provider
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import java.net.URI

@Suppress("unused")
fun RepositoryHandler.codeArtifact(
    repositoryUrl: String,
    profile: String,
    action: Action<in MavenArtifactRepository>? = null,
) = codeArtifact(repositoryUrl, ProfileCredentialsProvider.create(profile), action)

fun RepositoryHandler.codeArtifact(
    repositoryUrl: String,
    credentialsProvider: AwsCredentialsProvider = DefaultCredentialsProvider.builder().build(),
    action: Action<in MavenArtifactRepository>? = null,
) {
    val logger = Logging.getLogger(RepositoryHandler::class.java)
    logger.info(
        "Configuring CodeArtifact repository: url={}, credentialsProvider={}",
        repositoryUrl,
        credentialsProvider
    )

    val ext = (this as ExtensionAware).extensions.extraProperties

    @Suppress("UNCHECKED_CAST")
    val serviceProvider = ext[CodeArtifactService.EXTENSION_NAME] as Provider<CodeArtifactService>
    logger.info("Getting token for $repositoryUrl in credentialsProvider $credentialsProvider")
    val token = serviceProvider.get().getToken(repositoryUrl, credentialsProvider)
    maven { mavenRepo ->
        mavenRepo.url = URI(repositoryUrl)
        mavenRepo.credentials { creds ->
            creds.username = "aws"
            creds.password = token
        }
        action?.execute(mavenRepo)
    }
}

fun AuthenticationSupported.useCodeArtifactCredentials(profileName: String? = null) {
    credentials.username = USE_CODE_ARTIFACT_CREDENTIALS_TAG
    credentials.password = profileName
}