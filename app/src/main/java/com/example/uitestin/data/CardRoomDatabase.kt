package com.example.uitestin.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Card::class], version = 1, exportSchema = false)
abstract class CardRoomDatabase : RoomDatabase(){

    abstract fun cardDao(): CardDao

    companion object {
        @Volatile
        private var INSTANCE: CardRoomDatabase? = null

        fun getDatabase(context: Context): CardRoomDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CardRoomDatabase::class.java,
                    "card_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}