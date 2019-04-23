package com.drmmx.notes.database

import androidx.room.*
import com.drmmx.notes.models.Task
import com.drmmx.notes.models.TaskEntity
import com.drmmx.notes.models.Todo

@Dao
interface TaskDAO {

    @Insert
    fun addTask(taskEntity: TaskEntity)

    @Insert
    fun addTodo(todo: Todo)

    @Update
    fun updateTask(taskEntity: TaskEntity)

    @Update
    fun updateTodo(todo: Todo)

    @Delete
    fun deleteTask(taskEntity: TaskEntity)

    @Query("SELECT * FROM tasks")
    fun retrieveTask(): MutableList<Task>

}