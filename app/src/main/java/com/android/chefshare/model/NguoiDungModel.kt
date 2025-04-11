package com.android.chefshare.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "nguoidung")
data class NguoiDungModel(
    @PrimaryKey // @PrimaryKey(autoGenerate = true)
    val id_nguoidung: String,
    val google_id: String,
    val email : String,
    val tennguoidung : String,
    val avatar_url : String,
    val access_token : String,
    val created_at : LocalDateTime,
    val updated_at : LocalDateTime,
)