package com.juanasoco.compasschallenge.presentation

import com.google.common.truth.Truth.assertThat
import com.juanasoco.compasschallenge.domain.repository.CompassRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
 class CompassChallengeViewModelTest {
        private lateinit var viewModel: CompassChallengeViewModel
        private val compassRepository : CompassRepository = mock()

        private val testDispatcher = StandardTestDispatcher()
        private val mockedString = "01234  56789ABCDEFGHIJKLM 01234 01234"
        private val mockedString2 = "0123467890 ABCD EFGH IJKL MNOP"

        @Before
        fun setUp() {
            Dispatchers.setMain(testDispatcher)
        }

        @After
        fun tearDown() {
            Dispatchers.resetMain()
        }

        @Test
        fun testGetEvery10thCharacter() = runTest  {
            whenever(compassRepository.getContent()).thenReturn(flowOf(Result.success(mockedString)))
            viewModel = CompassChallengeViewModel(compassRepository,testDispatcher)
            viewModel.fetchData()
            advanceUntilIdle()
            assertThat(viewModel.every10thChar.value.get(0)).isEqualTo('9')
            assertThat(viewModel.every10thChar.value.get(1)).isEqualTo('J')
        }
        @Test
        fun testGetEvery10thCharacterWithBlankSpace() = runTest  {
            whenever(compassRepository.getContent()).thenReturn(flowOf(Result.success(mockedString2)))
            viewModel = CompassChallengeViewModel(compassRepository,testDispatcher)
            viewModel.fetchData()
            advanceUntilIdle()
            assertThat(viewModel.every10thChar.value.get(0)).isEqualTo('0')
            assertThat(viewModel.every10thChar.value.get(1)).isEqualTo('J')
        }

    @Test
    fun testCountWords() = runTest {
        // Arrange
        whenever(compassRepository.getContent()).thenReturn(flowOf(Result.success(mockedString)))
        viewModel = CompassChallengeViewModel(compassRepository,testDispatcher)

        // Act
        viewModel.fetchData()
        advanceUntilIdle()

        // Assert
        val expectedMap = mapOf(
            "01234" to 3,
            "56789abcdefghijklm" to 1
        )
        assertEquals(expectedMap, viewModel.wordCount.value)
    }

 }