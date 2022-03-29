package com.malex.tea.tea

import kotlinx.coroutines.delay
import kotlin.random.Random

object ValueRepository {

    suspend fun getValue(): Result<String> {
        delay(1000)
        return if (Random.nextBoolean()) {
            Result.success("Yeah!")
        } else {
            Result.failure(IllegalStateException("Some error message"))
        }
    }
}