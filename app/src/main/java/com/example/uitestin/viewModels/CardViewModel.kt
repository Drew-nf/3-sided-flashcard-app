package com.example.uitestin.viewModels

import androidx.lifecycle.*
import androidx.lifecycle.viewModelScope
import com.example.uitestin.data.Card
import com.example.uitestin.data.CardDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException


class CardViewModel(private val cardDao: CardDao) : ViewModel(){

    val allCards: LiveData<List<Card>> = cardDao.getCards().asLiveData()

    fun updateCard(
        cardId: Int,
        cardCharacter: String,
        cardPinyin: String,
        cardTranslation: String
    ){
        val updatedCard = getUpdatedCardEntry(cardId,cardCharacter,cardPinyin,cardTranslation)
        updateCard(updatedCard)
    }

    fun addNewCard(cardCharacter: String, cardPinyin: String, cardTranslation: String){
        val newCard = getNewCardEntry(cardCharacter,cardPinyin,cardTranslation)
        insertCard(newCard)
    }

    private fun insertCard(card: Card){
        viewModelScope.launch {
            cardDao.insert(card)
        }
    }

    fun retrieveRandomCard(): LiveData<Card>{
        return cardDao.getRandomCard().asLiveData()
    }

    fun deleteCard(card: Card){
        viewModelScope.launch {
            cardDao.delete(card)
        }
    }

    fun retrieveCard(id: Int): LiveData<Card>{
        return cardDao.getCard(id).asLiveData()
    }

    private fun updateCard(card: Card){
        viewModelScope.launch {
            cardDao.update(card)
        }
    }

    private fun getNewCardEntry(cardCharacter: String,cardPinyin: String,cardTranslation: String):Card{
        return Card(
            character = cardCharacter,
            pinyin = cardPinyin,
            translation = cardTranslation
        )
    }

    fun isEntryValid(character: String, pinyin: String, translation: String): Boolean{
        if(character.isBlank()||pinyin.isBlank()||translation.isBlank()){
            return false
        }
        return true
    }

    private fun getUpdatedCardEntry(
        cardId: Int,
        cardCharacter: String,
        cardPinyin: String,
        cardTranslation: String
    ): Card{
        return Card(
            id = cardId,
            character = cardCharacter,
            pinyin = cardPinyin,
            translation = cardTranslation
        )
    }
}


class CardViewModelFactory(private val itemDao: CardDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CardViewModel(itemDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}