package ru.claus42.anothertodolistapp.data.local

import androidx.room.TypeConverter
import ru.claus42.anothertodolistapp.data.local.models.entities.LocalDataItemPriority
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID


object TodoItemTypeConverters {
    @TypeConverter
    @JvmStatic
    fun toUUID(uuid: String): UUID {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    @JvmStatic
    fun fromUUID(uuid: UUID): String {
        return uuid.toString()
    }

    @TypeConverter
    @JvmStatic
    fun toPriority(importance: Int): LocalDataItemPriority {
        return when (importance) {
            0 -> LocalDataItemPriority.LOW
            1 -> LocalDataItemPriority.BASIC
            else -> LocalDataItemPriority.IMPORTANT
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromPriority(itemPriority: LocalDataItemPriority): Int {
        return when (itemPriority) {
            LocalDataItemPriority.LOW -> 0
            LocalDataItemPriority.BASIC -> 1
            LocalDataItemPriority.IMPORTANT -> 2
        }
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDateTime(timestamp: Long): LocalDateTime {
        return timestamp.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalDateTime(date: LocalDateTime): Long {
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    @TypeConverter
    @JvmStatic
    fun toZonedDateTime(timestamp: Long): ZonedDateTime {
        return Instant.ofEpochSecond(timestamp).atZone(ZoneId.systemDefault())

    }

    @TypeConverter
    @JvmStatic
    fun fromZonedDateTime(date: ZonedDateTime): Long {
        return date.toEpochSecond()
    }
}