package kim.jade.gradle.plugin.aws.codeartifact.plugin

import kim.jade.gradle.plugin.aws.codeartifact.CodeArtifactService
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension

class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val configurer = RepositoryHandlerConfigurer(CodeArtifactService.load(target.gradle), target.logger)

        configurer.installTo(target.repositories)

        target.plugins.withId("maven-publish") {
            target.extensions.findByType(PublishingExtension::class.java)?.repositories?.let {
                configurer.installTo(it)
            }
        }
    }
}