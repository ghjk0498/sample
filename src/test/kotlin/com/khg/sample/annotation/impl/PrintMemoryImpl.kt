package com.khg.sample.annotation.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import java.lang.management.ManagementFactory
import java.lang.management.MemoryMXBean
import java.lang.management.MemoryUsage

private val logger = KotlinLogging.logger {}

class PrintMemoryImpl : BeforeTestExecutionCallback, AfterTestExecutionCallback {

    override fun beforeTestExecution(context: ExtensionContext) {
        printMemory(context.displayName)
    }

    override fun afterTestExecution(context: ExtensionContext) {
        printMemory(context.displayName)
    }

    private fun printMemory(displayName: String) {
        val runtime = Runtime.getRuntime()
        val usedMemory = runtime.totalMemory() - runtime.freeMemory()

        val memoryMXBean: MemoryMXBean = ManagementFactory.getMemoryMXBean()
        val heapMemoryUsage: MemoryUsage = memoryMXBean.heapMemoryUsage
        val usedHeapMemory: Long = heapMemoryUsage.used

        logger.info { "[$displayName]현재 사용 중인 메모리: ${usedMemory / 1024} KB" }
        logger.info { "[$displayName]현재 사용 중인 힙 메모리: ${usedHeapMemory / 1024} KB" }
    }

}