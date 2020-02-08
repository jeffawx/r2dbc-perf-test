package com.airwallex.demo.jdbc

import ch.sbb.esta.openshift.gracefullshutdown.GracefulshutdownSpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class DemoR2dbcApplication

fun main(args: Array<String>) {
    //BlockHound.install()
    GracefulshutdownSpringApplication.run(DemoR2dbcApplication::class.java, *args)
}
