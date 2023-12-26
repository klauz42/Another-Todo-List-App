package ru.claus42.anothertodolistapp.data.local

import androidx.room.TypeConverter
import ru.claus42.anothertodolistapp.data.local.models.entities.DataItemPriority
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
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
    fun toPriority(importance: Int): DataItemPriority {
        return when (importance) {
            0 -> DataItemPriority.LOW
            1 -> DataItemPriority.BASIC
            else -> DataItemPriority.IMPORTANT
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromPriority(itemPriority: DataItemPriority): Int {
        return when (itemPriority) {
            DataItemPriority.LOW -> 0
            DataItemPriority.BASIC -> 1
            DataItemPriority.IMPORTANT -> 2
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
}