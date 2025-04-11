package com.android.chefshare.controller

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.android.chefshare.model.NguoiDungModel

@Dao
interface NguoiDungDAO  {
    @Query("Select * from nguoidung")
    fun LayNguoiDung():List<NguoiDungModel>?
    @Delete
    fun xoaNguoiDung(nguoidung:NguoiDungModel):Boolean
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun themNguoiDung(nguoidung: NguoiDungModel):Boolean


}