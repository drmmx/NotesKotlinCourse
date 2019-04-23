package com.drmmx.notes.notes

import com.drmmx.notes.application.NoteApplication
import com.drmmx.notes.database.RoomDatabaseClient
import com.drmmx.notes.models.Note
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

const val TIMEOUT_DURATION_MILLIS = 3000L

class NoteLocalModel @Inject constructor() : INoteModel {

    private var databaseClient = RoomDatabaseClient.getInstance(NoteApplication.instance.applicationContext)

    private fun performOperationWIthTimeout(function: () -> Unit, callback: SuccessCallback) {
        GlobalScope.launch {
            val job = async {
                try {
                    withTimeout(TIMEOUT_DURATION_MILLIS) {
                        function.invoke()
                    }
                } catch (e: Exception) {
                    callback.invoke(false)
                }
            }
            job.await()
            callback.invoke(true)
        }
    }

    override fun addNote(note: Note, callback: SuccessCallback) {
        performOperationWIthTimeout({ databaseClient.noteDAO().addNote(note) }, callback)
    }

    override fun updateNote(note: Note, callback: SuccessCallback) {
        performOperationWIthTimeout({ databaseClient.noteDAO().updateNote(note) }, callback)
    }

    override fun deleteNote(note: Note, callback: SuccessCallback) {
        performOperationWIthTimeout({ databaseClient.noteDAO().deleteNote(note) }, callback)
    }

    override fun retrieveNotes(callback: (List<Note>?) -> Unit) {
        GlobalScope.launch {
            val job = async {
                withTimeoutOrNull(TIMEOUT_DURATION_MILLIS) {
                    databaseClient.noteDAO().retrieveNotes()
                }
            }
            callback.invoke(job.await())
        }
    }
}