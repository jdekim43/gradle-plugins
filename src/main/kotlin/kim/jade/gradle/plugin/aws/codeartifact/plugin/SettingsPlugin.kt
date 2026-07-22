package kim.jade.gradle.plugin.aws.codeartifact.plugin

import kim.jade.gradle.plugin.aws.codeartifact.CodeArtifactService
import kim.jade.gradle.plugin.aws.codeartifact.plugin.AwsCodeArtifactPlugin.Companion.DEFAULT_PROFILE_ENV_VAR
import kim.jade.gradle.plugin.aws.codeartifact.plugin.AwsCodeArtifactPlugin.Companion.DEFAULT_PROFILE_PROPERTY
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.logging.Logging

class SettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        val configurer = RepositoryHandlerConfigurer(
            CodeArtifactService.load(target.gradle),
            Logging.getLogger(Settings::class.java),
        )
        val defaultProfile: String? =
            target.providers.gradleProperty(DEFAULT_PROFILE_PROPERTY).orNull ?: System.getenv(DEFAULT_PROFILE_ENV_VAR)

        configurer.installTo(target.pluginManagement.repositories, defaultProfile)
        @Suppress("UnstableApiUsage")
        configurer.installTo(target.dependencyResolutionManagement.repositories, defaultProfile)
    }
}