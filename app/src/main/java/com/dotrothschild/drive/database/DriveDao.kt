package com.dotrothschild.drive.database

import androidx.room.Dao
import androidx.room.Query
import com.dotrothschild.drive.database.models.Feedback
import com.dotrothschild.drive.database.models.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface DriveDao {
    @Query("SELECT * FROM place ORDER BY title ASC")
    fun getAllPlaces(): Flow<List<Place>>
    @Query("SELECT * FROM feedback ORDER BY user_name ASC")
    fun getAllFeedback(): Flow<List<Feedback>>
}
