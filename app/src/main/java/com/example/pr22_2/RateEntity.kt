package com.example.pr22_2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rates")
data class RateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var pair: String,
    var rate: String,
    var timestamp: Long = System.currentTimeMillis()
)
