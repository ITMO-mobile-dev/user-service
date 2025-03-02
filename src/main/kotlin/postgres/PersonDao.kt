package com.database

/**
 * Интерфейс для основных методов работы с БД Postgres
 */
interface PersonDao {
    suspend fun addPerson(name: String, age: Int) : Person?
    suspend fun getAllUsers() : List<Person>
}