package kim.jade.gradle.plugin.aws.codeartifact

import software.amazon.awssdk.regions.Region
import java.net.URI
import java.net.URL

@Suppress("unused")
data class CodeArtifactUrl(
    val url: URL,
    val domain: String,
    val ownerAccountId: String,
    val region: Region,
    val type: CodeArtifactRepositoryType,
    val repositoryName: String,
) {

    init {
        require(type == CodeArtifactRepositoryType.MAVEN)
    }

    constructor(
        domain: String,
        ownerAccountId: String,
        region: Region,
        type: CodeArtifactRepositoryType,
        repository: String,
    ) : this(
        URI("https://$domain-$ownerAccountId.d.codeartifact.$region.amazonaws.com/${type.pathName}/$repository").toURL(),
        domain,
        ownerAccountId,
        region,
        type,
        repository,
    )

    constructor(url: String) : this(URI(url).toURL())

    constructor(url: URL) : this(
        url,
        url.host.split('.'),
        url.path.removePrefix("/").removeSuffix("/").split('/'),
    )

    private constructor(
        url: URL,
        hostParts: List<String>,
        pathParts: List<String>,
    ) : this(
        url = url,
        domain = hostParts[0].substring(0, hostParts[0].lastIndexOf('-')),
        ownerAccountId = hostParts[0].substring(hostParts[0].lastIndexOf('-') + 1),
        region = Region.of(hostParts[hostParts.size - 3]),
        type = CodeArtifactRepositoryType.from(pathParts[0]),
        repositoryName = pathParts.slice(1 until pathParts.size).joinToString("/"),
    )
}