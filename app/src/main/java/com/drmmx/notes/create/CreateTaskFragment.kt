package com.drmmx.notes.create

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.drmmx.notes.R
import com.drmmx.notes.customviews.CreateTodoView
import com.drmmx.notes.foundations.ApplicationScope
import com.drmmx.notes.foundations.NullFieldChecker
import com.drmmx.notes.foundations.StateChangeTextWatcher
import com.drmmx.notes.models.Task
import com.drmmx.notes.models.Todo
import com.drmmx.notes.tasks.ITaskModel
import kotlinx.android.synthetic.main.fragment_create_task.*
import kotlinx.android.synthetic.main.view_create_task.view.*
import kotlinx.android.synthetic.main.view_create_todo.view.*
import toothpick.Toothpick
import javax.inject.Inject

private const val MAX_TODO_COUNT = 5

class CreateTaskFragment : Fragment() {

    @Inject
    lateinit var model: ITaskModel

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toothpick.inject(this, ApplicationScope.scope)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createTaskView.taskEditText.addTextChangedListener(object : StateChangeTextWatcher() {
            override fun afterTextChanged(s: Editable?) {

                if (!s.isNullOrEmpty() && previousValue.isNullOrEmpty()) {
                    addTodoView()
                }

                super.afterTextChanged(s)
            }
        })
    }

    private fun addTodoView() {
        if (canCreateTodo()) {
            val view = (LayoutInflater.from(context)
                .inflate(R.layout.view_create_todo, containerView, false) as CreateTodoView).apply {

                todoEditText.addTextChangedListener(object : StateChangeTextWatcher() {
                    override fun afterTextChanged(s: Editable?) {

                        if (!s.isNullOrEmpty() && previousValue.isNullOrEmpty()) {
                            addTodoView()
                        } else if (s.isNullOrEmpty() && !previousValue.isNullOrEmpty()) {
                            removeTodoView(this@apply)

                            if (containerView.childCount == MAX_TODO_COUNT) {
                                addTodoView()
                            }
                        }

                        super.afterTextChanged(s)
                    }
                })
            }

            containerView.addView(view)
        }
    }

    private fun removeTodoView(view: View) {
        containerView.removeView(view)
    }

    private fun canCreateTodo(): Boolean = (containerView.childCount < MAX_TODO_COUNT + 1) &&
            !(containerView.getChildAt(containerView.childCount -1) as NullFieldChecker).hasNullField()

    private fun isTaskEmpty(): Boolean = createTaskView.taskEditText.editableText.isNullOrEmpty()

    fun saveTask(callback: (Boolean) -> Unit) {
        createTask()?.let {task ->
            model.addTask(task) {
                callback.invoke(it)
            }
        } ?: callback.invoke(false)
    }

    private fun createTask(): Task? {
        if (!isTaskEmpty()) {
            containerView.run {
                var taskField: String? = null
                val todoList: MutableList<Todo> = mutableListOf()

                for (i in 0 until containerView.childCount) {
                    if (i == 0) {
                        taskField = containerView.getChildAt(i).taskEditText.editableText.toString()
                    } else {
                        if (!containerView.getChildAt(i).todoEditText.editableText.isNullOrEmpty()) {
                            todoList.add(
                                Todo(description = containerView.getChildAt(i).todoEditText.editableText.toString())
                            )
                        }
                    }
                }
                return taskField?.let {
                    Task(taskField, todoList)
                }
            }
        } else {
            return null
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction()
    }

    companion object {
        fun newInstance() = CreateTaskFragment()
    }

}
