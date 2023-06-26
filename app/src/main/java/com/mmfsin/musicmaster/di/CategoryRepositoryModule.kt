package com.mmfsin.musicmaster.di

import com.mmfsin.musicmaster.data.repository.CategoryRepository
import com.mmfsin.musicmaster.domain.interfaces.ICategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface CategoryRepositoryModule {
    @Binds
    fun bind(repository: CategoryRepository): ICategoryRepository
}