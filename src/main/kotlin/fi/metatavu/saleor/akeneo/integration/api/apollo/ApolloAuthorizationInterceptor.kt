package fi.metatavu.saleor.akeneo.integration.api.apollo

import okhttp3.Interceptor
import okhttp3.Response
import org.eclipse.microprofile.config.ConfigProvider


class ApolloAuthorizationInterceptor: Interceptor {

    private val authToken: String
        get() {
            return ConfigProvider.getConfig().getValue("saleor.app.auth.token", String::class.java)
        }

    override fun intercept(chain: Interceptor.Chain): Response {
//        val authToken = "YSwaTxRD2jW6Sindl4SzHOvsHtoP2e"
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $authToken")
            .build()

        return chain.proceed(request)
    }
}