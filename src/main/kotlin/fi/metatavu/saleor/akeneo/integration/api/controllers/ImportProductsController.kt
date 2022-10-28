package fi.metatavu.saleor.akeneo.integration.api.controllers

import fi.metatavu.saleor.akeneo.integration.api.akeneo.AkeneoController
import fi.metatavu.saleor.akeneo.integration.api.apollo.ApolloController
import fi.metatavu.saleor.akeneo.integration.api.apollo.translate.ProductToProductCreateInputTranslator
import org.slf4j.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Controller for synchronizing products
 */
@ApplicationScoped
class ImportProductsController {

    @Inject
    lateinit var logger: Logger

    @Inject
    lateinit var apolloController: ApolloController

    @Inject
    lateinit var akeneoController: AkeneoController

    @Inject
    lateinit var productToProductCreateInputTranslator: ProductToProductCreateInputTranslator

    /**
     * Synchronizes products from Akeneo PIM into Saleor
     * Returns amount of successfully synchronized products
     *
     * @returns Amount of successfully synchronized products
     */
    suspend fun synchronizeProducts(): Int {
        logger.info("Proceeding to retrieve products from Akeneo PIM...")

        val akeneoProducts = akeneoController.getProducts()
            .filterIndexed { index, _ -> index < 10  }
            .distinctBy { it.values.name?.first()?.data }

        logger.info("Proceeding to create products in Saleor...")

        val synchronizedProducts = akeneoProducts.map {
            apolloController.executeProductCreateMutation(
                productToProductCreateInputTranslator.translate(it)
            )

        }

        return synchronizedProducts.filter { it }.size
    }
}