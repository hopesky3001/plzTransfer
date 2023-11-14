package com.omapp.plztransfer.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TFEntity (
    @PrimaryKey(autoGenerate = true) var id : Int? = null,
    @ColumnInfo(name="examTitle") val examTitle : String,
    @ColumnInfo(name="examScore") val examScore : Double,
    @ColumnInfo(name="examPercent") val examPercent : Double
    )
