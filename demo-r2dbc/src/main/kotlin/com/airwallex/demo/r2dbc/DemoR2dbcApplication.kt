package com.airwallex.demo.r2dbc

import ch.sbb.esta.openshift.gracefullshutdown.GracefulshutdownSpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DemoR2dbcApplication

fun main(args: Array<String>) {
    GracefulshutdownSpringApplication.run(DemoR2dbcApplication::class.java, *args)
}
