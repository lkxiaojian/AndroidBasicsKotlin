package com.zky.basics.common.repository

import com.zky.basics.api.dto.RespDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


 class BaseRepository {
    suspend fun <T : Any> request(call: suspend () -> RespDTO<T>): RespDTO<T> {
        return withContext(Dispatchers.IO) { call.invoke() }.apply {
        }
    }
}