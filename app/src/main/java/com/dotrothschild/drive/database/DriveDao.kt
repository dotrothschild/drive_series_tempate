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

    @Query("SELECT * FROM feedback WHERE place_id = :placeId ORDER BY user_name ASC")
    fun getFeedbackByPlace(placeId: Int): Flow<List<Feedback>>

    // Works, but need to create the datatype to store results
   // @Query(" SELECT user_name, comment, place_id, Place.title FROM feedback INNER JOIN Place ON Feedback.place_id = Place.id WHERE place_id = :placeId ORDER BY user_name ASC")
   // fun getFeedbackAndPlaceName(placeId: Int): Flow<List<Feedback>>
}
