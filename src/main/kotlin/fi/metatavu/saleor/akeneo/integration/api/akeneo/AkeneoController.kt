package fi.metatavu.saleor.akeneo.integration.api.akeneo

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fi.metatavu.saleor.akeneo.integration.api.akeneo.model.AkeneoAuthResponse
import fi.metatavu.saleor.akeneo.integration.api.akeneo.model.AkeneoProduct
import fi.metatavu.saleor.akeneo.integration.api.akeneo.model.AkeneoProductsResponse
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.Logger
import java.util.Base64
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import org.apache.http.NameValuePair
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair
import java.io.IOException

/**
 * Controller for Akeneo PIM REST API Client
 */
@ApplicationScoped
class AkeneoController {

    @Inject
    lateinit var logger: Logger

    @Inject
    @ConfigProperty(name = "akeneo.pim.base.url")
    lateinit var apiBasePath: String

    @Inject
    @ConfigProperty(name = "akeneo.pim.api.client.id")
    lateinit var apiClientId: String

    @Inject
    @ConfigProperty(name = "akeneo.pim.api.client.secret")
    lateinit var apiClientSecret: String

    @Inject
    @ConfigProperty(name = "akeneo.pim.api.username")
    lateinit var apiUsername: String

    @Inject
    @ConfigProperty(name = "akeneo.pim.api.password")
    lateinit var apiPassword: String

    private val objectMapper: ObjectMapper
        get() {
            return jacksonObjectMapper()
                .findAndRegisterModules()
        }

    private val encodedClientIdAndSecret: String
        get() {
            return Base64.getEncoder().encodeToString("$apiClientId:$apiClientSecret".toByteArray())
        }

    private val httpClient: CloseableHttpClient
        get() {
            return HttpClients.createDefault()
        }

    /**
     * Gets list of products from Akeneo PIM API
     *
     * @return List of AkeneoProducts
     */
    fun getProducts(): List<AkeneoProduct> {
        val products = mutableListOf<AkeneoProduct>()
        val auth = getAccessToken() ?: throw Exception("Failed to obtain Akeneo PIM access token")
        var retrievedAllProducts = false
        var nextPageLink = "$apiBasePath$LIST_PRODUCTS_ENDPOINT?limit=100"

        while (!retrievedAllProducts) {
            logger.info("Retrieving $nextPageLink...")
            val response: AkeneoProductsResponse = doRequest(
                requestUrl = nextPageLink,
                auth = auth
            ) ?: break
            if (response.links.next == null) {
                retrievedAllProducts = true
            } else {
                nextPageLink = response.links.next.href
            }
            products.addAll(response.embedded.items)
        }

        return products.filter { it.enabled ?: false }
    }

    /**
     * Does HTTP GET-request to Akeneo PIM API
     *
     * @param requestUrl requestUrl
     * @param auth auth
     * @return InputStream
     */
    private fun doRequest(requestUrl: String, auth: AkeneoAuthResponse): AkeneoProductsResponse? {
        val accessToken = auth.accessToken

        try {
            httpClient.use { client ->
                val httpGet = HttpGet(requestUrl)
                httpGet.addHeader("Authorization", "Bearer $accessToken")
                client.execute(httpGet).use { response ->
                    if (response.statusLine.statusCode != 200) {
                        logger.error("Failed to do request to $requestUrl: {}", response.entity.content)
                        return null
                    }

                    return objectMapper.readValue(response.entity.content)
                }
            }
        } catch (error: IOException) {
            logger.debug("Failed to do request", error)
        }

        return null
    }

    /**
     * Requests access token from Akeneo PIM API
     *
     * @return AkeneoAuthResponse
     */
    private fun getAccessToken(): AkeneoAuthResponse? {
        logger.info("Obtaining new access token for Akeneo PIM...")

        val authUri = "$apiBasePath$TOKEN_ENDPOINT"

        try {
            httpClient.use { client ->
                val httpPost = HttpPost(authUri)
                val params: MutableList<NameValuePair> = ArrayList()
                params.add(BasicNameValuePair("grant_type", "password"))
                params.add(BasicNameValuePair("username", apiUsername))
                params.add(BasicNameValuePair("password", apiPassword))
                httpPost.entity = UrlEncodedFormEntity(params)
                httpPost.addHeader("Authorization", "Basic $encodedClientIdAndSecret")
                client.execute(httpPost).use { response ->
                    if (response.statusLine.statusCode != 200) {
                        logger.error("Failed to obtain new access token: {}", response.entity.content)
                        return null
                    }

                    response.entity.content.use { inputStream ->
                        return objectMapper.readValue(inputStream)
                    }
                }

            }

        } catch (error: IOException) {
            logger.debug("Failed to retrieve access token", error)
        }

        return null
    }

    companion object {
        const val TOKEN_ENDPOINT = "oauth/v1/token"
        const val LIST_PRODUCTS_ENDPOINT = "rest/v1/products"
    }
}