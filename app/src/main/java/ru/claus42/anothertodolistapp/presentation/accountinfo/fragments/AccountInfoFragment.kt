package ru.claus42.anothertodolistapp.presentation.accountinfo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.claus42.anothertodolistapp.databinding.FragmentAccountInfoBinding
import ru.claus42.anothertodolistapp.di.components.DaggerFragmentComponent
import ru.claus42.anothertodolistapp.di.components.FragmentComponent
import ru.claus42.anothertodolistapp.domain.authentication.SessionManager
import ru.claus42.anothertodolistapp.presentation.MainActivity
import javax.inject.Inject

class AccountInfoFragment :
    Fragment(),
    SignOutConfirmationFragment.SignOutConfirmationListener {

    private lateinit var fragmentComponent: FragmentComponent

    private var _binding: FragmentAccountInfoBinding? = null
    val binding get() = _binding!!

    @Inject
    lateinit var sessionManager: SessionManager

    private val activity: MainActivity by lazy { requireActivity() as MainActivity }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentComponent =
            DaggerFragmentComponent.builder().activityComponent(activity.activityComponent).build()
        fragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountInfoBinding.inflate(inflater, container, false)

        binding.emailInfo.text = sessionManager.getUserEmail()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSignOut.setOnClickListener {
            showSignOutDialog()
        }
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    private fun showSignOutDialog() {
        SignOutConfirmationFragment().show(
            parentFragmentManager,
            DIALOG_SIGN_OUT_CONFIRM
        )
    }

    override fun onSignOutConfirmed() {
        activity.signOut()
    }

    override fun onSignOutCancel() {}

    private companion object {
        const val DIALOG_SIGN_OUT_CONFIRM = "SignOutConfirmation"
    }
}