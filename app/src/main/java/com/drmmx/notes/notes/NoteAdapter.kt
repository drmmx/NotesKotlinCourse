package com.drmmx.notes.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drmmx.notes.R
import com.drmmx.notes.customviews.NoteView
import com.drmmx.notes.foundations.BaseRecyclerAdapter
import com.drmmx.notes.models.Note
import com.drmmx.notes.navigation.NavigationActivity
import kotlinx.android.synthetic.main.view_add_button.view.*

class NoteAdapter(
    noteList: MutableList<Note> = mutableListOf(),
    val touchActionDelegate: NotesListFragment.TouchActionDelegate,
    val dataActionDelegate: NoteListViewContract
) :
    BaseRecyclerAdapter<Note>(noteList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_INFO) {
            NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))
        } else {
            AddButtonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_add_button, parent, false))
        }


    inner class NoteViewHolder(itemView: View) : BaseViewHolder<Note>(itemView) {

        override fun onBind(data: Note, listIndex: Int) {
            (itemView as NoteView).initView(data) {
                dataActionDelegate.deleteNote(masterList[listIndex])
            }
        }
    }

    inner class AddButtonViewHolder(itemView: View) : BaseRecyclerAdapter.AddButtonViewHolder(itemView) {
        override fun onBind(data: Unit, listIndex: Int) {
            itemView.buttonText.text = itemView.context.getString(R.string.add_button_note)

            itemView.setOnClickListener {
                touchActionDelegate.onAddButtonClicked(NavigationActivity.FRAGMENT_VALUE_NOTE)
            }
        }
    }
}