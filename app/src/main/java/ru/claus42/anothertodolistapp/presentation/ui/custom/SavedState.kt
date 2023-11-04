package ru.claus42.anothertodolistapp.presentation.ui.custom

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.AbsSavedState

class SavedState : AbsSavedState {
    var data = Bundle()

    constructor(superState: Parcelable?) : super(superState)

    constructor(source: Parcel, loader: ClassLoader?) : super(source, loader) {
        data = source.readBundle(loader) ?: Bundle()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeBundle(data)
    }

    companion object CREATOR : Parcelable.Creator<SavedState> {
        override fun createFromParcel(source: Parcel): SavedState {
            return SavedState(source, null)
        }

        override fun newArray(size: Int): Array<SavedState?> {
            return arrayOfNulls(size)
        }
    }
}