package com.raveline.diary.util

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.google.firebase.storage.FirebaseStorage
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

@RequiresApi(Build.VERSION_CODES.O)
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