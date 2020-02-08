package com.airwallex.demo.jdbc.repository

import com.airwallex.common.lang.Codecs
import com.airwallex.common.postgres.mapper.EntityRowMapper
import com.airwallex.demo.jdbc.domain.Order
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class OrderRepository(private val jdbcTemplate: NamedParameterJdbcTemplate) {

    fun findByCustomer(customerId: UUID): List<Order> =
        jdbcTemplate.query(
            "SELECT * FROM orders WHERE data->>'customerId' = :customerId", mapOf(
                "customerId" to customerId.toString()
            ), OrderRowMapper
        )

    fun deleteByCustomer(customerId: UUID) =
        jdbcTemplate.update(
            "DELETE FROM orders WHERE data->>'customerId' = :customerId",
            mapOf("customerId" to customerId.toString())
        )

    fun findAll(): List<Order> = jdbcTemplate.query("SELECT * FROM orders", OrderRowMapper)

    fun findById(id: UUID) =
        jdbcTemplate.query(
            "SELECT * FROM orders WHERE id = :id", mapOf("id" to id), OrderRowMapper
        ).firstOrNull()

    fun save(order: Order): Order {
        // deliberately not using pg UPSERT to keep consistent with r2dbc impl.
        return jdbcTemplate.query(
            "UPDATE orders SET data = :data::jsonb WHERE id = :id RETURNING *",
            mapOf("id" to order.id, "data" to Codecs.objToJsonString(order)), OrderRowMapper
        ).firstOrNull()
            ?: jdbcTemplate.query(
                "INSERT INTO orders (id, data) VALUES (:id, :data::jsonb) RETURNING *",
                mapOf("id" to order.id, "data" to Codecs.objToJsonString(order)), OrderRowMapper
            ).first()
    }

    fun deleteAll() {
        jdbcTemplate.update("DELETE FROM orders", emptyMap<String, Any>())
    }
}

val OrderRowMapper = EntityRowMapper(Order::class.java)
