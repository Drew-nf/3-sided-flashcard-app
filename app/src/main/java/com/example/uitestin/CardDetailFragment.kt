package com.example.uitestin

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.uitestin.data.Card
import com.example.uitestin.data.CardRoomDatabase
import com.example.uitestin.databinding.FragmentCardDetailBinding
import com.example.uitestin.viewModels.CardViewModel
import com.example.uitestin.viewModels.CardViewModelFactory

class CardDetailFragment:Fragment() {


    private val viewModel: CardViewModel by activityViewModels {
        CardViewModelFactory(
            (activity?.application as CardApplication).database
                .cardDao()
        )
    }
    private val navigationArgs: CardDetailFragmentArgs by navArgs()
    lateinit var card: Card

    private var _binding: FragmentCardDetailBinding? = null
    private val binding get() = _binding!!

    private fun bind(card: Card){
        binding.apply {
            textInputCharacterDetail.setText(card.character, TextView.BufferType.SPANNABLE)
            textInputPinyinDetail.setText(card.pinyin,TextView.BufferType.SPANNABLE)
            textInputEnglishDetail.setText(card.translation, TextView.BufferType.SPANNABLE)
            buttonSaveDetail.setOnClickListener { updateCard() }
            buttonDeleteDetail.setOnClickListener { deleteCard() }
        }
    }

    private fun deleteCard(){
        viewModel.deleteCard(card)
        val action = CardDetailFragmentDirections.actionCardDetailFragmentToCardListFragment()
        findNavController().navigate(action)
    }

    private fun updateCard(){
        if(isEntryValid()) {
            viewModel.updateCard(
                this.navigationArgs.cardId,
                this.binding.textInputCharacterDetail.text.toString(),
                this.binding.textInputPinyinDetail.text.toString(),
                this.binding.textInputEnglishDetail.text.toString()
            )
            val action = CardDetailFragmentDirections.actionCardDetailFragmentToCardListFragment()
            findNavController().navigate(action)
        }

    }

    private fun isEntryValid(): Boolean{
        return viewModel.isEntryValid(
            binding.textInputCharacterDetail.text.toString(),
            binding.textInputPinyinDetail.text.toString(),
            binding.textInputEnglishDetail.text.toString()
        )
    }

    private fun addNewCard(){
        if(isEntryValid()){
            viewModel.addNewCard(
                binding.textInputCharacterDetail.text.toString(),
                binding.textInputPinyinDetail.text.toString(),
                binding.textInputEnglishDetail.text.toString()
            )
            val action = CardDetailFragmentDirections.actionCardDetailFragmentToCardListFragment()
            findNavController().navigate(action)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.cardId
        binding.buttonCancelDetail.setOnClickListener {
            val action = CardDetailFragmentDirections.actionCardDetailFragmentToCardListFragment()
            view.findNavController().navigate(action)
        }
        if(id > 0){
            (activity as AppCompatActivity).supportActionBar?.title = "Edit Flash Card"
            viewModel.retrieveCard(id).observe(this.viewLifecycleOwner){selectedCard->
                card = selectedCard
                bind(card)
            }
        } else {
            binding.buttonDeleteDetail.alpha = 0.0f
            binding.buttonSaveDetail.setOnClickListener {
                addNewCard()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
             InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken,0)
        _binding = null
    }
}