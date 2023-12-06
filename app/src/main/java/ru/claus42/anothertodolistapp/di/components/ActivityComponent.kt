package ru.claus42.anothertodolistapp.di.components

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import ru.claus42.anothertodolistapp.di.scopes.ActivityScope
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.presentation.views.activities.MainActivity


@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = []
)
interface ActivityComponent {
    fun inject(activity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: AppCompatActivity): Builder
        fun appComponent(appComponent: AppComponent): Builder

        fun build(): ActivityComponent
    }

    fun getTodoItemRepository(): TodoItemRepository
}