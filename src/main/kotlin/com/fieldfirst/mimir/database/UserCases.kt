package com.fieldfirst.mimir.database

import com.fieldfirst.mimir.neuralnetwork.UserCase
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

object UserCases : Table() {
    val createdOn: Column<DateTime> = datetime("created_on").default(DateTime.now()).uniqueIndex()

    val lastPredictedInterval: Column<Double> =
        double("last_predicted_interval").check { it.between(from = 0.0, to = 1.0) }
    val reviewInterval: Column<Double> = double("review_interval").check { it.between(from = 0.0, to = 1.0) }
    val repetition: Column<Double> = double("repetition").check { it.between(from = 0.0, to = 1.0) }
    val grade: Column<Double> = double("grade").check { it.between(from = 0.0, to = 1.0) }
    val predictedInterval: Column<Double> = double("predicted_interval").check { it.between(from = 0.0, to = 1.0) }

    override val primaryKey = PrimaryKey(createdOn)
}

fun retrieveUserCases(batchSize: Int = 3000): List<UserCase> = transaction {
    UserCases.selectAll().orderBy(UserCases.createdOn, SortOrder.DESC).limit(batchSize).map {
        UserCase(
            lastPredictedInterval = it[UserCases.lastPredictedInterval],
            reviewInterval = it[UserCases.reviewInterval],
            repetition = it[UserCases.repetition],
            grade = it[UserCases.grade]
        ).apply { predictedInterval = it[UserCases.predictedInterval] }
    }
}

fun insertUserCase(userCase: UserCase) {
    transaction {
        UserCases.insert {
            it[createdOn] = DateTime.now()
            it[lastPredictedInterval] = userCase.lastPredictedInterval
            it[reviewInterval] = userCase.reviewInterval
            it[repetition] = userCase.repetition
            it[grade] = userCase.grade
            it[predictedInterval] = userCase.predictedInterval
        }
    }
}