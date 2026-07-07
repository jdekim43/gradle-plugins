package kim.jade.gradle.plugin.cleanarch.plugin

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import java.io.File

class CleanArchSettingsPlugin : Plugin<Settings> {
    override fun apply(target: Settings) {
    }
}

fun Settings.module(name: String, path: String) {
    val projectPath = File(settingsDir, path)

    if (!projectPath.exists()) {
        return
    }

    val moduleName = "${rootProject.name}-$name"
    include(":$moduleName")
    project(":$moduleName").projectDir = projectPath
}

fun Settings.domain(name: String, extraModules: List<String> = emptyList()) {
    module("$name-entity", "domain/$name/entity")
    module("$name-usecase", "domain/$name/usecase")

    for (moduleName in extraModules) {
        module("$name-$moduleName", "domain/$name/$moduleName")
    }
}
