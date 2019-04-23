package com.drmmx.notes.foundations

import com.drmmx.notes.notes.INoteModel
import com.drmmx.notes.notes.NoteLocalModel
import com.drmmx.notes.tasks.ITaskModel
import com.drmmx.notes.tasks.TaskLocalModel
import toothpick.Toothpick
import toothpick.config.Module

object ApplicationScope {
    val scope = Toothpick.openScope(this).apply {
        installModules(ApplicationModule)
    }!!
}

object ApplicationModule : Module() {
    init {
        bind(INoteModel::class.java).toInstance(NoteLocalModel())
        bind(ITaskModel::class.java).toInstance(TaskLocalModel())
    }
}