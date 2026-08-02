package com.gallery.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey
    val mediaUri: String,
    val mediaId: Long,
    val addedDate: Long = System.currentTimeMillis()
)
