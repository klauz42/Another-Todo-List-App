package ru.claus42.anothertodolistapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import ru.claus42.anothertodolistapp.presentation.ViewModelFactory
import ru.claus42.anothertodolistapp.presentation.todoitemdetails.stateholders.TodoItemDetailsViewModel
import ru.claus42.anothertodolistapp.presentation.todoitemlist.stateholders.TodoItemListViewModel
import kotlin.reflect.KClass

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TodoItemListViewModel::class)
    fun bindTodoItemListViewModel(viewModel: TodoItemListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TodoItemDetailsViewModel::class)
    fun bindTodoItemDetailsViewModel(viewModel: TodoItemDetailsViewModel): ViewModel
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
