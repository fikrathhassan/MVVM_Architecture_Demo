package com.example.demo.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.demo.data.local.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Upsert
    suspend fun insertRemoteKeyList(data: List<RemoteKeyEntity>)

    @Query("SELECT * FROM remote_key WHERE id = :id")
    suspend fun getRemoteKeyById(id: Long): RemoteKeyEntity?

    @Query("DELETE FROM remote_key")
    suspend fun clearRemoteKeyList()

}