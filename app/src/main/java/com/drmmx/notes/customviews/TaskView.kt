package com.drmmx.notes.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.drmmx.notes.R
import com.drmmx.notes.models.Task
import kotlinx.android.synthetic.main.item_task.view.*

class TaskView @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 1
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var task: Task

    fun initView(task: Task, todoCheckedCallback: (Int, Boolean) -> Unit, deleteCallback: () -> Unit) {
        resetChildView()
        this.task = task
        initTaskLine(deleteCallback)
        addChildViews(todoCheckedCallback)
    }

    private fun resetChildView() {
        todoContainer.removeAllViewsInLayout()
    }

    private fun initTaskLine(deleteCallback: () -> Unit) {
        titleView.text = task.title

        imageButton.setOnClickListener {
            deleteCallback.invoke()
        }
    }

    private fun addChildViews(todoCheckedCallback: (Int, Boolean) -> Unit) {
        task.todos.forEachIndexed { todoIndex, todo ->
            val todoView = (LayoutInflater.from(context)
                .inflate(R.layout.view_todo, todoContainer, false) as TodoView).apply {
                initView(todo) { isChecked ->

                    todoCheckedCallback.invoke(todoIndex, isChecked)

                    if (task.isComplete()) {
                        this@TaskView.titleView.setStrikeThrough()
                    } else {
                        this@TaskView.titleView.removeSrtikeThrough()
                    }
                }
            }
            todoContainer.addView(todoView)
        }
    }

    private fun isTaskComplete(): Boolean = task.todos.filter {
        !it.isComplete
    }.isEmpty()
}