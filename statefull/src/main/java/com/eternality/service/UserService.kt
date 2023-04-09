package com.eternality.service

import com.eternality.domain.UserEntity
import com.eternality.domain.repository.UserRepository
import com.eternality.dto.CreateUserRequest
import com.eternality.dto.UpdateUserRequest
import com.eternality.dto.UserView
import com.eternality.exception.ServiceException
import com.eternality.mapper.mapToUser
import com.eternality.mapper.mapToUserEntity
import org.slf4j.LoggerFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
import java.util.Optional
import javax.validation.ValidationException

@Service
class UserService(
    private val userRepository: UserRepository,
    private var passwordEncoder: PasswordEncoder
    ) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    fun haveAccessToUser(userId: Long, principal: Principal): Boolean {
        val principalName = principal.name
        val user = getUserById(userId)
        if (String.format("%s,%s", user.userId, user.userName) == principalName) {
            return true
        }
        return false
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun createUser(request: CreateUserRequest): UserView {
        if (userRepository.findByUserName(request.userName).isPresent) {
            throw ValidationException("User with userName=${request.userName} already exists!")
        }
        val userEntity: UserEntity = userRepository.saveAndFlush(request.mapToUserEntity(passwordEncoder))
        log.info("Created new user with id=${userEntity.userId}, name=${userEntity.userName}")
        return userEntity.mapToUser()
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    fun updateUser(userId: Long, request: UpdateUserRequest): UserView {
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
    fun getUser(userId: Long): UserView =
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