package ru.claus42.anothertodolistapp.presentation.todoitemdetails.fragments

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment


class DatePickerDialogFragment : DialogFragment() {

    private var listener: OnDateSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnDateSetListener
        if (listener == null) {
            for (fragment in parentFragmentManager.fragments) {
                if (fragment is OnDateSetListener)
                    listener = fragment
            }
        }
        if (listener == null) {
            throw ClassCastException("$context must implement OnDateSetListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val year = arguments?.getInt(YEAR)!!
            val month = arguments?.getInt(MONTH)!!
            val dayOfMonth = arguments?.getInt(DAY)!!

            return DatePickerDialog((it as Context), listener, year, month - 1, dayOfMonth)
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    companion object {
        private const val YEAR = "year"
        private const val MONTH = "month"
        private const val DAY = "day"

        fun newInstance(year: Int, month: Int, dayOfMonth: Int): DatePickerDialogFragment {
            val args = Bundle()
            args.putInt(YEAR, year)
            args.putInt(MONTH, month)
            args.putInt(DAY, dayOfMonth)

            val fragment = DatePickerDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}