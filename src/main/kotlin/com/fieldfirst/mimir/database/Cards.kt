package com.fieldfirst.mimir.database

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.date
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Cards : IntIdTable() {
    val createdOn: Column<DateTime> = datetime("created_on").default(DateTime.now())
    val updatedOn: Column<DateTime> =
        datetime("updated_on").check { it.greaterEq(createdOn) }.default(DateTime.now())

    val lastPredictedInterval: Column<Int> =
        integer("last_predicted_interval").check { it.between(from = 0, to = 2048) }
    val reviewInterval: Column<Int> = integer("review_interval").check { it.between(from = 0, to = 2048) }
    val repetition: Column<Int> = integer("repetition").check { it.between(from = 0, to = 128) }
    val grade: Column<Int> = integer("grade").check { it.between(from = 0, to = 5) }
    val predictedInterval: Column<Int> = integer("predicted_interval").check { it.between(from = 0, to = 2048) }

    val front: Column<String> = text("front")
    val back: Column<String?> = text("back").nullable()
    val nextReviewOn: Column<DateTime> = date("next_review_on")
    val lastReviewOn: Column<DateTime> = date("last_review_on").default(DateTime.now())
    val category: Column<EntityID<Int>> =
        reference("category", Categories, onDelete = ReferenceOption.NO_ACTION).default(EntityID(1, Categories))
    val cardType: Column<EntityID<String>> =
        reference("card_type", CardTypes, onUpdate = ReferenceOption.CASCADE, onDelete = ReferenceOption.NO_ACTION)
}

data class Card(
    val id: Int,
    val createdOn: DateTime,
    val updatedOn: DateTime,
    val lastPredictedInterval: Int,
    val reviewInterval: Int,
    val repetition: Int,
    val grade: Int,
    val predictedInterval: Int,
    val front: String,
    val back: String?,
    val nextReviewOn: DateTime,
    val lastReviewOn: DateTime,
    val category: Int,
    val cardType: String
)