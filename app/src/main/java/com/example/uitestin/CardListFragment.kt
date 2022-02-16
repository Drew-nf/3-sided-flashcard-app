package com.example.uitestin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.uitestin.adapter.CardListAdapter
import com.example.uitestin.databinding.FragmentCardListBinding
import com.example.uitestin.viewModels.CardViewModel
import com.example.uitestin.viewModels.CardViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uitestin.databinding.FragmentCardDetailBinding


class CardListFragment: Fragment() {

    private val viewModel: CardViewModel by activityViewModels {
        CardViewModelFactory(
            (activity?.application as CardApplication).database.cardDao()
        )
    }

    private var _binding: FragmentCardListBinding? = null
    private val binding get() = _binding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /*fun returnButton(){
        findNavController().navigate(R.id.action_cardListFragment_to_mainMenuFragment)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CardListAdapter{
            val action = CardListFragmentDirections.actionCardListFragmentToCardDetailFragment("Edit Item",it.id)
            this.findNavController().navigate(action)
        }
        binding.recyclerViewCardList.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerViewCardList.adapter = adapter

        viewModel.allCards.observe(this.viewLifecycleOwner){ card ->
            card.let{
                adapter.submitList(it)
            }
        }

        val addBtn = view.findViewById(R.id.f_a_button_add_card) as FloatingActionButton
        addBtn.setOnClickListener {
            val action = CardListFragmentDirections.actionCardListFragmentToCardDetailFragment("Add Card")
            view.findNavController().navigate(action)
        }
        binding.buttonStudyTime.setOnClickListener {
            if(viewModel.allCards.value.isNullOrEmpty()){
                Toast.makeText(activity,"No Cards Yet!!", Toast.LENGTH_LONG).show()
            }else{
                val action = CardListFragmentDirections.actionCardListFragmentToStudyTimeFragment()
                view.findNavController().navigate(action)
            }
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