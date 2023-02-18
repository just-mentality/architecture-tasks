package com.eternality.controller

import com.eternality.dto.CreateUserRequest
import com.eternality.dto.UpdateUserRequest
import com.eternality.dto.User
import com.eternality.service.UserService
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class UserController(private val userService: UserService) {

    @Value("\${app.hello.name}")
    private lateinit var name: String

    @PostConstruct
    fun postInit() {
        println("Initialized with user=$name")
    }

    @PostMapping("/user")
    fun createUser(@RequestBody request: CreateUserRequest): User =
        userService.createUser(request)

    @GetMapping("/user/{userId}")
    fun getUser(@PathVariable("userId") userId: Long): User =
        userService.getUser(userId)

    @DeleteMapping("/user/{userId}")
    fun deleteUser(@PathVariable("userId") userId: Long): String =
        userService.deleteUser(userId)

    @PutMapping("/user/{userId}")
    fun updateUser(@PathVariable("userId") userId: Long, @RequestBody request: UpdateUserRequest): User =
        userService.updateUser(userId = userId, request = request)

    @GetMapping("/")
    fun hello(): String =
        "Hello from $name!"
}