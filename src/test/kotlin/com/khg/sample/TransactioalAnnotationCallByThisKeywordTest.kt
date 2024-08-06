package com.khg.sample

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.Test
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationManager

private val logger = KotlinLogging.logger {}

/**
 * @Transactional AOP 적용 시 프록시를 통해 호출되며 이때 내부적으로 this를 통하여 호출하면 프록시를 거치지 않기 때문에 AOP가 적용되지 않는다.
 * 참고: https://tech.kakaopay.com/post/overcome-spring-aop-with-kotlin/
 */
@MybatisTest
@ActiveProfiles("test")
class TransactioalAnnotationCallByThisKeywordTest {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    /**
     * (this로 호출) 트랜잭션 적용 메서드A -> 트랜잭션 적용 메서드B
     * => 메서드A : 트랜잭션 O
     * => 메서드B : 트랜잭션 O
     */
    @Test
    @Transactional
    fun `test with transaction`() {
        val currentMethodName = Throwable().stackTrace[0].methodName
        this.logTransactionStatus(currentMethodName)
        this.testTransaction(currentMethodName)
    }

    /**
     * (this로 호출) 트랜잭션 미적용 메서드A -> 트랜잭션 적용 메서드B
     * => 메서드A : 트랜잭션 X
     * => 메서드B : 트랜잭션 X
     */
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun `test without transaction`() {
        val currentMethodName = Throwable().stackTrace[0].methodName
        this.logTransactionStatus(currentMethodName)
        this.testTransaction(currentMethodName)
    }

    /**
     * (빈을 생성하여 this를 우회하여 호출) 트랜잭션 미적용 메서드A -> 트랜잭션 적용 메서드B
     * => 메서드A : 트랜잭션 X
     * => 메서드B : 트랜잭션 O
     */
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun `test fix without transaction`() {
        val currentMethodName = Throwable().stackTrace[0].methodName
        this.logTransactionStatus(currentMethodName)
        applicationContext.getBean(this::class.java).testTransaction(currentMethodName)
    }

    @Transactional
    fun testTransaction(methodName: String) {
        logTransactionStatus(methodName)
    }

    private fun logTransactionStatus(methodName: String) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            val transactionName = TransactionSynchronizationManager.getCurrentTransactionName()
            logger.info { "[$methodName] Current Transaction Name: $transactionName" }
        } else {
            logger.info { "[$methodName] No active transaction" }
        }
    }

    @Configuration
    class TestConfig {
        @Bean
        fun transactionTest(): TransactioalAnnotationCallByThisKeywordTest {
            return TransactioalAnnotationCallByThisKeywordTest()
        }
    }

}