package com.khg.sample.config

import org.assertj.core.api.Assertions.assertThat
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.junit.jupiter.api.Test


class JasyptTest : JasyptConfig() {
    @Test
    fun jasypt_encrypt_decrypt_test() {
        val plainText = "postgres"

        val jasypt = StandardPBEStringEncryptor()
        jasypt.setPassword("test_jasypt_key")

        val encryptedText = jasypt.encrypt(plainText)
        val decryptedText = jasypt.decrypt(encryptedText)

        println(encryptedText)

        assertThat(plainText).isEqualTo(decryptedText)
    }
}