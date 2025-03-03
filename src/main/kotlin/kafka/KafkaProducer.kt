package com.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerRecord
import java.time.Duration
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.fixedRateTimer

fun createProducer() : Producer<String, String>{
    val props = Properties()

    props["bootstrap.servers"] = "localhost:9092"
    props["acks"] = "all"
    props["retries"] = 0
    props["linger.ms"] = 1
    props["key.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"
    props["value.serializer"] = "org.apache.kafka.common.serialization.StringSerializer"

    return KafkaProducer(props)
}

fun Producer<String, String>.produceMessages(topic: String){
    fixedRateTimer(daemon = true, period = Duration.ofSeconds(2).toMillis()){
        val time = LocalDateTime.now()
        val message = ProducerRecord(
            topic,
            time.toString(),
            "Hello from Kafka in micro1!"
        )

        println("Producer sending a meassage: $message")
        this@produceMessages.send(message)
    }
}

