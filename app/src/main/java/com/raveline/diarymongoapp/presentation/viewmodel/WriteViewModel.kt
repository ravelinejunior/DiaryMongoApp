package com.raveline.diarymongoapp.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.raveline.diarymongoapp.common.utlis.Constants.WRITE_SCREEN_ARGUMENT_ID
import com.raveline.diarymongoapp.common.utlis.fetchImagesFromFirebase
import com.raveline.diarymongoapp.common.utlis.toRealmInstant
import com.raveline.diarymongoapp.data.database.dao.ImagesToDeleteDao
import com.raveline.diarymongoapp.data.database.dao.ImagesUploadDao
import com.raveline.diarymongoapp.data.database.entity.ImageToDelete
import com.raveline.diarymongoapp.data.database.entity.ImageToUpload
import com.raveline.diarymongoapp.data.model.DiaryModel
import com.raveline.diarymongoapp.data.model.MongoDB
import com.raveline.diarymongoapp.data.model.Mood
import com.raveline.diarymongoapp.data.stateModel.GalleryImage
import com.raveline.diarymongoapp.data.stateModel.GalleryState
import com.raveline.diarymongoapp.data.stateModel.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mongodb.kbson.ObjectId
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class WriteViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val imagesUploadDao: ImagesUploadDao,
    private val imagesToDeleteDao: ImagesToDeleteDao
) : ViewModel() {

    private val TAG: String = WriteViewModel::class.java.simpleName

    val galleryState = GalleryState()

    var uiState by mutableStateOf(UiState())
        private set

    init {
        getDiaryIdArgument()
        fetchSelectedDiary()
    }

    private fun getDiaryIdArgument() {

        uiState = uiState.copy(
            selectedDiaryId = savedStateHandle.get<String>(
                key = WRITE_SCREEN_ARGUMENT_ID
            )
        )

    }

    private fun fetchSelectedDiary() = viewModelScope.launch {
        if (uiState.selectedDiaryId != null) {
            MongoDB.getSelectedDiary(
                diaryId = ObjectId(uiState.selectedDiaryId!!)
            ).catch {
                emit(RequestState.Error(Exception("Diary is already deleted.")))
            }.collect {
                if (it is RequestState.Success) {
                    val data = it.data
                    withContext(Main) {
                        setSelectedDiary(diaryModel = data)
                        setTitle(title = data.title)
                        setDescription(description = data.description)
                        setMood(Mood.valueOf(data.mood))

                        fetchImagesFromFirebase(
                            remoteImagePaths = data.images,
                            onImageDownload = { downloadedImage ->
                                galleryState.addImage(
                                    galleryImage = GalleryImage(
                                        image = downloadedImage,
                                        remoteImagePath = extractRemoteImagePath(
                                            remotePath = downloadedImage.toString()
                                        )
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    private fun extractRemoteImagePath(remotePath: String): String {
        val chunks = remotePath.split("%2F")
        val imageName = chunks[2].split("?").first()
        return "images/${Firebase.auth.currentUser?.uid}/$imageName"
    }

    fun updateDateTime(zonedDateTime: ZonedDateTime?) {
        uiState = if (zonedDateTime != null) {
            uiState.copy(updatedDateTime = zonedDateTime.toInstant()?.toRealmInstant())
        } else {
            uiState.copy(updatedDateTime = null)
        }
    }

    fun setTitle(title: String) {
        uiState = uiState.copy(
            title = title
        )
    }

    fun setDescription(description: String) {
        uiState = uiState.copy(
            description = description
        )
    }

    private fun setMood(mood: Mood) {
        uiState = uiState.copy(
            mood = mood
        )
    }

    private fun setSelectedDiary(diaryModel: DiaryModel) {
        uiState = uiState.copy(selectedDiary = diaryModel)
    }

    fun upsertDiary(
        diaryModel: DiaryModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch(IO) {
        if (uiState.selectedDiaryId != null) {

            updateSelectedDiary(
                diaryModel = diaryModel.apply {
                    // Get the id of the selected diary
                    _id = ObjectId(uiState.selectedDiaryId.toString())
                    date = uiState.selectedDiary!!.date
                },
                onSuccess = onSuccess,
                onError = onError
            )
        } else {
            insertDiary(
                diaryModel = diaryModel,
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    private fun insertDiary(
        diaryModel: DiaryModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch(IO) {

        when (val result = MongoDB.insertDiary(
            diaryModel = diaryModel.apply {
                // verify if user selected the date
                if (uiState.updatedDateTime != null) {
                    date = uiState.selectedDiary!!.date
                }
            }
        )
        ) {
            is RequestState.Success -> {
                uploadImagesToFirebase()
                withContext(Main) {
                    onSuccess()
                }
            }

            is RequestState.Error -> {
                withContext(Main) {
                    onError(result.error.message.toString())
                }
            }

            else -> {
                withContext(Main) {
                    onError("Something went wrong. Try again.")
                }
            }
        }

    }

    fun deleteDiary(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch(IO) {
        if (uiState.selectedDiaryId != null) {
            when (val result =
                MongoDB.deleteDiary(id = ObjectId(uiState.selectedDiaryId.toString()))
            ) {

                is RequestState.Success -> {
                    withContext(Main) {
                        uiState.selectedDiary?.let { deleteImageFromFirebase(it.images) }
                        onSuccess()
                    }
                }

                is RequestState.Error -> {
                    withContext(Main) {
                        onError(result.error.message.toString())
                    }
                }

                else -> {
                    withContext(Main) {
                        onError("Something went wrong. Try again.")
                    }
                }

            }
        }
    }

    private fun updateSelectedDiary(
        diaryModel: DiaryModel,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch(IO) {
        when (val result = MongoDB.updateDiary(
            diaryModel = diaryModel.apply {
                // verify if user selected the date
                date = if (uiState.updatedDateTime != null) {
                    uiState.updatedDateTime!!
                } else {
                    uiState.selectedDiary!!.date
                }
            })) {
            is RequestState.Success -> {
                uploadImagesToFirebase()
                withContext(Main) {
                    deleteImageFromFirebase()
                    onSuccess()
                }
            }

            is RequestState.Error -> {
                withContext(Main) {
                    onError(result.error.message.toString())
                }
            }

            else -> {
                withContext(Main) {
                    onError("Something went wrong. Try again.")
                }
            }
        }
    }

    fun addImage(imageUri: Uri, imageType: String) {

        val remoteImagePath = "images/${FirebaseAuth.getInstance().currentUser?.uid}/" +
                "${imageUri.lastPathSegment}-${System.currentTimeMillis()}.$imageType"

        Log.i(TAG, remoteImagePath)

        val galleryImage = GalleryImage(
            image = imageUri,
            remoteImagePath = remoteImagePath
        )

        galleryState.addImage(galleryImage = galleryImage)
    }

    private fun deleteImageFromFirebase(images: List<String>? = null) {
        val storage = FirebaseStorage.getInstance().reference

        if (images != null) {
            images.forEach { remotePath ->
                storage.child(remotePath).delete().addOnFailureListener { exception ->
                    viewModelScope.launch(IO) {
                        imagesToDeleteDao.insertImageToDelete(
                            ImageToDelete(remoteImagePath = remotePath)
                        )
                    }
                }
            }
        } else {
            //Delete images selected
            galleryState.imagesToBeDeleted.map {
                it.remoteImagePath
            }.forEach { remotePath ->
                storage.child(remotePath).delete().addOnFailureListener {
                    viewModelScope.launch(IO) {
                        imagesToDeleteDao.insertImageToDelete(
                            ImageToDelete(remoteImagePath = remotePath)
                        )
                    }
                }
            }

        }
    }

    private fun uploadImagesToFirebase() {
        val storage = FirebaseStorage.getInstance().reference
        galleryState.images.forEach { galleryImage ->
            val imagePath = storage.child(galleryImage.remoteImagePath)
            imagePath.putFile(galleryImage.image)
                .addOnProgressListener {
                    val sessionUri = it.uploadSessionUri
                    // Save image on database case something goes wrong
                    if (sessionUri != null) {
                        viewModelScope.launch(IO) {
                            imagesUploadDao.insertImageToUpload(
                                ImageToUpload(
                                    imageUri = galleryImage.image.toString(),
                                    remoteImagePath = galleryImage.remoteImagePath,
                                    sessionUri = sessionUri.toString()
                                )
                            )
                        }
                    } else {

                    }
                }
        }
    }

}

data class UiState(
    val selectedDiaryId: String? = null,
    val selectedDiary: DiaryModel? = null,
    val title: String = "",
    val description: String = "",
    val mood: Mood = Mood.Neutral,
    val updatedDateTime: RealmInstant? = null
)