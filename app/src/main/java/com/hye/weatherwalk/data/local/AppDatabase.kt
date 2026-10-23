package com.hye.weatherwalk.data.local

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver


@Database(
    entities = [HourlyWeatherEntity::class],
    version = 1,
    exportSchema = false   // 마이그레이션 관리가 필요해지면 true + room3 { schemaDirectory(...) }
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        private const val DATABASE_NAME = "weatherwalk.db"

        // 만들어진 DB를 보관하는 곳. @Volatile: 한 스레드가 넣은 값을 다른 스레드가 바로 보게 함
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * 앱 전체에서 하나뿐인 DB를 돌려준다. (싱글턴)
         * 이미 있으면 그걸 반환하고, 없을 때만 새로 만든다.
         */
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                // 동시에 두 스레드가 들어와도 한 번만 만들도록 안에서 한 번 더 확인
                INSTANCE ?: create(context).also { INSTANCE = it }
            }

        private fun create(context: Context): AppDatabase =
            Room.databaseBuilder<AppDatabase>(
                context = context.applicationContext,
                name = DATABASE_NAME
            )
                .setDriver(BundledSQLiteDriver())
                .build()
    }
}