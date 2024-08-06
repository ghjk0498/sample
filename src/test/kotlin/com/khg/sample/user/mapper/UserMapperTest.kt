package com.khg.sample.user.mapper

import com.khg.sample.annotation.PrintMemory
import com.khg.sample.user.domain.User
import org.junit.jupiter.api.Test
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles

@MybatisTest
@ActiveProfiles("test")
class UserMapperTest {

    @Autowired
    private lateinit var userMapper: UserMapper

    @Test
    @PrintMemory
    fun `test memory`() {
        val user: User? = userMapper.findById(1)
        user?.id
    }

    @Test
    @PrintMemory
    fun `test memory 5`() {
        for (i in 1..10) {
            val user: User? = userMapper.findById(1)
            user?.id
        }
    }

}