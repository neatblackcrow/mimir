package com.fieldfirst.mimir.cubit

import kotlinx.coroutines.launch

class TestCubit : BaseCubit<String>() {
    override fun initialState(): String = "test"

    private var counter: Int = 0

    fun testFlow() {
        cubitScope.launch { _flow.emit("Hello world $counter") }
        counter++
    }

}