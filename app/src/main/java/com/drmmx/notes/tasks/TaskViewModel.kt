package com.drmmx.notes.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drmmx.notes.foundations.ApplicationScope
import com.drmmx.notes.models.Task
import toothpick.Toothpick
import javax.inject.Inject

class TaskViewModel : ViewModel(), TaskListViewContract {

    @Inject
    lateinit var model: ITaskModel

    private val _taskListLiveDate: MutableLiveData<MutableList<Task>> = MutableLiveData()

    val taskListLiveData: LiveData<MutableList<Task>> = _taskListLiveDate

    init {
        Toothpick.inject(this, ApplicationScope.scope)
        loadData()
    }

    fun loadData() {
        model.retrieveTasks { nullableList ->
            nullableList?.let {
                _taskListLiveDate.postValue(it.toMutableList())
            }
        }
    }

    override fun onTodoUpdated(taskIndex: Int, todoIndex: Int, isComplete: Boolean) {

        _taskListLiveDate.value?.let {
            val todo = it[taskIndex].todos[todoIndex]
            todo.apply {
                this.isComplete = isComplete
                this.taskId = it[taskIndex].uid
            }
            model.updateTodo(todo) {
                loadData()
            }
        }
    }

    override fun onTaskDeleted(taskIndex: Int) {
        _taskListLiveDate.value?.let {
            model.deleteTask(it[taskIndex]) {
                loadData()
            }
        }
    }
}
