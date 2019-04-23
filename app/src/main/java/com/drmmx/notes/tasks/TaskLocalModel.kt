package com.drmmx.notes.tasks

import com.drmmx.notes.application.NoteApplication
import com.drmmx.notes.database.RoomDatabaseClient
import com.drmmx.notes.models.Task
import com.drmmx.notes.models.Todo
import com.drmmx.notes.notes.TIMEOUT_DURATION_MILLIS
import kotlinx.coroutines.*
import java.lang.Exception
import javax.inject.Inject

class TaskLocalModel @Inject constructor(): ITaskModel {
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

    override fun addTask(task: Task, callback: SuccessCallback) {
        GlobalScope.launch {
            val masterJob = async {
                try {
                    databaseClient.taskDAO().addTask(task)
                } catch (e: Exception) {
                    callback.invoke(false)
                }
                addTodosJob(task)
            }
            masterJob.await()
            callback.invoke(true)
        }
    }

    override fun updateTask(task: Task, callback: SuccessCallback) {
        performOperationWIthTimeout({databaseClient.taskDAO().updateTask(task)}, callback)
    }

    override fun updateTodo(todo: Todo, callback: SuccessCallback) {
        performOperationWIthTimeout({databaseClient.taskDAO().updateTodo(todo)}, callback)
    }

    override fun deleteTask(task: Task, callback: SuccessCallback) {
        performOperationWIthTimeout({databaseClient.taskDAO().deleteTask(task)}, callback)
    }

    private fun addTodosJob(task: Task): Job = GlobalScope.async {
        task.todos.forEach {
            databaseClient.taskDAO().addTodo(it)
        }
    }

    override fun retrieveTasks(callback: (List<Task>?) -> Unit) {
        GlobalScope.launch {
            val job = async {
                withTimeoutOrNull(TIMEOUT_DURATION_MILLIS) {
                    databaseClient.taskDAO().retrieveTask()
                }
            }
            callback.invoke(job.await())
        }
    }
}