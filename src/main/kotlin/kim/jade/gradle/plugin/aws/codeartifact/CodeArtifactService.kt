package kim.jade.gradle.plugin.aws.codeartifact

import org.gradle.api.invocation.Gradle
import org.gradle.api.provider.Provider
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

class CodeArtifactService : BuildService<CodeArtifactService.Parameter> {

    companion object {
        const val EXTENSION_NAME = "codeArtifactService"

        fun load(gradle: Gradle): Provider<CodeArtifactService> = gradle.sharedServices.registerIfAbsent(
            "aws/codeArtifact/service",
            CodeArtifactService::class.java,
        )
    }

    open class Parameter : BuildServiceParameters

    private data class CacheKey(
        val url: CodeArtifactUrl,
        val credentialProvider: AwsCredentialsProvider?,
    )

    private val cache = ConcurrentHashMap<CacheKey, String>()
    private val client = CodeArtifactClient()

    override fun getParameters(): Parameter = Parameter()

    fun getToken(url: String, credentialProvider: AwsCredentialsProvider?): String =
        getToken(CodeArtifactUrl(url), credentialProvider)

    fun getToken(url: URL, credentialProvider: AwsCredentialsProvider?): String =
        getToken(CodeArtifactUrl(url), credentialProvider)

    fun getToken(url: CodeArtifactUrl, credentialProvider: AwsCredentialsProvider?): String =
        cache.getOrPut(CacheKey(url, credentialProvider)) {
            client.getAuthorizationToken(url, credentialProvider)
        }
}