package com.labinot.bajrami.fitnessapp.di

import android.content.Context
import androidx.credentials.CredentialManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.labinot.bajrami.fitnessapp.domain.repositories.AuthRepository
import com.labinot.bajrami.fitnessapp.domain.repositories.AuthRepositoryImp
import com.labinot.bajrami.fitnessapp.domain.repositories.DataBaseRepositoryImp
import com.labinot.bajrami.fitnessapp.domain.repositories.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideCredentialManager(
        @ApplicationContext context: Context
    ): CredentialManager {
        return CredentialManager.create(context)
    }


    @Provides
    @Singleton
    fun provideAuthRepository(
    firebaseAuth: FirebaseAuth,
    credentialManager: CredentialManager
    ): AuthRepository {
        return AuthRepositoryImp(firebaseAuth,credentialManager)
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): DatabaseRepository {
        return DataBaseRepositoryImp(firebaseAuth,firestore)
    }

}