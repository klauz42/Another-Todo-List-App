package ru.claus42.anothertodolistapp.presentation.binding_adapters

import android.content.res.ColorStateList
import android.graphics.Paint
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.domain.models.entities.Importance

@BindingAdapter("importanceCheckboxTint")
fun setImportanceCheckboxTint(view: CheckBox, importance: Importance) {
    val context = view.context
    val basicColor = ContextCompat.getColor(context, R.color.basic_priority_check)
    val importantColor = ContextCompat.getColor(context, R.color.important_priority_check)

    view.buttonTintList = when (importance) {
        Importance.LOW, Importance.BASIC -> ColorStateList.valueOf(basicColor)
        Importance.IMPORTANT -> ColorStateList.valueOf(importantColor)
    }
}

@BindingAdapter("importanceDescriptionDrawable")
fun setImportanceDescriptionDrawable(textView: TextView, importance: Importance) {
    val context = textView.context
    val drawable = when (importance) {
        Importance.LOW -> ContextCompat.getDrawable(context, R.drawable.arrow_downward)
        Importance.BASIC -> null
        Importance.IMPORTANT -> ContextCompat.getDrawable(context, R.drawable.priority_high)
    }
    if (ViewCompat.getLayoutDirection(textView) == ViewCompat.LAYOUT_DIRECTION_LTR) {
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
    } else {
        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }
}

@BindingAdapter("doneItem")
fun setItemTextViewDone(textView: TextView, done: Boolean) {
    val context = textView.context
    if (done) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        textView.setTextColor(ContextCompat.getColor(context, R.color.label_tertiary))
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        textView.setTextColor(ContextCompat.getColor(context, R.color.label_primary))
    }
}

//TODO: add this adapter to fragment_todo_item_details using ViewModel
@BindingAdapter("doneCount")
fun setDoneCount(textView: TextView, count: Int) {
    textView.text = textView.context.getString(R.string.done_format, count)
}