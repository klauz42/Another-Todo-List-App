package ru.claus42.anothertodolistapp.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ru.claus42.anothertodolistapp.di.modules.DATABASE_NAME


val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.beginTransaction()

        try {
            //milliseconds replaced with seconds
            db.execSQL("UPDATE $DATABASE_NAME SET deadline = deadline / 1000")
            db.execSQL("UPDATE $DATABASE_NAME SET created_at = created_at / 1000")
            db.execSQL("UPDATE $DATABASE_NAME SET changed_at = changed_at / 1000")
        } finally {
            db.endTransaction()
        }
    }
}