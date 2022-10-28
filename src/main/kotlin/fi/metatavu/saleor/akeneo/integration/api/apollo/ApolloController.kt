package fi.metatavu.saleor.akeneo.integration.api.apollo

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import fi.metatavu.saleor.akeneo.integration.graphql.type.ProductCreateInput
import fi.metatavu.saleor.akeneo.integration.graphql.ProductcreateMutation
import okhttp3.OkHttpClient
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.slf4j.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller class for Apollo GraphQL client
 */
@ApplicationScoped
class ApolloController {

    @Inject
    lateinit var logger: Logger

    @Inject
    @ConfigProperty(name = "saleor.graphql.endpoint.url")
    lateinit var graphQlEndpointUrl: String

    private val okHttpClient: OkHttpClient
        get() {
            return OkHttpClient.Builder()
                .addInterceptor(ApolloAuthorizationInterceptor())
                .build()
        }

    private val apolloClient: ApolloClient
        get() {
            return ApolloClient.Builder()
                .serverUrl(graphQlEndpointUrl)
                .okHttpClient(okHttpClient)
                .build()
        }

    /**
     * Executes ProductCreate mutation to Saleor API
     * Returns boolean whether mutation resulted in creation of new product
     *
     * @param productCreateInput productCreateInput
     * @return Boolean
     */
    suspend fun executeProductCreateMutation(productCreateInput: ProductCreateInput): Boolean {
        val createdProduct = apolloClient.mutation(
            ProductcreateMutation(
                input = productCreateInput
            )
        ).execute()

        if (createdProduct.hasErrors() || createdProduct.data?.productCreate?.product?.slug == null) {
            logger.error("Failed to create product ${productCreateInput.slug.getOrNull()}:")
            createdProduct.errors?.forEach {
                logger.error(it.message)
            }

            return false
        }

        logger.info("Successfully created product ${createdProduct.data?.productCreate?.product?.slug}")

        return true
        }
}