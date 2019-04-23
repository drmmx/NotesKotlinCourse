package com.drmmx.notes.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drmmx.notes.R
import com.drmmx.notes.customviews.TaskView
import com.drmmx.notes.foundations.BaseRecyclerAdapter
import com.drmmx.notes.models.Task
import com.drmmx.notes.navigation.NavigationActivity
import kotlinx.android.synthetic.main.view_add_button.view.*

class TaskAdapter(
    taskList: MutableList<Task> = mutableListOf(),
    val touchActionDelegate: TasksListFragment.TouchActionDelegate,
    val dataActionDelegate: TaskListViewContract
) : BaseRecyclerAdapter<Task>(taskList) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_INFO) {
            TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
        } else {
            AddButtonViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_add_button, parent, false))
        }

    inner class TaskViewHolder(itemView: View) : BaseViewHolder<Task>(itemView) {

        override fun onBind(data: Task, listIndex: Int) {
            (itemView as TaskView).initView(
                task = data,
                todoCheckedCallback = { todoIndex, isChecked ->
                    dataActionDelegate.onTodoUpdated(listIndex, todoIndex, isChecked)
                },
                deleteCallback = {
                    dataActionDelegate.onTaskDeleted(listIndex)
                })
        }
    }

    inner class AddButtonViewHolder(itemView: View) : BaseRecyclerAdapter.AddButtonViewHolder(itemView) {
        override fun onBind(data: Unit, listIndex: Int) {
            itemView.buttonText.text = itemView.context.getString(R.string.add_button_task)

            itemView.setOnClickListener {
                touchActionDelegate.onAddButtonClicked(NavigationActivity.FRAGMENT_VALUE_TASK)
            }
        }

    }
}