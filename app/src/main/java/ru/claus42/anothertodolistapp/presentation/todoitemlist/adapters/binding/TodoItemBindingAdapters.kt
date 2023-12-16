package ru.claus42.anothertodolistapp.presentation.todoitemlist.adapters.binding

import android.content.res.ColorStateList
import android.graphics.Paint
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.BindingAdapter
import ru.claus42.anothertodolistapp.R
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority


@BindingAdapter("importanceCheckboxTint")
fun setPriorityCheckboxTint(view: CheckBox, itemPriority: ItemPriority) {
    val context = view.context
    val basicColor = ContextCompat.getColor(context, R.color.basic_priority_check)
    val importantColor = ContextCompat.getColor(context, R.color.important_priority_check)

    view.buttonTintList = when (itemPriority) {
        ItemPriority.LOW, ItemPriority.BASIC -> ColorStateList.valueOf(basicColor)
        ItemPriority.IMPORTANT -> ColorStateList.valueOf(importantColor)
    }
}

@BindingAdapter("importanceDescriptionDrawable")
fun setPriorityDescriptionDrawable(textView: TextView, itemPriority: ItemPriority) {
    val context = textView.context
    val drawable = when (itemPriority) {
        ItemPriority.LOW -> ContextCompat.getDrawable(context, R.drawable.arrow_downward)
        ItemPriority.BASIC -> null
        ItemPriority.IMPORTANT -> ContextCompat.getDrawable(context, R.drawable.priority_high)
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
