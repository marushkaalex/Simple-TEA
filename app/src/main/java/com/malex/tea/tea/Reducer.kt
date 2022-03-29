package com.malex.tea.tea

interface Reducer<State, Event, Effect, Command> {
    fun reduce(state: State, event: Event): Next<State, Effect, Command>
}

data class Next<State, Effect, Command>(
    val state: State,
    val commands: List<Command> = emptyList(),
    val effects: List<Effect> = emptyList(),
)

class MyReducer : Reducer<State, Event, Effect, Command> {
    override fun reduce(state: State, event: Event): Next<State, Effect, Command> = when (event) {
        is Event.Internal.ValueLoadingSuccess -> Next(
            state = state.copy(
                isLoading = false,
                value = event.value
            ),
        )
        is Event.Internal.ValueLoadingError -> Next(
            state = state.copy(
                isLoading = false,
                value = "Error"
            ),
            effects = listOf(Effect.ShowError),
        )
        Event.Ui.Init -> Next(
            state = state.copy(
                isLoading = true
            ),
            commands = listOf(Command.LoadValue),
        )
        Event.Ui.ReloadClick -> Next(
            state = state.copy(
                isLoading = true,
                value = "Loading..."
            ),
            commands = listOf(Command.LoadValue),
        )
    }
}