package com.breezenationalplasticfsm.features.logout.presentation.api

/**
 * Created by Pratishruti on 23-11-2017.
 */
object LogoutRepositoryProvider {
    fun provideLogoutRepository(): LogoutRepository {
        return LogoutRepository(LogoutApi.create())
    }
}