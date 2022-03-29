package com.malex.tea.tea

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface Store<State, Event, Effect> {
    val state: StateFlow<State>
    val effects: Flow<Effect>
    fun accept(event: Event)
}

class MyStore<State, Event, Effect, Command>(
    initialState: State,
    private val reducer: Reducer<State, Event, Effect, Command>,
    private val actor: Actor<Command, Event>,
) : Store<State, Event, Effect> {
    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<State> = _state
    private val _effects = MutableSharedFlow<Effect?>()
    override val effects: Flow<Effect> = _effects.filterNotNull()

    override fun accept(event: Event) {
        GlobalScope.launch(Dispatchers.Default) {
            val next = reducer.reduce(_state.value, event)
            _state.value = next.state
            next.effects.forEach {
                _effects.emit(it)
            }
            next.commands.forEach {
                withContext(Dispatchers.IO) {
                    val internalEvent = actor.execute(it)
                    internalEvent?.let(::accept)
                }
            }
        }
    }
}