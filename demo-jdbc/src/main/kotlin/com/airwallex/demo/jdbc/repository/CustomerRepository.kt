package com.airwallex.demo.jdbc.repository

import com.airwallex.common.postgres.pagination.orWildcard
import com.airwallex.common.postgres.pagination.param
import com.airwallex.common.postgres.pagination.predicate
import com.airwallex.common.postgres.pagination.query
import com.airwallex.common.postgres.pagination.runQuery
import com.airwallex.demo.jdbc.domain.Customer
import com.airwallex.demo.jdbc.domain.CustomerSearch
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.sql.ResultSet
import java.util.UUID

interface CustomerRepository : CrudRepository<Customer, UUID>, CustomerOperations {

    @Query("SELECT * FROM customer WHERE name ILIKE :name")
    fun findByName(name: String): List<Customer>
}

interface CustomerOperations {
    fun search(search: CustomerSearch): List<Customer>
}

internal class CustomerRepositoryImpl(private val jdbcTemplate: NamedParameterJdbcTemplate) : CustomerOperations {

    override fun search(search: CustomerSearch) = jdbcTemplate.runQuery(query {
        tableName = "customer"
        -predicate("name ILIKE :name", search.name)
        search.name?.let { +param("name" to it.orWildcard()) }

        +predicate("email = :email", search.email)
        search.email?.let { +param("email" to it) }

        +predicate("address ILIKE :address", search.address)
        search.address?.let { +param("address" to it.orWildcard()) }

        fuzzySearch = true
    }, CustomerMapper).get().results
}

object CustomerMapper : RowMapper<Customer> {
    override fun mapRow(rs: ResultSet, rowNum: Int) = Customer(
        id = UUID.fromString(rs.getString("id")),
        name = rs.getString("name"),
        email = rs.getString("email"),
        address = rs.getString("address"),
        createTime = rs.getTimestamp("create_time"),
        lastUpdate = rs.getTimestamp("last_update")
    )
}
