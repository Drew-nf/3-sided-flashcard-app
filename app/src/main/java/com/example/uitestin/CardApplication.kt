package com.example.uitestin

import android.app.Application
import com.example.uitestin.data.CardRoomDatabase

class CardApplication : Application() {

    val database: CardRoomDatabase by lazy { CardRoomDatabase.getDatabase(this) }
}