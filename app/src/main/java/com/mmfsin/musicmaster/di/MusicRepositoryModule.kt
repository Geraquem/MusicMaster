package com.mmfsin.musicmaster.di

import com.mmfsin.musicmaster.data.repository.MusicRepository
import com.mmfsin.musicmaster.domain.interfaces.IMusicRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface MusicRepositoryModule {
    @Binds
    fun bind(repository: MusicRepository): IMusicRepository
}