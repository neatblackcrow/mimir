package com.fieldfirst.mimir.cubit

import kotlinx.coroutines.launch

class TestCubit : BaseCubit<String>() {
    override fun initialState(): String = "test"

    fun testFlow() {
        cubitScope.launch { _flow.emit("Hello world") }
    }

}