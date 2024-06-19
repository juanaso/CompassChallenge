package com.juanasoco.compasschallenge.data.repository

import com.google.common.truth.Truth.assertThat
import com.juanasoco.compasschallenge.data.data_source.local.CompassDao
import com.juanasoco.compasschallenge.data.data_source.remote.CompassAPI
import com.juanasoco.compasschallenge.domain.repository.CompassRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

import com.google.common.truth.Truth.assertThat
import com.juanasoco.compasschallenge.data.data_source.local.CompassContent
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest


@ExperimentalCoroutinesApi
class CompassRepositoryImplTest {

    private lateinit var compassRepository: CompassRepositoryImpl
    private val compassAPI: CompassAPI = mock()
    private val compassDao: CompassDao = mock()

    private val cachedContent = "Cached content"
    private val apiResponse = "API response"
    private val errorMessage = "Something went wrong"
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        compassRepository = CompassRepositoryImpl(compassAPI, compassDao)

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Before
    fun setup() {
    }

    @Test
    fun testGetContentWithCachedData() = runTest {
        whenever(compassDao.getCachedContent()).thenReturn(cachedContent)

        val result = compassRepository.getContent().first()

        assert(result.isSuccess)
        assertThat(result.getOrNull()).isEqualTo(cachedContent)

        verify(compassDao, never()).insertContent(any())
        verify(compassAPI, never()).fetchContent()
    }

    @Test
    fun testGetContentWithApiSuccess() = runTest {
        whenever(compassDao.getCachedContent()).thenReturn(null)
        whenever(compassAPI.fetchContent()).thenReturn(apiResponse)
        advanceUntilIdle()

        val result = compassRepository.getContent().first()
        advanceUntilIdle()


        assert(result.isSuccess)
        assertThat(result.getOrNull()).isEqualTo(apiResponse)

        verify(compassDao, times(1)).insertContent(CompassContent(content = apiResponse))
        verify(compassAPI, times(1)).fetchContent()
    }

    @Test
    fun testGetContentWithApiError() = runTest {
        whenever(compassDao.getCachedContent()).thenReturn(null)
        whenever(compassAPI.fetchContent()).thenThrow(RuntimeException(errorMessage))
        advanceUntilIdle()

        val result = compassRepository.getContent().onEach { delay(100) } // Introduce a delay to simulate asynchronous behavior
            .first()
        advanceUntilIdle()

        assert(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)

        verify(compassDao, never()).insertContent(any())
        verify(compassAPI, times(1)).fetchContent()
    }

    @Test
    fun testGetContentErrorWhenDaoFails() = runTest {
        whenever(compassDao.getCachedContent()).thenThrow(RuntimeException(errorMessage))

        val result = compassRepository.getContent().first()

        assert(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)

        verify(compassAPI, never()).fetchContent()
    }

}
