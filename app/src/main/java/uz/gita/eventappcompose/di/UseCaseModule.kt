package uz.gita.eventappcompose.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.eventappcompose.domain.usecase.UseCase
import uz.gita.eventappcompose.domain.usecase.UseCaseImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface UseCaseModule {

    @[Binds Singleton]
    fun bindEventUseCase(impl: UseCaseImpl): UseCase
}