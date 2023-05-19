package com.raveline.diarymongoapp.presentation.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.raveline.diarymongoapp.connectivity.ConnectivityObserver
import com.raveline.diarymongoapp.connectivity.NetworkConnectivityObserver
import com.raveline.diarymongoapp.data.database.dao.ImagesToDeleteDao
import com.raveline.diarymongoapp.data.database.entity.ImageToDelete
import com.raveline.diarymongoapp.data.model.MongoDB
import com.raveline.diarymongoapp.data.repository.Diaries
import com.raveline.diarymongoapp.data.stateModel.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkConnectivityObserver: NetworkConnectivityObserver,
    private val imagesToDeleteDao: ImagesToDeleteDao
) : ViewModel() {

    private var network by mutableStateOf(ConnectivityObserver.Status.Unavailable)

    var diaries: MutableState<Diaries> = mutableStateOf(RequestState.Idle)

    init {
        observeAllDiaries()
    }

    private fun observeAllDiaries() {
        viewModelScope.launch {
            MongoDB.getAllDiaries().collect { result ->
                diaries.value = result
            }
        }

        viewModelScope.launch {
            networkConnectivityObserver.observe().collect { connectivityStatus ->
                network = connectivityStatus
            }
        }
    }

    fun deleteAllDiaries(
        onSuccess: () -> Unit,
        onFailure: (error: Throwable) -> Unit
    ) {
        if (network == ConnectivityObserver.Status.Available) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val imagesDirectory = "images/${userId}"
            val storage = FirebaseStorage.getInstance().reference

            storage.child(imagesDirectory).listAll().addOnSuccessListener { listResult ->
                listResult.items.forEach { ref ->

                    val imagePath = "images/${userId}/${ref.name}"
                    storage.child(imagePath).delete()
                        .addOnFailureListener { _ ->
                            viewModelScope.launch(IO) {
                                imagesToDeleteDao.insertImageToDelete(
                                    ImageToDelete(
                                        remoteImagePath = imagePath
                                    )
                                )
                            }
                        }

                }

                viewModelScope.launch(IO) {
                    val result = MongoDB.deleteAllDiaries()
                    if (result is RequestState.Success) {
                        withContext(Main) {
                            onSuccess()
                        }
                    } else if (result is RequestState.Error) {
                        withContext(Main) {
                            onFailure(result.error)
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                onFailure(exception)
            }
        }else {
            onFailure(Exception("No Internet Connection!"))
        }
    }
}