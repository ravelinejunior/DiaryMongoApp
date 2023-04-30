package com.raveline.diarymongoapp.presentation.screens.write

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.raveline.diarymongoapp.data.model.DiaryModel
import com.raveline.diarymongoapp.data.model.Mood
import com.raveline.diarymongoapp.data.stateModel.GalleryState
import com.raveline.diarymongoapp.presentation.viewmodel.UiState
import java.time.ZonedDateTime

@OptIn(ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WriteScreen(
    uiState: UiState,
    moodName: () -> String,
    pagerState: PagerState,
    galleryState: GalleryState,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onDeleteClicked: () -> Unit,
    onBackPressed: () -> Unit,
    onSaveClicked: (DiaryModel?) -> Unit,
    onDateTimeUpdated: (ZonedDateTime?) -> Unit,
    onImageSelect: (Uri) -> Unit
) {

    // Update the mood when selecting a diary
    LaunchedEffect(key1 = uiState.mood) {
        pagerState.scrollToPage(Mood.valueOf(uiState.mood.name).ordinal)
    }

    Scaffold(
        topBar = {
            WriteTopBar(
                onDeleteClicked = onDeleteClicked,
                moodName = moodName,
                selectedDiary = uiState.selectedDiary,
                onBackPressed = onBackPressed,
                onDateTimeUpdated = onDateTimeUpdated
            )
        },
        content = {
            WriteContent(
                uiState = uiState,
                pagerState = pagerState,
                galleryState = galleryState,
                title = uiState.title,
                onTitleChanged = onTitleChanged,
                description = uiState.description,
                onDescriptionChanged = onDescriptionChanged,
                paddingValues = it,
                onSaveClicked = onSaveClicked,
                onImageSelect = onImageSelect,
            )
        }
    )
}