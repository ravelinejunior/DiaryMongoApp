package com.raveline.diarymongoapp.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.raveline.diarymongoapp.data.model.DiaryModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    selectedDiary: DiaryModel?,
    onDeleteClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            WriteTopBar(
                selectedDiary = selectedDiary,
                onDeleteClicked = onDeleteClicked,
                onBackPressed = onBackPressed
            )
        },
        content = {

        }
    )
}