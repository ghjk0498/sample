package com.khg.sample.user.service

import com.khg.sample.user.domain.User
import com.khg.sample.user.mapper.UserMapper
import org.springframework.stereotype.Service

@Service
class UserService(private val userMapper: UserMapper) {
    fun findById(id: Long): User? {
        return userMapper.findById(id)
    }

    fun findAll(): List<User> {
        return userMapper.findAll()
    }
}