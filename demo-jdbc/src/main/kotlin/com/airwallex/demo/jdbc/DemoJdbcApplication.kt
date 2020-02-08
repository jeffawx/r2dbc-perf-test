package com.airwallex.demo.jdbc

import ch.sbb.esta.openshift.gracefullshutdown.GracefulshutdownSpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DemoJdbcApplication

fun main(args: Array<String>) {
    GracefulshutdownSpringApplication.run(DemoJdbcApplication::class.java, *args)
}
