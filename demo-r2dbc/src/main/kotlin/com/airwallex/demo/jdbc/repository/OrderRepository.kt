package com.airwallex.demo.jdbc.repository

import com.airwallex.common.rx.db.BaseEntityRepository
import com.airwallex.common.rx.db.delete
import com.airwallex.common.rx.db.fromTable
import com.airwallex.demo.jdbc.domain.Order
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class OrderRepository(databaseClient: DatabaseClient) :
    BaseEntityRepository<Order>(databaseClient, Order::class) {

    fun findByCustomer(customerId: UUID) =
        databaseClient.fromTable(tableName, mapper) {
            +("data->>'customerId'" eq customerId.toString())
        }.results

    fun deleteByCustomer(customerId: UUID) =
        databaseClient.delete(tableName) {
            +("data->>'customerId'" eq customerId.toString())
        }
}
