package ru.claus42.anothertodolistapp.presentation.views.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import ru.claus42.anothertodolistapp.R

class DeleteConfirmationDialogFragment() : DialogFragment() {
    interface DeleteConfirmationListener {
        fun onDeleteConfirmed()
        fun onDeleteCancel()
    }

    private var listener: DeleteConfirmationListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? DeleteConfirmationListener
        if (listener == null) {
            for (fragment in parentFragmentManager.fragments) {
                if (fragment is DeleteConfirmationListener)
                    listener = fragment
            }
        }
        if (listener == null) {
            throw ClassCastException("$context must implement DeleteConfirmationListener")
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
                .setTitle(getString(R.string.deleting_confirmation_title))
                .setMessage(getString(R.string.do_you_want_to_delete_todo))
                .setPositiveButton(getString(R.string.yes_save)) { _, _ ->
                    listener?.onDeleteConfirmed()
                }
                .setNeutralButton(getString(R.string.cancel_save)) { _, _ ->
                    listener?.onDeleteCancel()
                }
                .setCancelable(true)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}