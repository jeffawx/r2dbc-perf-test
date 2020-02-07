package com.airwallex.demo.r2dbc.domain

import com.airwallex.common.lang.Codecs
import com.airwallex.common.postgres.annotations.Entity
import com.airwallex.common.postgres.model.BaseEntity
import com.fasterxml.jackson.annotation.JsonFormat
import com.neovisionaries.i18n.CurrencyCode
import java.math.BigDecimal
import java.util.Date
import java.util.UUID

@Entity("orders")
data class Order(

    override var id: UUID = UUID.randomUUID(),

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    override var createTime: Date? = Date(),
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    override var lastUpdate: Date? = Date(),
    override var version: Long? = 0,

    val ccy: CurrencyCode,
    val amount: BigDecimal,
    val customerId: UUID
) : BaseEntity

fun main() {
    val order = Order(ccy = CurrencyCode.USD, amount = BigDecimal.TEN, customerId = UUID.randomUUID())
    println(Codecs.pprint(order))
}