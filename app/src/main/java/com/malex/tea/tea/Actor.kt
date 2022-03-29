package com.malex.tea.tea

import java.lang.IllegalStateException

interface Actor <Command, Event> {
    suspend fun execute(command: Command): Event?
}

class MyActor : Actor<Command, Event> {
    override suspend fun execute(command: Command): Event? {
        return when (command) {
            is Command.LoadValue -> {
                ValueRepository.getValue()
                    .mapEvent(Event.Internal::ValueLoadingSuccess, Event.Internal::ValueLoadingError)
            }
        }
    }
}

fun <T, E> Result<T>.mapEvent(success: (T) -> E, fail: (Throwable) -> E): E {
    return when {
        this.isSuccess -> success(this.getOrThrow())
        this.isFailure -> fail(this.exceptionOrNull()!!)
        else -> throw IllegalStateException()
    }
}