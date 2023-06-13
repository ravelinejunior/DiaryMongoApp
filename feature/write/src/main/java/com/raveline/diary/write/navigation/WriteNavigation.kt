package com.raveline.diary.write.navigation

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import com.raveline.diary.util.Constants
import com.raveline.diary.util.model.Mood
import com.raveline.diary.util.screens.Screens
import com.raveline.diary.write.WriteScreen
import com.raveline.diary.write.WriteViewModel

val TAG: String = NavGraph::class.java.simpleName

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
fun NavGraphBuilder.writeRoute(
    onBackPressed: () -> Unit
) {

    composable(
        route = Screens.Write.route,
        arguments = listOf(
            navArgument(name = Constants.WRITE_SCREEN_ARGUMENT_ID) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
    ) {

        val context = LocalContext.current
        val writeViewModel: WriteViewModel = hiltViewModel()
        val uiState = writeViewModel.uiState
        val pagerState = rememberPagerState()
        val pageNumber by remember {
            derivedStateOf { pagerState.currentPage }
        }
        val galleryState = writeViewModel.galleryState

        LaunchedEffect(key1 = uiState) {
            Log.d(TAG, "Selected Diary Id: ${uiState.selectedDiaryId}")
        }

        WriteScreen(
            uiState = uiState,
            pagerState = pagerState,
            galleryState = galleryState,
            moodName = {
                Mood.values()[pageNumber].name
            },
            onTitleChanged = {
                writeViewModel.setTitle(title = it)
            },
            onDescriptionChanged = {
                writeViewModel.setDescription(description = it)
            },
            onDeleteClicked = {
                writeViewModel.deleteDiary(
                    onSuccess = {
                        Toast.makeText(
                            context,
                            "${uiState.title} deleted.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onError = {
                        Toast.makeText(
                            context,
                            it,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                )
            },
            onBackPressed = onBackPressed,
            onSaveClicked = { diary ->
                diary?.apply {
                    //Setting the selected mood saved on viewModel
                    mood = Mood.values()[pageNumber].name
                }?.let { finalDiary ->
                    writeViewModel.upsertDiary(
                        diaryModel = finalDiary,
                        onSuccess = {
                            onBackPressed()
                        },
                        onError = {
                            Log.e(TAG, "writeRoute: $it")
                        }
                    )
                }
            },
            onDateTimeUpdated = {
                writeViewModel.updateDateTime(zonedDateTime = it)
            },
            onImageSelect = { imageUri ->

                val type = context.contentResolver.getType(imageUri)?.split("/")?.last() ?: "jpeg"

                Log.i(TAG, "OnImageSelect Called - Uri: $imageUri")

                writeViewModel.addImage(imageUri, type)

            },
            onImageDeleteClick = {
                galleryState.removeImage(it)
            }
        )
    }
}
