package fi.metatavu.saleor.akeneo.integration.api.akeneo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * POJO for storing response metadata from Akeneo PIM HTTP requests
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AkeneoProductsResponse (

    /**
     * Links to different pages of paginated response
     */
    @JsonProperty("_links")
    val links: AkeneoResponseLinks,

    /**
     * Actual content of the response
     */
    @JsonProperty("_embedded")
    val embedded: AkeneoResponseEmbedded<AkeneoProduct>
)