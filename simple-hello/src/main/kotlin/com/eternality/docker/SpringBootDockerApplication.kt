package com.eternality.docker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootDockerApplication

fun main(args: Array<String>) {
	runApplication<SpringBootDockerApplication>(*args)
}
