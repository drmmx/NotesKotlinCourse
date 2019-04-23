package com.drmmx.notes.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.drmmx.notes.models.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NoteView @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : ConstraintLayout(context, attrs, defStyleAttr) {

    fun initView(note: Note, deleteButtonClickedCallback: () -> Unit) {
        descriptionView.text = note.description
        imageButton.setOnClickListener {
            deleteButtonClickedCallback.invoke()
        }
    }

}