package fi.metatavu.saleor.akeneo.integration.api.apollo.translate

import com.apollographql.apollo3.api.Optional
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fi.metatavu.saleor.akeneo.integration.api.akeneo.model.AkeneoProduct
import fi.metatavu.saleor.akeneo.integration.api.apollo.ProductDescription
import fi.metatavu.saleor.akeneo.integration.api.apollo.ProductDescriptionBlock
import fi.metatavu.saleor.akeneo.integration.api.apollo.ProductDescriptionData
import fi.metatavu.saleor.akeneo.integration.graphql.type.AttributeValueInput
import fi.metatavu.saleor.akeneo.integration.graphql.type.ProductCreateInput
import java.text.Normalizer
import java.time.OffsetDateTime
import java.util.*
import javax.enterprise.context.ApplicationScoped

/**
 * Translates AkeneoProduct to ProductCreateInput object
 */
@ApplicationScoped
class ProductToProductCreateInputTranslator {

    fun translate(entity: AkeneoProduct): ProductCreateInput {
        val productName = entity.values.name?.firstOrNull()?.data ?: UNNAMED_PRODUCT
        val productSlug = createSlugFromName(productName)
        val productDescription = createProductDescription(entity.values.description?.firstOrNull()?.data ?: EMPTY_DESCRIPTION)
        return ProductCreateInput(
            attributes = Optional.Present(listOf(
                AttributeValueInput(
                    id = Optional.present(AKENEO_PRODUCT_ID_ATTRIBUTE_ID),
                    plainText = Optional.Present(entity.identifier)
                )
            )),
            description = Optional.present(productDescription),
            category = Optional.present(DEFAULT_CATEGORY_ID),
            productType = SIMPLE_PRODUCT_TYPE_ID,
            name = Optional.Present(productName),
            slug = Optional.present(productSlug),
            collections = Optional.present(listOf(FEATURE_PRODUCTS_COLLECTION_ID))
        )
    }

    /**
     * Creates slug from Product's name
     *
     * @param productName productName
     * @return slug
     */
    private fun createSlugFromName(productName: String): String {
        return Normalizer
            .normalize(productName, Normalizer.Form.NFD)
            .replace("[^\\w\\s-]".toRegex(), "")
            .replace("-", "")
            .trim()
            .replace("\\s+".toRegex(), "-")
            .lowercase(Locale.getDefault())
    }

    /**
     * Creates Product's description in Saleor complaing rich text format
     *
     * @param productDescription productDescription
     * @return JSON String
     */
    private fun createProductDescription(productDescription: String): String {
        val description = ProductDescription(
            time = OffsetDateTime.now().toInstant().toEpochMilli(),
            blocks = listOf(
                ProductDescriptionBlock(
                    id = UUID.randomUUID().toString().replace("-", "").substring(0, 9),
                    type = "paragraph",
                    data = ProductDescriptionData(
                        text = productDescription
                    )
                )
            )
        )

        return jacksonObjectMapper().writeValueAsString(description)
    }

    companion object {
        const val SIMPLE_PRODUCT_TYPE_ID = "UHJvZHVjdFR5cGU6MjI="
        const val AKENEO_PRODUCT_ID_ATTRIBUTE_ID = "QXR0cmlidXRlOjQ0"
        const val DEFAULT_CATEGORY_ID = "Q2F0ZWdvcnk6MQ=="
        const val FEATURE_PRODUCTS_COLLECTION_ID = "Q29sbGVjdGlvbjo0"
        const val EMPTY_DESCRIPTION = "Empty description"
        const val UNNAMED_PRODUCT = "Unnamed product"
    }
}