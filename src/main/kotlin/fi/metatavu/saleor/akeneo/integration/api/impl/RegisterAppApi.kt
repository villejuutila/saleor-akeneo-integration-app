package fi.metatavu.saleor.akeneo.integration.api.impl

import fi.metatavu.saleor.akeneo.integration.model.AuthToken
import fi.metatavu.saleor.akeneo.integration.spec.RegisterAppApi
import org.slf4j.Logger
import javax.enterprise.context.RequestScoped
import javax.inject.Inject
import javax.ws.rs.core.Response

/**
 * API implementation for RegisterAppApi
 */
@RequestScoped
class RegisterAppApi: RegisterAppApi, AbstractApi() {

    @Inject
    lateinit var logger: Logger

    override suspend fun registerAppWithSaleor(authToken: AuthToken?): Response {
        logger.info("Initiating registration with Saleor...")

        if (isAppRegistered()) {
            return createBadRequest("App is already registered")
        }

        logger.info("Received Saleor App Auth Token: ${authToken?.authToken}")

        return createOk()
    }
}