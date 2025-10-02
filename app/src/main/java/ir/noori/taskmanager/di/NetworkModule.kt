package ir.noori.taskmanager.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ir.noori.taskmanager.data.remote.api.LoginApiService
import ir.noori.taskmanager.data.remote.api.TaskApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TASKS_BASE_URL = "https://jsonplaceholder.typicode.com"
    private const val LOGIN_BASE_URL = "https://dummyjson.com"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    @Named("tasks")
    fun provideRetrofitTasks(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(TASKS_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("login")
    fun provideRetrofitLogin(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(LOGIN_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideTaskApiService(@Named("tasks") retrofit: Retrofit): TaskApiService =
        retrofit.create(TaskApiService::class.java)

    @Provides
    @Singleton
    fun provideLoginApiService(@Named("login") retrofit: Retrofit): LoginApiService =
        retrofit.create(LoginApiService::class.java)
}