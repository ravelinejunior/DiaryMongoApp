package com.raveline.diarymongoapp.presentation.screens.home

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.raveline.diarymongoapp.R
import com.raveline.diarymongoapp.data.model.DiaryModel
import com.raveline.diary.ui.components.DiaryHolder
import java.time.LocalDate
import java.util.*


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    diaryNotes: Map<LocalDate, List<DiaryModel>>,
    onClick: (String) -> Unit,
    paddingValues: PaddingValues
) {
    if (diaryNotes.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .imePadding()
                .navigationBarsPadding()
                .padding(top = paddingValues.calculateTopPadding())

        ) {
            diaryNotes.forEach { (localDate, diaries) ->

                // Diary Header
                stickyHeader(key = localDate) {
                    DateHeader(localDate = localDate)
                }

                // Diary Items
                items(
                    items = diaries,
                    key = { it._id.toString() }
                ) {
                    DiaryHolder(diary = it, onClick = onClick)
                }
            }
        }
    } else {
        EmptyPage(
            subtitle = "Your Diary is Empty Darling. Tell us something!"
        )
    }
}

@Composable
fun DateHeader(localDate: LocalDate) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.75f))
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = String.format("%02d", localDate.dayOfMonth),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                )
            )

            Text(
                text = localDate.dayOfWeek.toString().take(3),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = localDate.month.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Light
                )
            )

            Text(
                text = "${localDate.year}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.87f),
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    fontWeight = FontWeight.Light
                )
            )
        }
    }
}

@Composable
fun EmptyPage(
    title: String = "Empty Diary",
    subtitle: String = "Write Something"
) {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.read_book))

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.Medium,
                )
            )

            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                restartOnPlay = true,
                reverseOnRepeat = true,
                enableMergePaths = true,
            )


            Text(
                text = subtitle,
                style = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Normal,
                )
            )
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_MASK)
@Composable
fun DateHeaderPreview() {
    DateHeader(localDate = LocalDate.now())
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_MASK)
@Composable
fun EmptyScreenPreview() {
    EmptyPage()
}