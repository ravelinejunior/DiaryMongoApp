package com.raveline.diarymongoapp.presentation.screens.write

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.raveline.diarymongoapp.data.model.DiaryModel

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    selectedDiary: DiaryModel?,
    pagerState: PagerState,
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
            WriteContent(
                pagerState = pagerState,
                title = "",
                onTitleChanged = {},
                description = "",
                onDescriptionChanged = {},
                paddingValues = it
            )
        }
    )
}