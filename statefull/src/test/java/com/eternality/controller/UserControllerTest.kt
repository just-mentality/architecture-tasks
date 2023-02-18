package com.eternality.controller

import com.eternality.AbstractTest
import com.eternality.dto.CreateUserRequest
import com.eternality.dto.UpdateUserRequest
import com.eternality.exception.ServiceException
import java.util.Optional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class UserControllerTest : AbstractTest() {

    private companion object {
        val REQUEST = CreateUserRequest(
            userName = "Alice Marshall",
            firstName = "Alice",
            lastName = "Marshall",
            email = "alice@gg.com",
            phone = "+123456789"
        )
    }

    @BeforeAll
    fun cleanBeforeAll() {
        cleanTestProcessInformation()
    }

    @AfterEach
    fun cleanAfterEach() {
        cleanTestProcessInformation()
    }

    @AfterAll
    fun cleanAfterAll() {
        cleanTestProcessInformation()
    }

    private fun cleanTestProcessInformation() {
        userRepository.deleteAll()
    }

    @Test
    fun `test create user`() {
        // act
        val createdUser = userController.createUser(REQUEST)

        // assert
        assertThat(createdUser.userId).isNotNull
        assertThat(createdUser.userName).isEqualTo(REQUEST.userName)
        assertThat(createdUser.firstName).isEqualTo(REQUEST.firstName)
        assertThat(createdUser.lastName).isEqualTo(REQUEST.lastName)
        assertThat(createdUser.email).isEqualTo(REQUEST.email)
        assertThat(createdUser.phone).isEqualTo(REQUEST.phone)
    }

    @Test
    fun `test get user`() {
        // act
        val createdUser = userController.createUser(REQUEST)
        val foundUser = userController.getUser(createdUser.userId)

        // assert
        assertThat(foundUser.userId).isNotNull
        assertThat(foundUser.userId).isEqualTo(createdUser.userId)
        assertThat(foundUser.userName).isEqualTo(createdUser.userName)
        assertThat(foundUser.firstName).isEqualTo(createdUser.firstName)
        assertThat(foundUser.lastName).isEqualTo(createdUser.lastName)
        assertThat(foundUser.email).isEqualTo(createdUser.email)
        assertThat(foundUser.phone).isEqualTo(createdUser.phone)
    }

    @Test
    fun `test delete user`() {
        // act
        val createdUser = userController.createUser(REQUEST)
        val userId = createdUser.userId
        assertDoesNotThrow { userController.getUser(userId) }

        val result = userController.deleteUser(userId)
        assertThat(result).isEqualTo("User with id $userId successfully deleted.")

        val serviceException = assertThrows<ServiceException> { userController.getUser(userId) }
        assertThat(serviceException.message).isEqualTo("User with id $userId not found!")
    }

    @Test
    fun `test update user`() {
        // arrange
        val createdUser = userController.createUser(REQUEST)
        val userId = createdUser.userId
        assertDoesNotThrow { userController.getUser(userId) }

        val updateRequest = UpdateUserRequest.builder()
            .userName(Optional.of("Alice Mac Marshall"))
            .firstName(Optional.ofNullable(null))
            .email(Optional.of("alice-mac@gg.com"))
            .phone(Optional.empty())
            .build()

        // act
        val updatedUser = userController.updateUser(userId = userId, request = updateRequest)

        assertThat(updatedUser.userId).isNotNull
        assertThat(updatedUser.userId).isEqualTo(createdUser.userId)
        // changed attrs
        assertThat(updatedUser.userName).isEqualTo(updateRequest.userName.get())
        assertThat(updatedUser.firstName).isEqualTo(null)
        assertThat(updatedUser.phone).isEqualTo(null)
        assertThat(updatedUser.email).isEqualTo(updateRequest.email.get())
        // NOT changed attrs
        assertThat(updatedUser.lastName).isEqualTo(createdUser.lastName)
    }
}