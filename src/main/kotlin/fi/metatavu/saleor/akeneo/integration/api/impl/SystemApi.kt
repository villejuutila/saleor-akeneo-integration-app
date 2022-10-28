package fi.metatavu.saleor.akeneo.integration.api.impl

import fi.metatavu.saleor.akeneo.integration.spec.SystemApi
import javax.enterprise.context.RequestScoped
import javax.ws.rs.core.Response

/**
 * API implementation for SystemApi
 */
@RequestScoped
class SystemApi: SystemApi, AbstractApi() {

    override suspend fun ping(): Response {
        return createOk("pong")
    }
}