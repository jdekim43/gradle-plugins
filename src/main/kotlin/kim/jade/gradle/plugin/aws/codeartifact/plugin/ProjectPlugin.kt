package kim.jade.gradle.plugin.aws.codeartifact.plugin

import kim.jade.gradle.plugin.aws.codeartifact.CodeArtifactService
import kim.jade.gradle.plugin.aws.codeartifact.plugin.AwsCodeArtifactPlugin.Companion.DEFAULT_PROFILE_ENV_VAR
import kim.jade.gradle.plugin.aws.codeartifact.plugin.AwsCodeArtifactPlugin.Companion.DEFAULT_PROFILE_PROPERTY
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension

class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val configurer = RepositoryHandlerConfigurer(CodeArtifactService.load(target.gradle), target.logger)
        val defaultProfile: String? = target.findProperty(DEFAULT_PROFILE_PROPERTY)?.toString() ?: System.getenv(DEFAULT_PROFILE_ENV_VAR)

        configurer.installTo(target.repositories, defaultProfile)

        target.plugins.withId("maven-publish") {
            target.extensions.findByType(PublishingExtension::class.java)?.repositories?.let {
                configurer.installTo(it, defaultProfile)
            }
        }
    }
}