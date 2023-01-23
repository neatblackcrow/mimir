package com.fieldfirst.mimir.database

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object NeuralNetworkWeights : Table() {
    val weight: Column<Double> = double("weight")
}

fun restoreWeights(): List<Double> = transaction {
    NeuralNetworkWeights.selectAll().map { it[NeuralNetworkWeights.weight] }
}

fun saveWeights(weights: List<Double>) {
    transaction {
        NeuralNetworkWeights.batchInsert(weights, shouldReturnGeneratedValues = false) {
            this[NeuralNetworkWeights.weight] = it
        }
    }
}