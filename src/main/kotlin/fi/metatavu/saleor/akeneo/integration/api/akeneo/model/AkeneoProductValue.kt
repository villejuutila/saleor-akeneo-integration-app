package fi.metatavu.saleor.akeneo.integration.api.akeneo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * POJO for AkeneoProductValue objects
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AkeneoProductValue (
    /**
     * Locale code of value
     */
    val locale: String?,

    /**
     * Actual data of value (seems to be a mix of String/Int types but we're intered in types of String)
     */
    val data: String?
) {
    override fun toString(): String {
        return "AkeneoProductValue(locale=$locale, data=$data)"
    }
}