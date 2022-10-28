package fi.metatavu.saleor.akeneo.integration.api.akeneo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * POJO for Akeneo PIM Products
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AkeneoProduct (

    /**
     * Product identifier, i.e. the value of the only 'pim_catalog_identifier' attribute
     */
    val identifier: String?,

    /**
     * Whether the product is enabled
     */
    val enabled: Boolean?,

    /**
     * Family code from which the product inherits its attributes and attributes requiremenents.
     */
    val family: String?,

    /**
     * Values of AkeneoProduct we're interested in
     */
    val values: AkeneoProductValues


) {
    override fun toString(): String {
        return "AkeneoProduct(identifier=$identifier, enabled=$enabled, family=$family, values=$values)"
    }
}