package com.omapp.plztransfer.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TFDao {
    //모든 db 가져오기
    @Query("SELECT * FROM TFEntity")
    fun getAll() : List<TFEntity>

    //db에 추가
    @Insert
    fun insertTF(tf : TFEntity)

    //db 삭제
    @Delete
    fun deleteTF(tf : TFEntity)

    @Query("SELECT examScore FROM TFEntity")
    fun getScore() : List<Double>

    @Query("SELECT examPercent FROM TFEntity")
    fun getPercent() : List<Double>

}