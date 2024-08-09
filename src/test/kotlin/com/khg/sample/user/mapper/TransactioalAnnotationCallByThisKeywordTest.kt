package com.khg.sample.user.mapper

import com.khg.sample.user.domain.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.GenericBeanDefinition
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.interceptor.TransactionAspectSupport
import org.springframework.transaction.support.TransactionSynchronizationManager


/**
 * @Transactional AOP 적용 시 프록시를 통해 호출되며 이때 내부적으로 this 를 통하여 호출하면 프록시를 거치지 않기 때문에 AOP 가 적용되지 않는다.
 * 참고: https://tech.kakaopay.com/post/overcome-spring-aop-with-kotlin/
 */
@MybatisTest
@ActiveProfiles("test")
class TransactionalAnnotationCallByThisKeywordTest {

    @Autowired
    private lateinit var context: ConfigurableApplicationContext

    @Autowired
    private lateinit var userMapper: UserMapper

    /**
     * 일반 메서드 내에서 트랜잭션 메서드를 this 로 호출 : 트랜잭션 적용 X
     * this 를 통해 호출하면 프록시가 동작하지 않아 트랜잭션이 적용되지 않는다.
     */
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun `transaction is null`() {
        val user = User(null, "tx", "tx")
        assertThat(this.insertUserAndReturnTransactionName(user)).isNull()

        assertThat(this.userMapper.findByName("tx")).isNotNull()
    }

    /**
     * 일반 메서드 내에서 트랜잭션 메서드를 빈을 통해 우회하여 호출 : 트랜잭션 적용 O
     * 빈을 통해 호출하면 프록시가 동작하여 트랜잭션이 적용된다.
     */
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    fun `transaction is not null`() {
        val registry = context.beanFactory as BeanDefinitionRegistry
        val beanDefinition = GenericBeanDefinition()
        beanDefinition.setBeanClass(this::class.java)
        registry.registerBeanDefinition("myBean", beanDefinition)

        val user = User(null, "tx", "tx")
        assertThat(context.getBean(this::class.java).insertUserAndReturnTransactionName(user)).isNotNull()

        assertThat(this.userMapper.findByName("tx")).isNull()
    }

    @Transactional
    fun insertUserAndReturnTransactionName(user: User): String? {
        this.userMapper.insert(user)

        try {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()
        } catch (_: Exception) {
        }

        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            return TransactionSynchronizationManager.getCurrentTransactionName()
        }
        return null
    }

//    @Configuration
//    class TestConfig {
//        @Bean
//        fun transactionTest(): TransactionalAnnotationCallByThisKeywordTest {
//            return TransactionalAnnotationCallByThisKeywordTest()
//        }
//    }

}