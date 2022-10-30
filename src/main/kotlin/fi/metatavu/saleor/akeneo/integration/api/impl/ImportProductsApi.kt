package fi.metatavu.saleor.akeneo.integration.api.impl

import fi.metatavu.saleor.akeneo.integration.api.controllers.ImportProductsController
import javax.enterprise.context.RequestScoped
import org.slf4j.Logger
import javax.inject.Inject
import javax.ws.rs.core.Response
import fi.metatavu.saleor.akeneo.integration.spec.ImportProductsApi

/**
 * API implementation for SynchronizeProductsApi
 */
@RequestScoped
class ImportProductsApi: ImportProductsApi, AbstractApi() {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var importProductsController: ImportProductsController

    override suspend fun importProducts(): Response {
//        if (!isApiKeyValid()) {
//            return createUnauthorized("Invalid API-key")
//        }

        logger.info("Initiating product synchronization...")

        if (!isAppRegistered()) {
            logger.error("Aborting! App is not registered with Saleor")
            return createBadRequest("App is not registered with Saleor")
        }

        val productImportResult = importProductsController.synchronizeProducts()


        return createOk(
            message = "$IMPORT_OPERATION $SUCCESS_MESSAGE",
            count = productImportResult
        )
    }
}