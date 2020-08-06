package com.lamine.realestatemanager.controllers.viewModel

import android.content.Context
import com.lamine.realestatemanager.database.RealEstateDatabase
import com.lamine.realestatemanager.repositories.PropertyDataRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *Created by Lamine MESSACI on 05/08/2020.
 */

object DataInjection {

    object Injection {

        // DataSource provider
        private fun providePropertyDataSource(context: Context): PropertyDataRepository {
            val database: RealEstateDatabase = RealEstateDatabase.getInstance(context)
            return PropertyDataRepository(database.propertyDao())
        }

        // Executor
        private fun provideExecutor(): Executor {
            return Executors.newSingleThreadExecutor()
        }

        // Provider ViewModelFactory
        fun provideViewModelFactory(context: Context): ViewModelFactory {
            val dataSourceItem: PropertyDataRepository = providePropertyDataSource(context)

            val executor: Executor = provideExecutor()
            return ViewModelFactory(dataSourceItem, executor)
        }
    }
}