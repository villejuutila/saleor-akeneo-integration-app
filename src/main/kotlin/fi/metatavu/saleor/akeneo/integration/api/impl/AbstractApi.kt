package fi.metatavu.saleor.akeneo.integration.api.impl

import org.eclipse.microprofile.config.ConfigProvider
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Context
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response

/**
 * Abstract base class for all API services
 */
@RequestScoped
abstract class AbstractApi {

    @Inject
    @ConfigProperty(name = "saleor.akeneo.integrations.apiKey")
    lateinit var apiKey: String

    @Context
    lateinit var headers: HttpHeaders

    /**
     * Returns API key from request or null if not available
     */
    private val requestApiKey: String?
        get() {
            return headers.getHeaderString("X-API-KEY")
        }

    /**
     * Checks whether given API key is valid
     *
     * @return Boolean whether given API key is valid
     */
    protected fun isApiKeyValid(): Boolean {
        val givenApiKey = requestApiKey ?: return false

        return givenApiKey == apiKey
    }

    /**
     * Checks whether app is registered with Saleor
     *
     * @return Boolean whether App is registered
     */
    protected fun isAppRegistered(): Boolean {
        return ConfigProvider.getConfig().getValue("saleor.app.registered", Boolean::class.java)
    }

    /**
     * Constructs OK response
     *
     * @return Response
     */
    protected fun createOk(): Response {
        return Response
            .status(Response.Status.OK)
            .build()
    }

    /**
     * Constructs OK response
     *
     * @param message response message
     * @return Response
     */
    protected fun createOk(message: String): Response {
        return constructSuccess(
            status = Response.Status.OK,
            message = message
        )
    }

    /**
     * Constructs OK response
     * with X-TOTAL-COUNT header
     *
     * @param message response message
     * @param count count
     * @return Response
     */
    protected fun createOk(message: String, count: Int): Response {
        return constructSuccess(
            status = Response.Status.OK,
            message = message,
            count =  count
        )
    }

    /**
     * Constructs bad request response
     *
     * @param message message
     * @return Response
     */
    protected fun createBadRequest(message: String): Response {
        return constructError(
            status = Response.Status.BAD_REQUEST,
            message = message
        )
    }

    /**
     * Constructs internal server error response
     *
     * @param message message
     * @return Response
     */
    protected fun createInternalServerError(message: String): Response {
        return constructError(
            status = Response.Status.INTERNAL_SERVER_ERROR,
            message = message
        )
    }

    /**
     * Constructs unauthorized response
     *
     * @param message message
     * @return Response
     */
    protected fun createUnauthorized(message: String): Response {
        return constructError(
            status = Response.Status.UNAUTHORIZED,
            message = message
        )
    }

    /**
     * Constructs a success response
     *
     * @param status status code
     * @param message success message
     * @return Success response
     */
    private fun constructSuccess(status: Response.Status, message: String): Response {
        val entity = fi.metatavu.saleor.akeneo.integration.model.Success(
            message = message,
            code = status.statusCode
        )

        return Response
            .status(status)
            .entity(entity)
            .build()
    }

    /**
     * Constructs a success response
     * with X-TOTAL-COUNT header
     *
     * @param status status code
     * @param message success message
     * @param count count
     * @return Success Response
     */
    private fun constructSuccess(status: Response.Status, message: String, count: Int): Response {
        val entity = fi.metatavu.saleor.akeneo.integration.model.Success(
            message = message,
            code = status.statusCode
        )

        return Response
            .status(status)
            .entity(entity)
            .header(X_TOTAL_COUNT_HEADER, count.toString())
            .build()
    }

    /**
     * Constructs an error response
     *
     * @param status status code
     * @param message error message
     * @return Error response
     */
    private fun constructError(status: Response.Status, message: String): Response {
        val entity = fi.metatavu.saleor.akeneo.integration.model.Error(
            message = message,
            code = status.statusCode
        )

        return Response
            .status(status)
            .entity(entity)
            .build()
    }

    companion object {
        const val SUCCESS_MESSAGE = "operation completed successfully"
        const val IMPORT_OPERATION = "Import"
        const val X_TOTAL_COUNT_HEADER = "X-TOTAL-COUNT"
    }
}