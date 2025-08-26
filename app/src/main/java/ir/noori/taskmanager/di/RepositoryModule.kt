package ir.noori.taskmanager.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.noori.taskmanager.data.repository.LoginRepositoryImpl
import ir.noori.taskmanager.data.repository.TaskRepositoryImpl
import ir.noori.taskmanager.domain.repository.LoginRepository
import ir.noori.taskmanager.domain.repository.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        impl: LoginRepositoryImpl
    ): LoginRepository
}
