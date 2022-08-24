package com.dotrothschild.drive.database.models

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "Feedback"//, indices = [Index(value = ["placeId", "userName"], unique = true)]
    // risk that no 2 comments on same place by same user, not currently verified in db
)
@Parcelize
data class Feedback(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "place_id") val place_id: Int,
    @NonNull @ColumnInfo(name = "user_name") val user_name: String,
    @NonNull @ColumnInfo(name = "comment") val comment: String
  //  @SerializedName("place_id") val placeId : Int,
  //  @SerializedName("user_name") val userName : String,
  //  @SerializedName("comment") val comment : String
) : Parcelable
