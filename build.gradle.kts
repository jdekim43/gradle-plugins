plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.gradle.plugin.publish)
}

group = "kim.jade"
version = "0.1.8"

gradlePlugin {
    website = "https://github.com/jdekim43/gradle-plugins"
    vcsUrl = "https://github.com/jdekim43/gradle-plugins"

    plugins.register("aws-codeartifact") {
        id = "kim.jade.gradle.plugin.aws-codeartifact"
        implementationClass = "kim.jade.gradle.plugin.aws.codeartifact.plugin.AwsCodeArtifactPlugin"

        displayName = "AWS CodeArtifact Gradle Plugin"
        description = "A Gradle plugin that allows you to access code artifacts easily."
        tags = listOf("aws", "codeartifact", "maven", "repository", "authentication")
    }

    plugins.register("cleanArchitecture") {
        id = "kim.jade.gradle.plugin.cleanarch"
        implementationClass = "kim.jade.gradle.plugin.cleanarch.plugin.CleanArchSettingsPlugin"

        displayName = "Clean Architecture Toolbox Gradle Plugin"
        description = "A Gradle plugin that support multi module clean architecture easily."
        tags = listOf("clean-architecture")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform(libs.aws.bom))
    implementation(libs.aws.codeartifact)
    implementation(libs.aws.sts)
    implementation(libs.aws.sso)
    implementation(libs.aws.sso.oidc)
}

kotlin {
    jvmToolchain(17)
}
