package com.labinot.bajrami.fitnessapp.domain.repositories

import android.content.Context
import com.labinot.bajrami.fitnessapp.domain.model.AuthStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val authStatus: Flow<AuthStatus>
    suspend fun signInAnonymously(): Result<Boolean>
    suspend fun signIn(context: Context): Result<Boolean>
    suspend fun anonymousUserSignInWithGoogle(context: Context): Result<Boolean>
    suspend fun signOut(): Result<Boolean>


}