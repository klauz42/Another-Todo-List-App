package ru.claus42.anothertodolistapp.di.components

import androidx.appcompat.app.AppCompatActivity
import dagger.BindsInstance
import dagger.Component
import ru.claus42.anothertodolistapp.di.modules.AuthBindModule
import ru.claus42.anothertodolistapp.di.scopes.ActivityScope
import ru.claus42.anothertodolistapp.domain.models.TodoItemRepository
import ru.claus42.anothertodolistapp.presentation.MainActivity
import ru.claus42.anothertodolistapp.presentation.auth.activities.SignInActivity


@ActivityScope
@Component(
    dependencies = [AppComponent::class],
    modules = [AuthBindModule::class]
)
interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: SignInActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun activity(activity: AppCompatActivity): Builder
        fun appComponent(appComponent: AppComponent): Builder

        fun build(): ActivityComponent
    }

    fun getTodoItemRepository(): TodoItemRepository
}