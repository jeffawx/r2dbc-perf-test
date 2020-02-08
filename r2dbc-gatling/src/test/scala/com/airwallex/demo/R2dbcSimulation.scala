package com.airwallex.demo

import java.util.UUID

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class R2dbcSimulation extends Simulation {

  val feeder = Iterator.continually(Map("user" -> Random.alphanumeric.take(20).mkString))
  val uuidFeeder = Iterator.continually(Map("uuid" -> UUID.randomUUID().toString))

  val httpProtocol = http
    .baseUrl("http://34.102.238.220")
    .acceptHeader("application/json")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn = scenario("new customer submitting order").forever {
    feed(feeder)
      .exec(http("create new customer")
        .post("/customers")
        .body(StringBody("""{"name": "${user}"}""")).asJson
        .check(status is 202)
        .check(jsonPath("$.id").saveAs("customerId"))
      ).exitHereIfFailed

      .exec(http("update customer email")
        .put("/customers/${customerId}")
        .body(StringBody(
          """{
          "id": "${customerId}",
          "name": "${user}",
          "email": "${user}@airwallex.com"
         }""")).asJson
        .check(status is 200)
      )

      .repeat(3)(
        feed(uuidFeeder)
          .exec(http("customer place order")
            .post("/orders")
            .body(StringBody(
              """{
            "id" : "${uuid}",
            "amount" : 100,
            "ccy" : "USD",
            "customerId" : "${customerId}"
           }""")).asJson
            .check(status is 202)
          )
      )

      .exec(http("view customer profile")
        .get("/customers/${customerId}")
        .check(status is 200)
      )

      .exec(http("view customer orders")
        .get("/orders/searchByCustomer?customerId=${customerId}")
        .check(status is 200)
      )
  }

  setUp(
    scn.inject(
      rampUsers(4000) during (30 seconds)
    )
  ).maxDuration(10 minutes).protocols(httpProtocol)
}
