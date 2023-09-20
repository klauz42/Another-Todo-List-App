package ru.claus42.anothertodolistapp.data.local

import androidx.room.TypeConverter
import ru.claus42.anothertodolistapp.domain.models.entities.Importance
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.UUID

object TodoItemTypeConverters {
    @TypeConverter
    @JvmStatic
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    @JvmStatic
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    @JvmStatic
    fun toImportance(importance: Int?): Importance? {
        return when(importance) {
            null -> null
            0 -> Importance.LOW
            1 -> Importance.BASIC
            else -> Importance.IMPORTANT
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromImportance(importance: Importance?): Int? {
        return when(importance) {
            Importance.LOW -> 0
            Importance.BASIC -> 1
            Importance.IMPORTANT -> 2
            else -> null
        }
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(timestamp: Long?): LocalDateTime? {
        return timestamp?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }
}