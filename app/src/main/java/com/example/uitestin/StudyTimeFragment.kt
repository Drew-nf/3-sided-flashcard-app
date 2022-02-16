package com.example.uitestin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.uitestin.data.Card
import com.example.uitestin.databinding.FragmentStudyTimeBinding
import com.example.uitestin.viewModels.CardViewModel
import com.example.uitestin.viewModels.CardViewModelFactory

class StudyTimeFragment: Fragment() {

    private val viewModel: CardViewModel by activityViewModels {
        CardViewModelFactory(
            (activity?.application as CardApplication).database.cardDao()
        )
    }
    lateinit var card: Card

    private var _binding: FragmentStudyTimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun setCharacter(card:Card){
        binding.apply {
            textViewCardValue.setText(card.character,TextView.BufferType.SPANNABLE)
        }
    }
    private fun setPinyin(card:Card){
        binding.apply {
            textViewCardValue.setText(card.pinyin,TextView.BufferType.SPANNABLE)
        }
    }
    private fun setTranslation(card:Card){
        binding.apply {
            textViewCardValue.setText(card.translation,TextView.BufferType.SPANNABLE)
        }
    }
    private fun newCard(){
        viewModel.retrieveRandomCard().observe(this.viewLifecycleOwner){selectedCard->
            card = selectedCard
            switchButton(1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStudyTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retrieveRandomCard().observe(this.viewLifecycleOwner){selectedCard->
            card = selectedCard
            setCharacter(card)
        }
        var value = 1
        binding.buttonLeftSt.setOnClickListener {
            value = when(value){
                1 -> 2
                2 -> 1
                else -> 1
            }
            switchButton(value)
        }
        binding.buttonRightSt.setOnClickListener {
            value = when(value){
                1 -> 3
                2 -> 3
                else -> 2
            }
            switchButton(value)
        }
        binding.buttonNewCard.setOnClickListener {
            newCard()
            value = 1
        }
        binding.buttonCancelSt.setOnClickListener {
            val action = StudyTimeFragmentDirections.actionStudyTimeFragmentToCardListFragment()
            view.findNavController().navigate(action)
        }
    }

    fun clickedChar(){
        binding.buttonLeftSt.setText(getString(R.string.pronunciation),TextView.BufferType.SPANNABLE)
        binding.buttonRightSt.setText(getString(R.string.translation),TextView.BufferType.SPANNABLE)
        setCharacter(card)
    }
    fun clickedPin(){
        binding.buttonLeftSt.setText(getString(R.string.vocabulary),TextView.BufferType.SPANNABLE)
        binding.buttonRightSt.setText(getString(R.string.translation),TextView.BufferType.SPANNABLE)
        setPinyin(card)
    }
    fun clickedEng(){
        binding.buttonLeftSt.setText(getString(R.string.vocabulary),TextView.BufferType.SPANNABLE)
        binding.buttonRightSt.setText(getString(R.string.pronunciation),TextView.BufferType.SPANNABLE)
        setTranslation(card)
    }

    fun switchButton(int: Int){
        when(int){
            1 -> clickedChar()
            2 -> clickedPin()
            else -> clickedEng()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken,0)
        _binding = null
    }
}