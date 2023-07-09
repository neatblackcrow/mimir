package com.fieldfirst.mimir.database

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime

object Categories : IntIdTable() {
    val name: Column<String> = text("name")
    val createdOn: Column<DateTime> = datetime("created_on").default(DateTime.now())
    val updatedOn: Column<DateTime> = datetime("updated_on").check { it.greaterEq(createdOn) }.default(DateTime.now())
    val parentCategory: Column<EntityID<Int>> =
        reference("parent_category", Categories, onDelete = ReferenceOption.NO_ACTION).default(EntityID(1, Categories))
    val ordered: Column<Int> = integer("ordered").check { it.greater(0) }
}

fun Categories.fromResultRow(r: ResultRow): Category = Category(
    id = r[Categories.id].value,
    name = r[name],
    createdOn = r[createdOn],
    updatedOn = r[updatedOn],
    parentCategory = r[parentCategory],
    ordered = r[ordered]
)

data class Category(
    val id: Int,
    val name: String,
    val createdOn: DateTime,
    val updatedOn: DateTime,
    val parentCategory: EntityID<Int>,
    override val ordered: Int
) : Order()