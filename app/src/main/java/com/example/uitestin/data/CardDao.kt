package com.example.uitestin.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM card ORDER BY translation ASC")
    fun getCards(): Flow<List<Card>>

    @Query("SELECT * FROM card WHERE id = :id")
    fun getCard(id: Int): Flow<Card>

    @Query("SELECT * FROM card ORDER BY Random() LIMIT 1")
    fun getRandomCard(): Flow<Card>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(card: Card)

    @Update
    suspend fun update(card: Card)

    @Delete
    suspend fun delete(card: Card)
}