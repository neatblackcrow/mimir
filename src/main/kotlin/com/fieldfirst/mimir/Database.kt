package com.fieldfirst.mimir

import com.fieldfirst.mimir.neuralnetwork.UserCase
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.io.File
import java.sql.Connection
import java.sql.DriverManager


class Database {

    private val db: Database

    init {
        val dbFile = File("/Users/first/Desktop/mimir.db")
        if (!dbFile.exists()) {
            val conn = DriverManager.getConnection("jdbc:sqlite:/Users/first/Desktop/mimir.db")
            val statement = conn.createStatement()
            val ddl: List<String> =
                File(this.javaClass.getResource("ddl.sql")?.path ?: "").readText().replace(System.lineSeparator(), " ")
                    .split(";")

            for (line in ddl) {
                statement.executeUpdate(line)
            }
            conn.close()
        }
        db = Database.connect(
            url = "jdbc:sqlite:/Users/first/Desktop/mimir.db", driver = "org.sqlite.JDBC"
        )
        TransactionManager.defaultDatabase = db
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    }

    private object NeuralNetworkWeights : Table() {
        val weight: Column<Double> = double("weight")
    }

    private object UserCases : Table() {
        val createdOn: Column<DateTime> = datetime("created_on").default(DateTime.now(DateTimeZone.UTC)).uniqueIndex()

        val lastPredictedInterval: Column<Double> =
            double("last_predicted_interval").check { it.between(from = 0.0, to = 1.0) }
        val reviewInterval: Column<Double> = double("review_interval").check { it.between(from = 0.0, to = 1.0) }
        val repetition: Column<Double> = double("repetition").check { it.between(from = 0.0, to = 1.0) }
        val grade: Column<Double> = double("grade").check { it.between(from = 0.0, to = 1.0) }
        val predictedInterval: Column<Double> = double("predicted_interval").check { it.between(from = 0.0, to = 1.0) }

        override val primaryKey = PrimaryKey(createdOn)
    }

    private object CardTypes : IdTable<String>() {
        val name: Column<String> = text("name").uniqueIndex()

        override val id: Column<EntityID<String>> = name.entityId()
        override val primaryKey = PrimaryKey(name)
    }

    private object Categories : IntIdTable() {
        val name: Column<String> = text("name")
        val createdOn: Column<DateTime> = datetime("created_on").default(DateTime.now(DateTimeZone.UTC))
        val updatedOn: Column<DateTime> =
            datetime("updated_on").check { it.greaterEq(createdOn) }.default(DateTime.now(DateTimeZone.UTC))
        val parentCategory: Column<EntityID<Int>> =
            reference("parent_category", Categories, onDelete = ReferenceOption.NO_ACTION).default(
                EntityID(
                    1,
                    Categories
                )
            )
    }

    private object Cards : IntIdTable() {
        val createdOn: Column<DateTime> = datetime("created_on").default(DateTime.now(DateTimeZone.UTC))
        val updatedOn: Column<DateTime> =
            datetime("updated_on").check { it.greaterEq(createdOn) }.default(DateTime.now(DateTimeZone.UTC))

        val lastPredictedInterval: Column<Int> =
            integer("last_predicted_interval").check { it.between(from = 0, to = 2048) }
        val reviewInterval: Column<Int> = integer("review_interval").check { it.between(from = 0, to = 2048) }
        val repetition: Column<Int> = integer("repetition").check { it.between(from = 0, to = 128) }
        val grade: Column<Int> = integer("grade").check { it.between(from = 0, to = 5) }
        val predictedInterval: Column<Int> = integer("predicted_interval").check { it.between(from = 0, to = 2048) }

        val front: Column<String> = text("front")
        val back: Column<String?> = text("back").nullable()
        val nextReviewOn: Column<DateTime> = date("next_review_on")
        val lastReviewOn: Column<DateTime> = date("last_review_on").default(DateTime.now(DateTimeZone.UTC))
        val category: Column<EntityID<Int>> =
            reference("category", Categories, onDelete = ReferenceOption.NO_ACTION).default(EntityID(1, Categories))
        val cardType: Column<EntityID<String>> =
            reference("card_type", CardTypes, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.NO_ACTION)
    }

    fun retrieveWeights(): List<Double> = transaction {
        NeuralNetworkWeights.selectAll().map { it[NeuralNetworkWeights.weight] }
    }

    fun saveWeights(weights: List<Double>) {
        transaction {
            NeuralNetworkWeights.batchInsert(weights, shouldReturnGeneratedValues = false) {
                this[NeuralNetworkWeights.weight] = it
            }
        }
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
                it[createdOn] = DateTime.now(DateTimeZone.UTC)
                it[lastPredictedInterval] = userCase.lastPredictedInterval
                it[reviewInterval] = userCase.reviewInterval
                it[repetition] = userCase.repetition
                it[grade] = userCase.grade
                it[predictedInterval] = userCase.predictedInterval
            }
        }
    }

}