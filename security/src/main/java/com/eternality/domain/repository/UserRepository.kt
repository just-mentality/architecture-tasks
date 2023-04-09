package com.eternality.domain.repository

import com.eternality.domain.UserEntity
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {

    fun findByUserName(userName: String?): Optional<UserEntity>
}