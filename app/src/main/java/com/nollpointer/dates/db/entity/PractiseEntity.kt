package com.nollpointer.dates.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity практики для статистики
 *
 * @author Onanov Aleksey (@onanov)
 */
@Entity(tableName = "Practise")
data class PractiseEntity(
        @ColumnInfo(name = "question") var question: String = "",
        @ColumnInfo(name = "timestamp") var timestamp: Long = 0L,
        @ColumnInfo(name = "isCorrect") var isCorrect: Boolean = true,
        //TODO добавить таблицу для ответов.
        @ColumnInfo(name = "answers") var answers: String = "",
        @ColumnInfo(name = "selectedAnswer") var selectedAnswer: String = "",
        @ColumnInfo(name = "practise") var practise: Int = 0,
        @ColumnInfo(name = "mode") var mode: Int = 0,
        @ColumnInfo(name = "type") var type: Int = 0,

        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,

        )