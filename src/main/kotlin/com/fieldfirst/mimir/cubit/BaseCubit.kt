package com.fieldfirst.mimir.cubit

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

abstract class BaseCubit<T> {
    protected val _flow: MutableStateFlow<T>
    val flow: StateFlow<T>
        get() = _flow.asStateFlow()

    val cubitScope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    protected abstract fun initialState(): T

    init {
        _flow = MutableStateFlow(initialState())
    }
}