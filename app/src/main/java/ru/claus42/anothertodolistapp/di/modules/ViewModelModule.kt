package ru.claus42.anothertodolistapp.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import ru.claus42.anothertodolistapp.presentation.ViewModelFactory
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.FilterOptionsViewModel
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.SearchTodosViewModel
import ru.claus42.anothertodolistapp.presentation.searchtodos.stateholders.SortOptionsViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(SearchTodosViewModel::class)
    fun bindSearchTodosViewModel(viewModel: SearchTodosViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SortOptionsViewModel::class)
    fun bindSortOptionsViewModel(viewModel: SortOptionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FilterOptionsViewModel::class)
    fun bindFilterOptionsViewModel(viewModel: FilterOptionsViewModel): ViewModel
}

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)
