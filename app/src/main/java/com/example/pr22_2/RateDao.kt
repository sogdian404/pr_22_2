package com.example.pr22_2

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RateDao {
    @Query("SELECT * FROM rates ORDER BY timestamp DESC")
    fun getAll(): LiveData<List<RateEntity>>

    @Insert
    fun insert(rate: RateEntity): Long

    @Update
    fun update(rate: RateEntity)

    @Delete
    fun delete(rate: RateEntity):Int
}
