package fi.metatavu.saleor.akeneo.integration.api.akeneo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * POJO for Akeneo PIM response pagination links
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AkeneoResponseLinks(
    val self: AkeneoResponseLink,
    val first: AkeneoResponseLink,
    val next: AkeneoResponseLink? = null
)