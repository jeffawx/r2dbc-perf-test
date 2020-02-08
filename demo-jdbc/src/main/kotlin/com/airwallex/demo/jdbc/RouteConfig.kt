package com.airwallex.demo.jdbc

import com.airwallex.demo.jdbc.domain.Customer
import com.airwallex.demo.jdbc.domain.CustomerSearch
import com.airwallex.demo.jdbc.domain.Order
import com.airwallex.demo.jdbc.repository.CustomerRepository
import com.airwallex.demo.jdbc.repository.OrderRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.function.paramOrNull
import org.springframework.web.servlet.function.router
import java.util.UUID

@Configuration
class RouteConfig {

    @Bean
    fun routes(customerRepo: CustomerRepository, orderRepo: OrderRepository) = router {
        "/customers".nest {
            // Search
            GET("/searchByName") { req ->
                ok().body(customerRepo.findByName(req.paramOrNull("name")!!.let { "%$it%" }))
            }
            POST("/search") { ok().body(customerRepo.search(it.body(CustomerSearch::class.java))) }

            // Get all customers
            GET("/") { ok().body(customerRepo.findAll()) }

            // Get customer by id
            GET("/{id}") { ok().body(customerRepo.findById(UUID.fromString(it.pathVariable("id")))) }

            // Create new customer
            POST("/") {
                Thread.sleep(200)
                accepted().body(customerRepo.save(it.body(Customer::class.java)))
            }

            // Update existing customer
            PUT("/{id}") { ok().body(customerRepo.save(it.body(Customer::class.java))) }

            // Delete customer
            DELETE("/{id}") { ok().body(customerRepo.deleteById(UUID.fromString(it.pathVariable("id")))) }
            POST("/deleteAll") { ok().body(customerRepo.deleteAll()) }
        }

        "/orders".nest {
            GET("/searchByCustomer") { ok().body(orderRepo.findByCustomer(UUID.fromString(it.paramOrNull("customerId")))) }

            GET("/") { ok().body(orderRepo.findAll()) }

            // Get order by id
            GET("/{id}") { ok().body(orderRepo.findById(UUID.fromString(it.pathVariable("id")))!!) }

            // Create new order
            POST("/") {
                Thread.sleep(200)
                accepted().body(orderRepo.save(it.body(Order::class.java)))
            }

            DELETE("/deleteByCustomer") { ok().body(orderRepo.deleteByCustomer(UUID.fromString(it.paramOrNull("customerId")))) }
            POST("/deleteAll") { ok().body(orderRepo.deleteAll()) }
        }

        GET("/ping") {
            ok().contentType(MediaType.APPLICATION_JSON).body(it.headers().asHttpHeaders())
        }
    }
}
