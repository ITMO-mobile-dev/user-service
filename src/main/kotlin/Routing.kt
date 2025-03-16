package com

import com.database.PersonDaoImpl
import com.kafka.consumeMessages
import com.kafka.createConsumer
import com.kafka.createProducer
import com.kafka.produceMessages
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.lettuce.core.api.sync.RedisCommands

/**
 * Класс, отвечающий за маршрутизацию запросов
 */
fun Application.configureRouting(dao: PersonDaoImpl, redisCommands: RedisCommands<String, String>) {

    routing {

        /**
         * Добавляет новую запись в таблицу сущностей People
         */
        post("/micro1/postgres"){
            val formParameters = call.receiveParameters()
            val name = formParameters.getOrFail("name")
            val age = formParameters.getOrFail("age").toInt()
            val article = dao.addPerson(name, age)
            call.respondText("Person added successfully")
        }

        /**
         * "Вытаскивает" всех пользователей, которые есть в таблице сущностей People
         */
        get("/micro1/postgres") {
            val listOfUsers = dao.getAllUsers()

            if (listOfUsers.isEmpty()) {
                call.respondText("DB is empty :(")
            } else {
                    call.respondText("Attributes of added entities:$listOfUsers")
            }
        }

        /**
         * Выдаёт приветственное сообщение
         */
        get("/micro1/hello") {
            call.respondText("Hello from micro1!")
        }

        /*
        В дальнейшем може понадобиться для регистрации пользователя в Редис
         */
//        get("/micro1/redis/create-user/1/Maxim/example@mail.ru"){
//            val id = call.parameters["id"] ?: return@get call.respondText("Missing Id", status = HttpStatusCode.BadRequest)
//            val name = call.parameters["name"] ?: return@get call.respondText("Missing name", status = HttpStatusCode.BadRequest)
//            val email = call.parameters["email"] ?: return@get call.respondText("Missing email", status = HttpStatusCode.BadRequest)
//            val key = "user:$id"
//            redisCommands.hmset(key, mapOf("name" to name, "email" to email))
//            call.respondText("User created with ID: $id")
//        }

        /**
         * Добавляет новую запись в мапу базы данных Redis. {key} - ключ для мапы, {value} - значение
         */
        get("/micro1/redis/set/{key}/{value}") {
            val key = call.parameters["key"] ?: "defaultKey"
            val value = call.parameters["value"] ?: "defaultValue"
            redisCommands.set(key, value)
            call.respondText("Key '$key' set to '$value'")
        }

        /**
         * Достаёт запись по ключу мапы в БД Redis
         */
        get("/micro1/redis/get/{key}") {
            val key = call.parameters["key"] ?: "defaultKey"
            val value = redisCommands.get(key)
            call.respondText("Value for key '$key': $value")
        }

        /**
         *
         */
        get("/micro1/kafka"){
            createProducer().produceMessages("micro1")
            call.respondText(createConsumer().consumeMessages("micro1"))
        }
    }
}
