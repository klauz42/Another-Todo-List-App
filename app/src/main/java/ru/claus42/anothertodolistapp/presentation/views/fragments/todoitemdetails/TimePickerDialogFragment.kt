package ru.claus42.anothertodolistapp.presentation.views.fragments.todoitemdetails

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment


private const val HOUR = "hour"
private const val MINUTE = "minute"

class TimePickerDialogFragment : DialogFragment() {

    private var listener: TimePickerDialog.OnTimeSetListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? TimePickerDialog.OnTimeSetListener
        if (listener == null) {
            for (fragment in parentFragmentManager.fragments) {
                if (fragment is TimePickerDialog.OnTimeSetListener)
                    listener = fragment
            }
        }
        if (listener == null) {
            throw ClassCastException("$context must implement OnTimeSetListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val hourOfDay = arguments?.getInt(HOUR)!!
            val minute = arguments?.getInt(MINUTE)!!

            return TimePickerDialog(it, listener, hourOfDay, minute, DateFormat.is24HourFormat(it))
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    companion object {
        fun newInstance(hourOfDay: Int, minute: Int): TimePickerDialogFragment {
            val args = Bundle()
            args.putInt(HOUR, hourOfDay)
            args.putInt(MINUTE, minute)

            val fragment = TimePickerDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}