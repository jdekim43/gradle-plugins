package kim.jade.gradle.plugin.aws.codeartifact.plugin

import kim.jade.gradle.plugin.aws.codeartifact.CodeArtifactService
import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import org.gradle.api.logging.Logging

class SettingsPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {
        val configurer = RepositoryHandlerConfigurer(
            CodeArtifactService.load(target.gradle),
            Logging.getLogger(Settings::class.java),
        )

        configurer.installTo(target.pluginManagement.repositories)
        @Suppress("UnstableApiUsage")
        configurer.installTo(target.dependencyResolutionManagement.repositories)
    }
}