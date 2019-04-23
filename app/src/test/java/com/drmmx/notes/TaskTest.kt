package com.drmmx.notes

import com.drmmx.notes.models.Task
import com.drmmx.notes.models.Todo
import org.junit.Before
import org.junit.Test

class TaskTest {

    lateinit var testTask: Task

    @Before
    fun setupBeforeTest() {
        testTask = getFakeTask()
    }

    @Test
    fun taskIsIncompleteOnInit() {
        assert(!testTask.isComplete())
    }

    @Test
    fun taskIsCompleteAfterAllTodosChecked() {
        testTask.todos.forEach {
            it.isComplete = true
        }

        assert(testTask.isComplete())
    }

    fun getFakeTask(): Task = Task(
        title = "Testing Task",
        todos = getFakeTodos()
    )
    fun getFakeTodos(): MutableList<Todo> = mutableListOf(
        Todo(description = "one", isComplete = true),
        Todo(description = "two"),
        Todo(description = "three")
    )
}