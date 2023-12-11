package ru.claus42.anothertodolistapp.data.local

import androidx.room.TypeConverter
import ru.claus42.anothertodolistapp.domain.models.entities.ItemPriority
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
    fun toPriority(importance: Int?): ItemPriority? {
        return when (importance) {
            null -> null
            0 -> ItemPriority.LOW
            1 -> ItemPriority.BASIC
            else -> ItemPriority.IMPORTANT
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromPriority(itemPriority: ItemPriority?): Int? {
        return when (itemPriority) {
            ItemPriority.LOW -> 0
            ItemPriority.BASIC -> 1
            ItemPriority.IMPORTANT -> 2
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