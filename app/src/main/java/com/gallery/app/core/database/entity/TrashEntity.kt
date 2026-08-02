package com.gallery.app.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trash")
data class TrashEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val mediaUri: String,
    val originalPath: String,
    val displayName: String,
    val sizeBytes: Long,
    val mimeType: String,
    val trashedTimestamp: Long,
    val expiryTimestamp: Long
)
