package ru.claus42.anothertodolistapp.di.modules

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.flow.flowOf
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import ru.claus42.anothertodolistapp.data.remote.NetworkServiceApi
import ru.claus42.anothertodolistapp.domain.models.DataResult


@Module
class NoAuthTestNetworkServiceApiModule {
    @Provides
    fun provideTestNetworkServiceApi(): NetworkServiceApi {
        return mock<NetworkServiceApi> {
            on { getTodoItems() } doReturn flowOf(DataResult.ok())
        }
    }
}