package com.juanasoco.compasschallenge.domain.repository

import kotlinx.coroutines.flow.Flow

interface CompassRepository {

    fun getContent(): Flow<Result<String>>

}