package com.airwallex.demo.r2dbc

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration
class RouteConfig {

    @Bean
    fun routes() = router {
        GET("/ping") {
            ok().bodyValue(it.headers().asHttpHeaders())
        }
    }
}
