package com.eternality.service

import com.eternality.domain.UserEntity
import com.eternality.domain.repository.UserRepository
import com.eternality.dto.CreateUserRequest
import com.eternality.dto.UpdateUserRequest
import com.eternality.dto.User
import com.eternality.exception.ServiceException
import com.eternality.mapper.mapToUser
import com.eternality.mapper.mapToUserEntity
import java.util.Optional
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun createUser(request: CreateUserRequest): User =
        userRepository.findByUserName(request.userName).orElseGet {
            userRepository.saveAndFlush(request.mapToUserEntity())
        }.mapToUser()

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun updateUser(userId: Long, request: UpdateUserRequest): User {
        val userEntity = getUserById(userId)
        request.apply {
            if (this.userName.needUpdate()) {
                userEntity.userName = this.userName.extract()
            }
            if (this.firstName.needUpdate()) {
                userEntity.firstName = this.firstName.extract()
            }
            if (this.lastName.needUpdate()) {
                userEntity.lastName = this.lastName.extract()
            }
            if (this.email.needUpdate()) {
                userEntity.email = this.email.extract()
            }
            if (this.phone.needUpdate()) {
                userEntity.phone = this.phone.extract()
            }
        }
        return userRepository.saveAndFlush(userEntity).mapToUser()
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    fun getUser(userId: Long): User =
        getUserById(userId).mapToUser()

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun deleteUser(userId: Long): String {
        val userEntity = getUserById(userId)
        userRepository.delete(userEntity)
        return "User with id $userId successfully deleted."
    }

    private fun getUserById(userId: Long): UserEntity =
        userRepository.findById(userId).orElseThrow { ServiceException("User with id $userId not found!") }

    fun <T> Optional<T?>?.needUpdate(): Boolean = this != null

    fun <T> Optional<T?>?.extract(): T? =
        when {
            this!!.isEmpty -> null
            else -> this.get()
        }

}