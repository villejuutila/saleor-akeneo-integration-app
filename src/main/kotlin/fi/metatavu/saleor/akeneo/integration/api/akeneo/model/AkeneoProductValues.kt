package fi.metatavu.saleor.akeneo.integration.api.akeneo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * POJO for AkeneoProduct values we're intered in
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AkeneoProductValues (
    /**
     * Name of product
     */
    val name: List<AkeneoProductValue>?,

    /**
     * Name of product in ERP system (seems to be simplified version of name property)
     */
    @JsonProperty("erp_name")
    val erpName: List<AkeneoProductValue>?,

    /**
     * Product description
     */
    val description: List<AkeneoProductValue>?
) {
    override fun toString(): String {
        return "AkeneoProductValues(name=$name, erpName=$erpName, description=$description)"
    }
}