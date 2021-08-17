package com.example.roomtest

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// @Database 어노테이션을 사용하여서 Entity와 Database Version을 지정해 준다.
@Database(entities = arrayOf(Book::class) , version = 1)
abstract class AppDataBase : RoomDatabase(){
    //  DAO를 사용하기 위하여 bookDao를 선언한다.
    abstract fun bookDao() : BookDao

    // Database 인스턴스의 중복 생성을 방지하기 위하여 싱글톤 패턴으로 생성한 후 getInstance를 사용하여 객체를 받을 수 있도록 만든다.
    companion object {
        private var Instance: AppDataBase? = null

        // Room Database를 생성하기 위해서는 RoomDatabase를 상속받아야만 하며
        // databaseBuilder를 사용하여 생성할 수 있다.
        fun getInstance(context: Context): AppDataBase?{
            if (Instance == null){
                synchronized(AppDataBase::class){
                    Instance = Room.databaseBuilder(context, AppDataBase::class.java, "hanja").build()
                    // migration // .addMigrations(MIGRATION_1_2)
                }
            }
            return Instance
        }

        fun deleteInstance(){
            Instance = null
        }

        // 마이그레이션 부분 (생략)
        val MIGRATION_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `books2` (`id` LONG, `name` TEXT, `writer` TEXT, `price` INTEGER, " +
                        "PRIMARY KEY(`id`))")
            }

        }

    }
}