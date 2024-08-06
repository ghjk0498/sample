package com.khg.sample.user.mapper

import com.khg.sample.user.domain.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    fun findById(userId: Long): User?

    @Select("SELECT * FROM users")
    fun findAll(): List<User>
}