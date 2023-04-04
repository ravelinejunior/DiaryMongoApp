package com.raveline.diarymongoapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.raveline.diarymongoapp.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    onMenuClicked: () -> Unit,
    navigateToWrite: () -> Unit
) {
    Scaffold(
        topBar = {
            HomeTopBar(onMenuClicked = onMenuClicked)
        },
        content = {

        },
        floatingActionButton = {
            FloatingActionButton(onClick = navigateToWrite) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.new_diary_icon_content_msg)
                )
            }
        }
    )
}