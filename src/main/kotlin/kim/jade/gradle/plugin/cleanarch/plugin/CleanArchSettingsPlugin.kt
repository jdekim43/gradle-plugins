package kim.jade.gradle.plugin.cleanarch.plugin

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import java.io.File

@Suppress("unused")
class CleanArchSettingsPlugin : Plugin<Settings> {
    override fun apply(target: Settings) {
    }
}

private fun nameToPath(name: String) = name.replace('-', '/')

fun Settings.module(name: String, path: String = nameToPath(name)) {
    val projectPath = File(settingsDir, path)

    if (!projectPath.exists()) {
        return
    }

    if (!File(projectPath, "build.gradle.kts").exists() && !File(projectPath, "build.gradle").exists()) {
        return
    }

    val moduleName = "${rootProject.name}-$name"
    include(":$moduleName")
    project(":$moduleName").projectDir = projectPath
}

@Suppress("unused")
fun Settings.core(name: String, path: String = "core/$name", prefix: String = "", extra: List<String> = emptyList()) {
    module("$prefix$name-domain", "$path/domain")
    module("$prefix$name-entity", "$path/entity")
    module("$prefix$name-value", "$path/value")
    module("$prefix$name-event", "$path/event")
    module("$prefix$name-state", "$path/state")
    module("$prefix$name-policy", "$path/policy")

    module("$prefix$name-application", "$path/application")
    module("$prefix$name-usecase", "$path/usecase")
    module("$prefix$name-port", "$path/port")
    module("$prefix$name-repository", "$path/repository")

    for (moduleName in extra) {
        module("$name-$moduleName", "$path/$moduleName")
    }
}
