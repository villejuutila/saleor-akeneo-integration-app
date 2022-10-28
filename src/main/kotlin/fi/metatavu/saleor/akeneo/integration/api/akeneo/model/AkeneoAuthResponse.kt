package fi.metatavu.saleor.akeneo.integration.api.akeneo.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * POJO for storing Akeneo Auth endpoint response
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AkeneoAuthResponse (
    /**
     * Access token
     */
    @JsonProperty("access_token")
    val accessToken: String,

    /**
     * Expires in
     */
    @JsonProperty("expires_in")
    val expiresIn: Long,

    /**
     * Token type
     */
    @JsonProperty("token_type")
    val tokenType: String,

    /**
     * Refresh token
     */
    @JsonProperty("refresh_token")
    val refreshToken: String
)