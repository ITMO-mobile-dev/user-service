package com.database

import org.jetbrains.exposed.sql.Table

/**
 * Сущность (таблица) в базе данных
 */
object People : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 128)
    val age = integer("age")

    override val primaryKey = PrimaryKey(id)
}