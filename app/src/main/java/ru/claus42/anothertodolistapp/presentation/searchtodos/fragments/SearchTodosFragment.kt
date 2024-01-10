package ru.claus42.anothertodolistapp.presentation.searchtodos.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.claus42.anothertodolistapp.databinding.FragmentSearchTodosBinding

class SearchTodosFragment : Fragment() {
    private var _binding: FragmentSearchTodosBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchTodosBinding.inflate(inflater, container, false)

        return binding.root
    }
}