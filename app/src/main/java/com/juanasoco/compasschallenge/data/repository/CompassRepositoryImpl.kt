package com.juanasoco.compasschallenge.data.repository

import com.juanasoco.compasschallenge.data.data_source.local.CompassContent
import com.juanasoco.compasschallenge.data.data_source.local.CompassDao
import com.juanasoco.compasschallenge.data.data_source.remote.CompassAPI
import com.juanasoco.compasschallenge.domain.repository.CompassRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CompassRepositoryImpl @Inject constructor(
    private val compassAPI: CompassAPI,
    private val compassDao: CompassDao
) : CompassRepository {
    override fun getContent(): Flow<Result<String>> {
        return flow {
            val cachedContent = compassDao.getCachedContent()

            if (cachedContent != null) {
                emit(Result.success(cachedContent))
            } else {
                val apiResponse = compassAPI.fetchContent()
                val compassContent = CompassContent(content = apiResponse)
                compassDao.insertContent(compassContent)
                emit(Result.success(apiResponse))
            }
        }.catch { e ->
            emit(Result.failure(e))
        }.flowOn(Dispatchers.IO)
    }

}