package com.drmmx.notes.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drmmx.notes.foundations.ApplicationScope
import com.drmmx.notes.models.Note
import toothpick.Toothpick
import javax.inject.Inject

class NoteViewModel : ViewModel(), NoteListViewContract {

    @Inject
    lateinit var model: INoteModel

    private val _noteListLiveDate: MutableLiveData<List<Note>> = MutableLiveData()

    val noteListLiveData: LiveData<List<Note>> = _noteListLiveDate
    init {
        Toothpick.inject(this, ApplicationScope.scope)
        loadData()
    }

    fun loadData() {
        model.retrieveNotes {nullableList ->
            nullableList?.let {
                _noteListLiveDate.postValue(it)
            }
        }
    }

    override fun deleteNote(note: Note) {
        model.deleteNote(note) {
            if (it) {
                loadData()
            }
        }
    }

}