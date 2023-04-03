package com.raveline.diarymongoapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raveline.diarymongoapp.common.utlis.Constants
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthenticationViewModel : ViewModel() {

    var authenticated = mutableStateOf(false)
    private set

    var loadingState = mutableStateOf(false)
        private set

    fun setLoading(loading: Boolean) {
        loadingState.value = loading
    }

    fun signInWithMongoAtlas(
        tokenId: String,
        onSuccess: (Boolean) -> Unit,
        onError: (Exception) -> Unit,

    ) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    App.create(Constants.MONGO_API_KEY)
                        .login(
                            Credentials.jwt(tokenId)
                            //Credentials.google(token = tokenId, GoogleAuthType.ID_TOKEN)
                        )
                        .loggedIn
                }
                withContext(Main) {
                    onSuccess(result)
                    delay(600)
                    authenticated.value = true
                }

            } catch (e: Exception) {
                withContext(Main) {
                    onError(e)
                }
            }
        }
    }

}