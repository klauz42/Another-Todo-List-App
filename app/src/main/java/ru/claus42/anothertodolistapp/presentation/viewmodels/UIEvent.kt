package ru.claus42.anothertodolistapp.presentation.viewmodels

sealed class UIEvent {
    data object DataUpdated : UIEvent()
}
