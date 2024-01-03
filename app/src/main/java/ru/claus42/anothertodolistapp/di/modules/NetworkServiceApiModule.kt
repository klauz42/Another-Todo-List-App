package ru.claus42.anothertodolistapp.di.modules

import dagger.Binds
import dagger.Module
import ru.claus42.anothertodolistapp.data.remote.NetworkServiceApi
import ru.claus42.anothertodolistapp.data.remote.firebase.FirebaseFirestoreServiceApi


@Module
interface NetworkServiceApiModule {
    @Suppress("FunctionName")
    @Binds
    fun bindFirebaseFirestoreService_to_NetworkServiceApi(
        firebaseFirestoreServiceApi: FirebaseFirestoreServiceApi
    ): NetworkServiceApi
}