package com.airwallex.demo.jdbc

import com.airwallex.demo.jdbc.domain.Customer
import com.airwallex.demo.jdbc.domain.CustomerSearch
import com.airwallex.demo.jdbc.domain.Order
import com.airwallex.demo.jdbc.repository.CustomerRepository
import com.airwallex.demo.jdbc.repository.OrderRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import java.time.Duration
import java.util.UUID

@Configuration
class RouteConfig {

    @Bean
    fun routes(customerRepo: CustomerRepository, orderRepo: OrderRepository) = router {
        "/customers".nest {
            // Search
            GET("/searchByName") { req ->
                ok().body(customerRepo.findByName(req.queryParam("name").get().let { "%$it%" }))
            }
            POST("/search") { ok().body(it.bodyToMono(CustomerSearch::class.java).flatMapMany(customerRepo::search)) }

            // Get all customers
            GET("/") { ok().body(customerRepo.findAll()) }

            // Get customer by id
            GET("/{id}") { ok().body(customerRepo.findById(UUID.fromString(it.pathVariable("id")))) }

            // Create new customer
            POST("/") {
                accepted().body(
                    it.bodyToMono(Customer::class.java).delayElement(Duration.ofMillis(200)).flatMap(
                        customerRepo::save
                    )
                )
            }

            // Update existing customer
            PUT("/{id}") { ok().body(it.bodyToMono(Customer::class.java).flatMap(customerRepo::save)) }

            // Delete customer
            DELETE("/{id}") { ok().body(customerRepo.deleteById(UUID.fromString(it.pathVariable("id")))) }
            POST("/deleteAll") { ok().body(customerRepo.deleteAll()) }
        }

        "/orders".nest {
            GET("/searchByCustomer") { ok().body(orderRepo.findByCustomer(UUID.fromString(it.queryParam("customerId").get()))) }

            GET("/") { ok().body(orderRepo.findAll()) }

            // Get order by id
            GET("/{id}") { ok().body(orderRepo.findById(UUID.fromString(it.pathVariable("id")))) }

            // Create new order
            POST("/") {
                accepted().body(
                    it.bodyToMono(Order::class.java).delayElement(Duration.ofMillis(200)).flatMap(
                        orderRepo::save
                    )
                )
            }

            DELETE("/deleteByCustomer") { ok().body(orderRepo.deleteByCustomer(UUID.fromString(it.queryParam("customerId").get()))) }
            POST("/deleteAll") { ok().body(orderRepo.deleteAll()) }
        }

        GET("/ping") {
            ok().bodyValue(it.headers().asHttpHeaders())
        }
    }
}
