package com.gallery.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_albums")
data class CustomAlbumEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val coverUri: String?,
    val createdDate: Long = System.currentTimeMillis()
)
