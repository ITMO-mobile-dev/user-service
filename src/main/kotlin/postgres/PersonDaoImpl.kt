package com.database

import com.database.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

/**
 * Класс, переопределяющий работу основных методов для работы с БД Postgres
 */
class PersonDaoImpl : PersonDao {

    /**
     * Добавляет новую запись в таблицу сущностей People
     */
    override suspend fun addPerson(name: String, age: Int) : Person? = dbQuery {
            val insertStatement = People.insert{
                it[People.name] = name
                it[People.age] = age
            }
            insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToArticle)
        }

    /**
     * Преобразовывает результат запроса к БД в читаемый вид
     */
    private fun resultRowToArticle(resultRow: ResultRow) = Person(
        id = resultRow[People.id],
        name = resultRow[People.name],
        age = resultRow[People.age]
    )

    /**
     * "Вытаскивает" всех пользователей, которые есть в таблице сущностей People
     */
    override suspend fun getAllUsers() : List<Person> =
        dbQuery {
            People.selectAll().map(::resultRowToArticle)
        }

}