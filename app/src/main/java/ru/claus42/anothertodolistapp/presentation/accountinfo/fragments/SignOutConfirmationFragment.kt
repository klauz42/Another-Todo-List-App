package ru.claus42.anothertodolistapp.presentation.accountinfo.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ru.claus42.anothertodolistapp.R

class SignOutConfirmationFragment : DialogFragment() {
    interface SignOutConfirmationListener {
        fun onSignOutConfirmed()
        fun onSignOutCancel()
    }

    private var listener: SignOutConfirmationListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? SignOutConfirmationListener
        if (listener == null) {
            for (fragment in parentFragmentManager.fragments) {
                if (fragment is SignOutConfirmationListener)
                    listener = fragment
            }
        }
        if (listener == null) {
            throw ClassCastException("$context must implement SignOutConfirmationListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder
                .setTitle(getString(R.string.sign_out_title))
                .setMessage(getString(R.string.sign_out_message))
                .setPositiveButton(getString(R.string.sign_out_yes)) { _, _ ->
                    listener?.onSignOutConfirmed()
                }
                .setNeutralButton(getString(R.string.sign_out_cancel)) { _, _ ->
                    listener?.onSignOutCancel()
                }
                .setCancelable(true)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}