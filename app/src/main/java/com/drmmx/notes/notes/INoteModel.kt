package com.drmmx.notes.notes

import com.drmmx.notes.models.Note

typealias SuccessCallback = (Boolean) -> Unit

interface INoteModel {

    fun addNote(note: Note, callback: SuccessCallback)
    fun updateNote(note: Note, callback: SuccessCallback)
    fun deleteNote(note: Note, callback: SuccessCallback)
    fun retrieveNotes(callback: (List<Note>?) -> Unit)

}