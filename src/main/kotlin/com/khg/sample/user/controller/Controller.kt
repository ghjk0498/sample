package com.khg.sample.user.controller

import com.khg.sample.user.domain.User
import com.khg.sample.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/users"])
class Controller(private val userService: UserService) {
    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Long): User? {
        return userService.findById(id)
    }

    @GetMapping("/")
    fun getAllUsers(): List<User> {
        return userService.findAll()
    }
}