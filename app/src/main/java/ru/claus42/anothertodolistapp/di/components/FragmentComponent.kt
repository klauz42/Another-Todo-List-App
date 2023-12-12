package ru.claus42.anothertodolistapp.di.components

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import ru.claus42.anothertodolistapp.di.modules.ViewModelModule
import ru.claus42.anothertodolistapp.di.scopes.FragmentScope
import ru.claus42.anothertodolistapp.presentation.views.fragments.todoitemdetails.TodoItemDetailsFragment
import ru.claus42.anothertodolistapp.presentation.views.fragments.todoitemlist.TodoItemListFragment


@FragmentScope
@Component(
    dependencies = [ActivityComponent::class],
    modules = [ViewModelModule::class]
)
interface FragmentComponent {
    fun inject(fragment: TodoItemListFragment)
    fun inject(fragment: TodoItemDetailsFragment)

    @Component.Builder
    interface Builder {
        fun activityComponent(activityComponent: ActivityComponent): Builder
        fun build(): FragmentComponent
    }

    fun getViewModelFactory(): ViewModelProvider.Factory
}