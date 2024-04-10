package io.directional.wine.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.server.WebSession

@Configuration
class SwaggerConfig {
    init {
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(
            WebSession::class.java,
        )
    }

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                .title("wine rest api")
                .version("v0.0.1")
                .description("wine REST API")
            )
    }
}
