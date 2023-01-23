package com.fieldfirst.mimir.cubit

import kotlin.system.exitProcess

class MainCubit : BaseCubit<Any>() {
    override fun initialState(): Any = "This cubit has no state."

    fun exitApplication() {
        exitProcess(0)
    }
}