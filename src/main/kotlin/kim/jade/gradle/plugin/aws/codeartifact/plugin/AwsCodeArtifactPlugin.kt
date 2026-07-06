package kim.jade.gradle.plugin.aws.codeartifact.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings

@Suppress("unused")
class AwsCodeArtifactPlugin : Plugin<Any> {

    companion object {
        const val USE_CODE_ARTIFACT_CREDENTIALS_TAG = "##code_artifact##"
    }

    private val projectPlugin = ProjectPlugin()
    private val settingsPlugin = SettingsPlugin()

    override fun apply(target: Any) {
        when (target) {
            is Project -> projectPlugin.apply(target)
            is Settings -> settingsPlugin.apply(target)
            else -> throw IllegalArgumentException("This plugin support only Project or Settings")
        }
    }
}