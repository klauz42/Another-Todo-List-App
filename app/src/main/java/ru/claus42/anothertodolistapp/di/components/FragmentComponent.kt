package ru.claus42.anothertodolistapp.di.components

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.claus42.anothertodolistapp.di.modules.ViewModelModule
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.presentation.accountinfo.fragments.AccountInfoFragment
import ru.claus42.anothertodolistapp.presentation.searchtodos.fragments.FilterOptionsFragment
import ru.claus42.anothertodolistapp.presentation.searchtodos.fragments.SearchTodosFragment
import ru.claus42.anothertodolistapp.presentation.searchtodos.fragments.SortOptionsFragment
import ru.claus42.anothertodolistapp.presentation.todoitemdetails.fragments.TodoItemDetailsFragment
import ru.claus42.anothertodolistapp.presentation.todoitemlist.fragments.TodoItemListFragment


@FragmentScope
@Component(
    dependencies = [ActivityComponent::class],
    modules = [ViewModelModule::class]
)
interface FragmentComponent {
    fun inject(fragment: TodoItemListFragment)
    fun inject(fragment: TodoItemDetailsFragment)
    fun inject(fragment: AccountInfoFragment)
    fun inject(fragment: SearchTodosFragment)
    fun inject(fragment: SortOptionsFragment)
    fun inject(fragment: FilterOptionsFragment)

    @Component.Builder
    interface Builder {
        fun activityComponent(activityComponent: ActivityComponent): Builder
        fun build(): FragmentComponent
    }

    fun getViewModelFactory(): ViewModelProvider.Factory
}