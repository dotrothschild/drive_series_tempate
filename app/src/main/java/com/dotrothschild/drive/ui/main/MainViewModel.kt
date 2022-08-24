package com.dotrothschild.drive.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dotrothschild.drive.database.DriveDao
import com.dotrothschild.drive.database.models.Place
import kotlinx.coroutines.flow.Flow

class MainViewModel(private val driveDao: DriveDao): ViewModel() {
    fun allPlaces(): Flow<List<Place>> = driveDao.getAllPlaces()
}
class MainViewModelFactory(
    private val driveDao: DriveDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(driveDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
