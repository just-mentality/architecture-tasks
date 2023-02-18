package com.eternality.docker.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {

    @GetMapping("/health")
    fun status(): String = "{\"status\": \"OK\"}"

    @GetMapping("/")
    fun liveness(): String = "Alive from ${System.getenv("HOSTNAME")}!"

}