package ru.claus42.anothertodolistapp.domain.models

sealed class DataResult<out T> {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error<T>(val error: Throwable) : DataResult<T>()
    object OK : DataResult<Nothing>()
    object Loading : DataResult<Nothing>()

    companion object {
        inline fun <T> on(f: () -> T): DataResult<T> = try {
            Success(f())
        } catch (exception: Exception) {
            Error(exception)
        }

        inline fun onWithoutData(f: () -> Unit): DataResult<Nothing> = try {
            f()
            OK
        } catch (exception: Exception) {
            Error(exception)
        }

        fun loading() = Loading
        fun ok() = OK
    }
}