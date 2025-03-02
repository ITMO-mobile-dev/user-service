package com

import com.database.DatabaseFactory
import com.database.PersonDao
import com.database.PersonDaoImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.lettuce.core.RedisClient
import kotlinx.coroutines.launch

fun main(args: Array<String>) {

    /**
     * Основное подключение к серверу
     */
    embeddedServer(Netty, port = 8081, host = "127.0.0.1") {

        val redisClient = RedisClient.create("redis://localhost:6379")
        val connection = redisClient.connect()
        val redisCommands = connection.sync()
        val userDaoImpl = PersonDaoImpl()

        module()
        configureRouting(userDaoImpl, redisCommands)

        /**
         * Тестовое подключение пользователя и отправка запросов Postgres и Redis
         */
        environment.monitor.subscribe(ApplicationStarted) { application ->

            application.launch {
                kotlinx.coroutines.delay(1000)

                val client = HttpClient(CIO)
                val rClient = RedisClient.create("redis://localhost:6379")
                val connectR = rClient.connect()
                val rCommands = connectR.sync()

                try {
                    val postResponse = client.post("http://localhost:8081/micro1/postgres") {
                        contentType(ContentType.Application.Json)
                        setBody(FormDataContent(Parameters.build {
                            append("name", "Maxim")
                            append("age", "20")
                        }))
                    }

                    println("POST Response from Postgres: ${postResponse.status}. DB has created. A new entity was created")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    val getResponse = client.get("http://localhost:8081/micro1/postgres")
                    println("GET Response from Postgres: ${getResponse.bodyAsText()}")
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                try {
                    rCommands.set("name", "Maxim")
                    rCommands.set("age", "19")
                    println(
                        "Redis successfully added new Person with parameters: ${rCommands.get("name")}, ${
                            rCommands.get(
                                "age"
                            )
                        }"
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                client.close()
            }
        }

    }.start(wait = true)

}

/**
 * Подключение к Postgres
 */
fun Application.module() {
    DatabaseFactory.init()
}
