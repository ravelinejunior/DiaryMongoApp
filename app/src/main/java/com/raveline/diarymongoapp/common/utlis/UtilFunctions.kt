package com.raveline.diarymongoapp.common.utlis

import android.net.Uri
import android.util.Log
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.raveline.diarymongoapp.data.database.entity.ImageToDelete
import com.raveline.diarymongoapp.data.database.entity.ImageToUpload
import io.realm.kotlin.types.RealmInstant
import java.time.Instant

/**
 * Download images from Firebase asynchronously.
 * This function returns imageUri after each successful download.
 * */
fun fetchImagesFromFirebase(
    remoteImagePaths: List<String>,
    onImageDownload: (Uri) -> Unit,
    onImageDownloadFailed: (Exception) -> Unit = {},
    onReadyToDisplay: () -> Unit = {}
) {
    if (remoteImagePaths.isNotEmpty()) {
        remoteImagePaths.forEachIndexed { index, remoteImagePath ->
            if (remoteImagePath.trim().isNotEmpty()) {
                FirebaseStorage.getInstance().reference.child(remoteImagePath.trim()).downloadUrl
                    .addOnSuccessListener {
                        Log.i("DownloadURL", "$it")
                        onImageDownload(it)
                        if (remoteImagePaths.lastIndexOf(remoteImagePaths.last()) == index) {
                            onReadyToDisplay()
                        }
                    }.addOnFailureListener {
                        onImageDownloadFailed(it)
                    }
            }
        }
    }
}

fun retryUploadingImageToFirebase(
    imageToUpload: ImageToUpload,
    onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToUpload.remoteImagePath).putFile(
        imageToUpload.imageUri.toUri(),
        storageMetadata { },
        imageToUpload.sessionUri.toUri()
    ).addOnSuccessListener { onSuccess() }
}

fun retryDeletingImageFromFirebase(
    imageToDelete: ImageToDelete,
    onSuccess: () -> Unit
) {
    val storage = FirebaseStorage.getInstance().reference
    storage.child(imageToDelete.remoteImagePath).delete()
        .addOnSuccessListener { onSuccess() }
}

fun RealmInstant.toInstant(): Instant {
    val sec: Long = this.epochSeconds
    val nano: Int = this.nanosecondsOfSecond
    return if (sec >= 0) {
        Instant.ofEpochSecond(sec, nano.toLong())
    } else {
        Instant.ofEpochSecond(sec - 1, 1_000_000 + nano.toLong())
    }
}

fun Instant.toRealmInstant(): RealmInstant {
    val sec: Long = this.epochSecond
    val nano: Int = this.nano
    return if (sec >= 0) {
        RealmInstant.from(sec, nano)
    } else {
        RealmInstant.from(sec + 1, -1_000_000 + nano)
    }
}

@Composable
fun CustomSnackBar(
    message: String,
    action: (() -> Unit)? = null
) {
    val snackBarHostState = remember { SnackbarHostState() }

    SnackbarHost(
        hostState = snackBarHostState,
        snackbar = {
            Snackbar(
                action = {
                    action?.let { onClick ->
                        TextButton(onClick = onClick) {
                            Text("Dismiss")
                        }
                    }
                }
            ) {
                Text(text = message)
            }
        }
    )

    if (message.isNotEmpty()) {
        LaunchedEffect(true) {
            snackBarHostState.showSnackbar(message)
        }
    }
}