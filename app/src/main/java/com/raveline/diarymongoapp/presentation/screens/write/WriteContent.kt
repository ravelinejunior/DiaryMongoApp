package com.raveline.diarymongoapp.presentation.screens.write

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.raveline.diarymongoapp.R
import com.raveline.diarymongoapp.data.model.Mood

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun WriteContent(
    pagerState: PagerState,
    title: String,
    onTitleChanged: (String) -> Unit,
    description: String,
    onDescriptionChanged: (String) -> Unit,
    paddingValues: PaddingValues,
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .padding(bottom = paddingValues.calculateBottomPadding())
            .padding(bottom = 24.dp)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(state = scrollState)
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalPager(
                state = pagerState,
                count = Mood.values().size
            ) { page ->
                // Put the mood icon inside a async image
                AsyncImage(
                    modifier = Modifier.size(120.dp),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(Mood.values()[page].icon)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.mood_icon_content_description),
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            TextField(
                value = title,
                onValueChange = onTitleChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .background(Color(0x80FFFFFF)),
                placeholder = {
                    Text(text = stringResource(id = R.string.title_str))
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    disabledPlaceholderColor = Color.Unspecified,
                    errorPlaceholderColor = Color.Red,
                    focusedPlaceholderColor = Color.Unspecified,
                    unfocusedPlaceholderColor = Color.Unspecified,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {},
                ),
                maxLines = 1,
                singleLine = true,
            )

            // Description
            TextField(
                value = description,
                onValueChange = onDescriptionChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 100.dp)
                    .padding(16.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .background(Color(0x80FFFFFF)),
                placeholder = {
                    Text(text = stringResource(id = R.string.description_str))
                },
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White.copy(alpha = 0.92f),
                    focusedIndicatorColor = Color.Unspecified,
                    disabledIndicatorColor = Color.Unspecified,
                    unfocusedIndicatorColor = Color.Unspecified,
                    disabledPlaceholderColor = Color.Unspecified,
                    errorPlaceholderColor = Color.Red,
                    focusedPlaceholderColor = Color.Unspecified,
                    unfocusedPlaceholderColor = Color.Unspecified,
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    capitalization = KeyboardCapitalization.Sentences,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {},
                ),
            )
        }

        Column(verticalArrangement = Arrangement.Bottom) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                onClick = {},
                shape = Shapes().small
            ) {
                Text(text = stringResource(R.string.save_str))
            }
        }
    }

}