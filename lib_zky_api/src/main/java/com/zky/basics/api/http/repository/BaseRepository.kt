package com.zky.basics.api.http.repository

import android.util.Log
import com.zky.basics.api.dto.RespDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


abstract class BaseRepository {
    suspend fun <T : Any> request(call: suspend () -> RespDTO<T>): RespDTO<T> {
        return withContext(Dispatchers.IO) { call.invoke() }.apply {
            Log.e("tag", "code-->${code}")
            when (code) {

            }
        }
    }
}