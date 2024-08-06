package com.khg.sample.annotation

import com.khg.sample.annotation.impl.PrintMemoryImpl
import org.junit.jupiter.api.extension.ExtendWith

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@ExtendWith(PrintMemoryImpl::class)
annotation class PrintMemory
