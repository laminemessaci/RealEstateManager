package com.lamine.realestatemanager.controllers.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lamine.realestatemanager.repositories.PropertyDataRepository
import java.util.concurrent.Executor

/**
 *Created by Lamine MESSACI on 05/08/2020.
 */

/**
 * ViewModelFactory
 */
class ViewModelFactory(
    private val propertyDataRepository: PropertyDataRepository,
    private val executor: Executor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PropertyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PropertyViewModel(
                propertyDataRepository,
                executor
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}