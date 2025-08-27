package com.mmfsin.musicmaster.di

import com.mmfsin.musicmaster.data.database.RealmDatabase
import com.mmfsin.musicmaster.data.models.CategoryDTO
import com.mmfsin.musicmaster.domain.interfaces.IRealmDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

@Module
@InstallIn(ViewModelComponent::class, ServiceComponent::class)
object RealmDatabaseModule {

    @Provides
    fun provideRealmDatabase(): IRealmDatabase {
        val config = RealmConfiguration.create(
            schema = setOf(
                CategoryDTO::class
            )
        )

        val realm = Realm.open(config)
        return RealmDatabase(realm)
    }
}