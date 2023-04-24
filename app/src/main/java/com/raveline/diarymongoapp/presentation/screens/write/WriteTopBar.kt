package com.raveline.diarymongoapp.presentation.screens.write

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.raveline.diarymongoapp.R
import com.raveline.diarymongoapp.common.utlis.toInstant
import com.raveline.diarymongoapp.data.model.DiaryModel
import com.raveline.diarymongoapp.presentation.components.DisplayAlertDialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteTopBar(
    selectedDiary: DiaryModel?,
    moodName: () -> String,
    onDeleteClicked: () -> Unit,
    onBackPressed: () -> Unit
) {

    val currentDate by remember {
        mutableStateOf(LocalDate.now())
    }

    val currentTime by remember {
        mutableStateOf(LocalTime.now())
    }

    val formattedDate = remember(key1 = currentDate) {
        DateTimeFormatter.ofPattern(
            "dd MMM yyyy"
        ).format(currentDate).uppercase()
    }

    val formattedTime = remember(key1 = currentTime) {
        DateTimeFormatter.ofPattern(
            "hh:mm a"
        ).format(currentTime).uppercase()
    }

    val selectedDiaryDateTime = remember(selectedDiary) {
        if (selectedDiary != null) {
            SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                .format(Date.from(selectedDiary?.date?.toInstant()))
        } else {
            "Unknown"
        }
    }

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_arrow_icon_content_description)
                )
            }
        },
        title = {
            Column {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = moodName(),
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (selectedDiary != null) selectedDiaryDateTime
                    else "$formattedDate, $formattedTime",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                        fontWeight = FontWeight.Normal
                    ),
                    textAlign = TextAlign.Center,
                )
            }
        },
        actions = {
            IconButton(
                onClick = { }
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = stringResource(R.string.date_icon_content_description),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            if (selectedDiary != null) {
                DeleteDiaryAction(
                    selectedDiary = selectedDiary,
                    onDeleteClicked = onDeleteClicked,
                )
            }
        }
    )

}

@Composable
fun DeleteDiaryAction(
    selectedDiary: DiaryModel?,
    onDeleteClicked: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    var openDialog by remember { mutableStateOf(false) }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.delete_str), style = TextStyle(
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        fontWeight = FontWeight.Bold,
                    )
                )
            },
            onClick = {
                openDialog = true
                expanded = false
            }
        )
    }

    DisplayAlertDialog(
        title = stringResource(R.string.delete_str),
        message = "Are you sure you want to permanently delete this note '${selectedDiary?.title}'?",
        dialogOpened = openDialog,
        onDialogClosed = { openDialog = false },
        onYesClicked = onDeleteClicked
    )

    IconButton(onClick = { expanded = !expanded }) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.overflow_menu_icon_content_description),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }

}
















