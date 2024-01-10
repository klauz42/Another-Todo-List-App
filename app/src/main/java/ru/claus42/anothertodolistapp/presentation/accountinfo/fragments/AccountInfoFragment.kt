package ru.claus42.anothertodolistapp.presentation.accountinfo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.claus42.anothertodolistapp.databinding.FragmentAccountInfoBinding

class AccountInfoFragment : Fragment() {
    private var _binding: FragmentAccountInfoBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)

        return binding.root
    }
}