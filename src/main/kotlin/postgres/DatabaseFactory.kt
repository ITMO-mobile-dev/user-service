package com.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Отвечает за создание соединения с Postgres и отправкой запросов
 */
object DatabaseFactory {
    fun init(){
        val database = Database.connect(
            url = "jdbc:postgresql://localhost:5432/ITMO-mobile",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "123"
        )
        transaction (database){
            SchemaUtils.create(People)
        }
    }

    /**
     * Отправка запросов
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


}