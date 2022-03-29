package com.malex.tea.tea

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        object ReloadClick : Ui()
    }

    sealed class Internal : Event() {
        data class ValueLoadingSuccess(val value: String) : Internal()
        data class ValueLoadingError(val throwable: Throwable) : Internal()
    }
}