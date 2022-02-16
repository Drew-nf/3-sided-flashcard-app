package com.example.uitestin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.uitestin.data.Card
import com.example.uitestin.databinding.ListCardBinding

class CardListAdapter(private val onItemClicked: (Card)->Unit):
    ListAdapter<Card, CardListAdapter.CardViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder{
        return CardViewHolder(
            ListCardBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener{
            onItemClicked(current)
        }
        holder.bind(current)
    }

    class CardViewHolder(private var binding: ListCardBinding):
            RecyclerView.ViewHolder(binding.root){
                fun bind(card: Card){
                    binding.recyclerCharacter.text = card.character
                    binding.recyclerPinyin.text = card.pinyin
                    binding.recyclerTranslation.text = card.translation
                }
            }

    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Card>(){
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean {
                return oldItem.character == newItem.character
            }

        }
    }
    }