package com.dotrothschild.drive.database.models

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    // TODO: WANT AN INDEX ON x,y so need to fix mock data
    tableName = "Place" // , indices = [Index(value = ["x", "y"], unique = true)]
    // risk that no 2 places same, not currently verified in db
)
@Parcelize
data class Place(
    @PrimaryKey val id: Int,
    @NonNull @ColumnInfo(name = "title") val title: String,
    @NonNull @ColumnInfo(name = "state") val state: String,
    @NonNull @ColumnInfo(name = "country") val country: String,
    @NonNull @ColumnInfo(name = "x") val x: Double,
    @NonNull @ColumnInfo(name = "y") val y: Double,
    @ColumnInfo(name = "place_number") val place_number: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "address") val address: String?,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "region") val region: String?,
    @ColumnInfo(name = "keywords") val keywords: String?
) : Parcelable

/*
  @SerializedName("title") val title : String,
  @SerializedName("state") val state : String,
  @SerializedName("country") val country : String,
  @SerializedName("x") val x: Double,
  @SerializedName("y") val y: Double,
  @SerializedName("place_number") val place_number : String?,
  @SerializedName("description") val description : String?,
  @SerializedName("address") val address: String?,
  @SerializedName("city") val city : String?,
  @SerializedName("region") val region : String?,
  @SerializedName("keywords") val keywords: String?,

   */