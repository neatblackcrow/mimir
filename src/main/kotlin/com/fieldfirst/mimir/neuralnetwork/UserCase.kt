package com.fieldfirst.mimir.neuralnetwork

data class UserCase(
    val lastPredictedInterval: Double = 0.0,
    val reviewInterval: Double = 0.0,
    val repetition: Double = 0.0,
    val grade: Double = 0.0,
) {
    var predictedInterval: Double = 0.0
}