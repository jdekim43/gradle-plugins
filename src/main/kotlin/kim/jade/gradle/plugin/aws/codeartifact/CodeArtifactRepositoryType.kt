package kim.jade.gradle.plugin.aws.codeartifact

enum class CodeArtifactRepositoryType {
    MAVEN;

    val pathName: String = name.lowercase()

    companion object {
        fun from(pathName: String): CodeArtifactRepositoryType =
            entries.firstOrNull { it.pathName.equals(pathName, true) }
                ?: throw IllegalArgumentException("Repository type $pathName is not supported")
    }
}