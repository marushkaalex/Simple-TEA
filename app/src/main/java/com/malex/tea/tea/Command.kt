package com.malex.tea.tea

sealed class Command {
    object LoadValue : Command()
}