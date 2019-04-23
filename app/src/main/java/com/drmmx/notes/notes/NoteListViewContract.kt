package com.drmmx.notes.notes

import com.drmmx.notes.models.Note

interface NoteListViewContract {
    fun deleteNote(note: Note)
}