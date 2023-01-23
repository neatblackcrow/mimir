package com.fieldfirst.mimir.database

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column

object CardTypes : IdTable<String>() {
    val name: Column<String> = text("name").uniqueIndex()

    override val id: Column<EntityID<String>> = name.entityId()
    override val primaryKey = PrimaryKey(name)
}