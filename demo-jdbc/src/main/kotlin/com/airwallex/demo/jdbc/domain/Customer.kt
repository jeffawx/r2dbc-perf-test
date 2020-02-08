package com.airwallex.demo.jdbc.domain

import org.springframework.data.annotation.Id
import java.util.Date
import java.util.UUID

data class Customer(
    @Id
    val id: UUID? = null,
    val name: String,
    val email: String? = null,
    val address: String? = null,
    val createTime: Date? = Date(),
    val lastUpdate: Date? = Date()
)

data class CustomerSearch(
    val name: String? = null,
    val email: String? = null,
    val address: String? = null
)
