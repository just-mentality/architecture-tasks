package com.eternality.controller

import com.eternality.dto.CreateUserRequest
import com.eternality.dto.UpdateUserRequest
import com.eternality.dto.UserView
import com.eternality.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.annotation.PostConstruct

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
    fun createUser(@RequestBody request: CreateUserRequest): UserView =
        userService.createUser(request)

    @GetMapping("/user/{userId}")
    fun getUser(@PathVariable("userId") userId: Long, principal: Principal): UserView {
        if (userService.haveAccessToUser(userId, principal)) {
            return userService.getUser(userId)
        } else {
            throw AccessDeniedException("No permissions to read information of userId=$userId")
        }
    }

    @DeleteMapping("/user/{userId}")
    fun deleteUser(@PathVariable("userId") userId: Long, principal: Principal): String {
        if (userService.haveAccessToUser(userId, principal)) {
            return userService.deleteUser(userId)
        } else {
            throw AccessDeniedException("No permissions to delete information of userId=$userId")
        }
    }

    @PutMapping("/user/{userId}")
    fun updateUser(
        @PathVariable("userId") userId: Long,
        @RequestBody request: UpdateUserRequest,
        principal: Principal
    ): UserView {
        if (userService.haveAccessToUser(userId, principal)) {
            return userService.updateUser(userId = userId, request = request)
        } else {
            throw AccessDeniedException("No permissions to update information of userId=$userId")
        }
    }

    @GetMapping("/")
    fun hello(): String =
        "Hello from $name!"
}

