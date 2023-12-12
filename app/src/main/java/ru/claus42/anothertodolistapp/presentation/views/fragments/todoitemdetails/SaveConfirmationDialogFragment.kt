package ru.claus42.anothertodolistapp.presentation.views.fragments.todoitemdetails

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.claus42.anothertodolistapp.R

class SaveConfirmationDialogFragment() : DialogFragment() {
    interface SaveConfirmationListener {
        fun onSaveConfirmed()
        fun onExitWithoutSaving()
        fun onSaveCancel()
    }

    private var listener: SaveConfirmationListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? SaveConfirmationListener
        if (listener == null) {
            for (fragment in parentFragmentManager.fragments) {
                if (fragment is SaveConfirmationListener)
                    listener = fragment
            }
        }
        if (listener == null) {
            throw ClassCastException("$context must implement SaveConfirmationListener")
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
                .setTitle(getString(R.string.save_changes_confirmation_title))
                .setMessage(getString(R.string.do_you_want_to_save_changes))
                .setPositiveButton(getString(R.string.yes_save)) { _, _ ->
                    listener?.onSaveConfirmed()
                }
                .setNegativeButton(getString(R.string.no_save)) { _, _ ->
                    listener?.onExitWithoutSaving()
                }
                .setNeutralButton(getString(R.string.cancel_save)) { _, _ ->
                    listener?.onSaveCancel()
                }
                .setCancelable(true)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}