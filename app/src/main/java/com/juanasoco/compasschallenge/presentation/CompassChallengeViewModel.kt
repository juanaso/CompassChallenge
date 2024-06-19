package com.juanasoco.compasschallenge.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juanasoco.compasschallenge.domain.repository.CompassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompassChallengeViewModel @Inject constructor(
    private val compassRepository: CompassRepository
): ViewModel(){

    private val _every10thChar = MutableStateFlow<List<Char>>(emptyList())
    val every10thChar: StateFlow<List<Char>> get() = _every10thChar

    private val _wordCount = MutableStateFlow<Map<String, Int>>(emptyMap())
    val wordCount: StateFlow<Map<String, Int>> get() = _wordCount


    private val _fetchingState = mutableStateOf(false)
    val fetchingState: State<Boolean> = _fetchingState


    fun fetchData() {
        viewModelScope.launch {
            _fetchingState.value = true
            val contentDeferred1 = async { compassRepository.getContent().firstOrNull { it.isSuccess }?.getOrNull() }
            val contentDeferred2 = async { compassRepository.getContent().firstOrNull { it.isSuccess }?.getOrNull() }

            val content1 = contentDeferred1.await()
            val content2 = contentDeferred2.await()

            content1?.let {
                _every10thChar.value = getEvery10thCharacter(it)
            }
            content2?.let {
                _wordCount.value = countWords(it)
            }
            _fetchingState.value = false
        }
    }

    private fun getEvery10thCharacter(content: String): List<Char> {
        val contentWithoutSpaces = content.replace(" ", "")
        return contentWithoutSpaces.filterIndexed { index, _ -> (index + 1) % 10 == 0 }.toList()
    }

    private fun countWords(content: String): Map<String, Int> {
        return content.split(Regex("\\s+"))
            .groupingBy { it.toLowerCase() }
            .eachCount()
    }

}