package com.example.uitestin.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "character")
    val character: String,
    @ColumnInfo(name = "pinyin")
    val pinyin: String,
    @ColumnInfo(name = "translation")
    val translation: String
)