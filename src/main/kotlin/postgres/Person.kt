package com.database

import kotlinx.serialization.Serializable

/**
 * Атрибуты (столбцы) записи (строки) в таблице сущностей People
 */
@Serializable
data class Person(
    val id: Int,
    val name: String,
    val age: Int
)
