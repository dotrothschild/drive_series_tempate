package com.dotrothschild.drive.ui.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dotrothschild.drive.database.DriveDao
import com.dotrothschild.drive.database.models.Feedback
import kotlinx.coroutines.flow.Flow


class FeedbackViewModel(private val driveDao: DriveDao): ViewModel() {
    fun allFeedback(): Flow<List<Feedback>> = driveDao.getAllFeedback()

    class FeedbackViewModelFactory(
        private val driveDao: DriveDao
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedbackViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FeedbackViewModel(driveDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}