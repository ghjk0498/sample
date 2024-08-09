package com.khg.sample.user.mapper

import com.khg.sample.user.domain.User
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    fun findById(userId: Long): User?

    @Select("SELECT * FROM users WHERE name = #{name}")
    fun findByName(name: String): User?

    @Select("SELECT * FROM users")
    fun findAll(): List<User>

    @Insert("INSERT INTO users(name, email) VALUES (#{name}, #{email})")
    fun insert(user: User)

}