package com.raveline.diarymongoapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.raveline.diarymongoapp.R
import kotlin.math.max

@Composable
fun Gallery(
    modifier: Modifier = Modifier,
    images: List<String>,
    imageSize: Dp = 40.dp,
    spaceBetween: Dp = 10.dp,
    imageShape: CornerBasedShape = Shapes().small
) {

    BoxWithConstraints {
        val numberOfVisibleImages = remember {
            derivedStateOf {
                max(
                    a = 0,
                    b = this.maxWidth.div(spaceBetween + imageSize).toInt().minus(1)
                )
            }
        }

        val remainingImages = remember {
            derivedStateOf {
                images.size - numberOfVisibleImages.value
            }
        }

        Row {
            images.take(numberOfVisibleImages.value).forEach { image ->
                AsyncImage(
                    modifier = Modifier
                        .clip(imageShape)
                        .size(imageSize),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(image)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(id = R.string.gallery_image_content_desc)
                )
                Spacer(modifier = Modifier.width(spaceBetween))
            }

            if (remainingImages.value > 0) {
                LastImageOverlay(
                    imageSize = imageSize,
                    remainingImages = remainingImages.value,
                    imageShape = imageShape
                )
            }
        }
    }

}

@Composable
fun LastImageOverlay(
    imageSize: Dp,
    remainingImages: Int,
    imageShape: CornerBasedShape
) {

    Box(contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier
                .clip(imageShape)
                .size(imageSize),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {

        }
        Text(
            text = "+$remainingImages",
            style = TextStyle(
                fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }

}

@Composable
fun ShowGalleryButton(
    galleryOpened: Boolean,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = if (galleryOpened) stringResource(R.string.hide_gallery_str) else stringResource(
                R.string.show_gallery_str
            ),
            style = TextStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)
        )
    }
}

@Preview
@Composable
fun GalleryPreview() {
    GalleryPreview()
}