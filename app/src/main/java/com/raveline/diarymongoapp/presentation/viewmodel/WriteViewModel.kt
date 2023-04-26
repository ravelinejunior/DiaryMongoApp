package com.raveline.diarymongoapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raveline.diarymongoapp.common.utlis.Constants.WRITE_SCREEN_ARGUMENT_ID
import com.raveline.diarymongoapp.common.utlis.RequestState
import com.raveline.diarymongoapp.data.model.DiaryModel
import com.raveline.diarymongoapp.data.model.MongoDB
import com.raveline.diarymongoapp.data.model.Mood
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId

class WriteViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    private fun getDiaryIdArgument() {

        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = WRITE_SCREEN_ARGUMENT_ID
            )
        )

    }

    private fun fetchSelectedDiary() = viewModelScope.launch {
        if (uiState.selectedDiaryId != null) {
            val diary = MongoDB.getSelectedDiary(
                diaryId = ObjectId(uiState.selectedDiaryId!!)
            )

            if (diary is RequestState.Success) {
                val data = diary.data
                withContext(Dispatchers.Main) {
                    setSelectedDiary(diaryModel = data)
                    setTitle(title = data.title)
                    setDescription(description = data.description)
                    setMood(Mood.valueOf(data.mood))
                }
            }
        }
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(
            title = title
        )
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(
            description = description
        )
    }

    private fun setMood(mood: Mood) {
        uiState = uiState.copy(
            mood = mood
        )
    }

    private fun setSelectedDiary(diaryModel: DiaryModel) {
        uiState = uiState.copy(selectedDiary = diaryModel)
    }


    fun insertDiary(
        diaryModel: DiaryModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch(IO) {

        when (val result = MongoDB.addNewDiary(diaryModel)) {
            is RequestState.Success -> {
                withContext(Main) {
                    onSuccess()
                }
            }

            is RequestState.Error -> {
                withContext(Main) {
                    onError(result.error.message.toString())
                }
            }

            else -> {
                withContext(Main) {
                    onError("Something went wrong. Try again.")
                }
            }
        }

    }

}

data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: DiaryModel? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
)