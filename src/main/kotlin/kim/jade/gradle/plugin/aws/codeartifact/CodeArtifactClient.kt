package kim.jade.gradle.plugin.aws.codeartifact

import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.codeartifact.CodeartifactClient
import java.util.concurrent.ConcurrentHashMap

class CodeArtifactClient {

    private data class CacheKey(val region: Region, val credentialProvider: AwsCredentialsProvider?)

    private val cache = ConcurrentHashMap<CacheKey, CodeartifactClient>()

    fun getAuthorizationToken(url: CodeArtifactUrl, credentialProvider: AwsCredentialsProvider?): String {
        val client = getClient(url.region, credentialProvider)

        return client.getAuthorizationToken {
            it.domain(url.domain)
            it.domainOwner(url.ownerAccountId)
        }.authorizationToken()
    }

    private fun getClient(region: Region, credentialProvider: AwsCredentialsProvider?): CodeartifactClient =
        cache.getOrPut(CacheKey(region, credentialProvider)) {
            CodeartifactClient.builder()
                .region(region)
                .let {
                    if (credentialProvider == null) {
                        it
                    } else {
                        it.credentialsProvider(credentialProvider)
                    }
                }
                .build()
        }
}