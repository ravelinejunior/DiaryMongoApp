package com.raveline.diarymongoapp.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raveline.diarymongoapp.R
import com.raveline.diarymongoapp.common.utlis.toInstant
import com.raveline.diarymongoapp.common.utlis.toRealmInstant
import com.raveline.diarymongoapp.data.model.DiaryModel
import com.raveline.diarymongoapp.data.model.Mood
import com.raveline.diarymongoapp.ui.theme.Elevation
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@Composable
fun DiaryHolder(
    diary: DiaryModel,
    onClick: (String) -> Unit
) {

    val localDensity = LocalDensity.current
    var componentHeight by remember {
        mutableStateOf(0.dp)
    }

    Row(modifier = Modifier.clickable(
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        }
    ) { onClick(diary._id.toString()) }) {
        Spacer(modifier = Modifier.width(14.dp))

        //Line of the main screen for each card
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1
        ) {

        }
        Spacer(modifier = Modifier.width(20.dp))
        //the card
        Surface(
            modifier = Modifier
                .clip(Shapes().medium)
                .onGloballyPositioned {
                    componentHeight = with(localDensity) {
                        it.size.height.toDp()
                    }
                }
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                DiaryHeader(moodName = diary.mood, time = diary.date.toInstant())
                Text(
                    modifier = Modifier.padding(all = 14.dp),
                    text = diary.description,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

}

@Composable
fun DiaryHeader(
    moodName: String,
    time: Instant
) {

    val mood by remember {
        mutableStateOf(Mood.valueOf(moodName))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(mood.containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = mood.icon),
                contentDescription = stringResource(id = R.string.icon_mood_content_description)
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = mood.name,
                color = mood.contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )
        }
        Text(
            text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date.from(time)),
            color = mood.contentColor,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )
    }

}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DiaryHolderPreview() {
    DiaryHolder(diary = DiaryModel().apply {
        title = "I wanna fuck some bitches"
        description =
            "Why do I wanna fuck this girls so much? Because they are super hot? I have no idea."
        mood = Mood.Anxious.name
        date = Instant.now().toRealmInstant()
    }, onClick = {})
}
