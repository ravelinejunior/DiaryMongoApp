package com.raveline.diarymongoapp.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.raveline.diarymongoapp.common.utlis.Constants.MONGO_API_KEY
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class SignUpViewModel : ViewModel() {

    private val TAG: String = "SignUpViewModel"

    private val realmApp: App = App.create(MONGO_API_KEY)

    var userSignedUp = mutableStateOf(false)
        private set

    var isLoading = mutableStateOf(false)
        private set

    fun signup(email: String, password: String) = viewModelScope.launch {


        if (email.isNotEmpty().and(email.contains("@")) && password.isNotEmpty()) {

            isLoading.value = true

            try {

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { authResult ->
                        if (authResult.isSuccessful && authResult.isComplete) {
                            viewModelScope.launch(IO) {
                                //Create a condition to see if the user is created
                                realmApp.emailPasswordAuth.registerUser(
                                    email.trim(),
                                    password.trim()
                                )

                                // Login user after it
                                val user =
                                    realmApp.login(Credentials.emailPassword(email, password))
                                if (user.loggedIn) {
                                    userSignedUp.value = true
                                    Log.i(TAG, "Success Signup user. ")
                                } else {
                                    Log.e(TAG, "User could not signup")
                                }
                            }
                        } else {
                            Log.e(TAG, "Firebase user was not created")
                        }
                    }.addOnFailureListener { exception ->
                        Log.e(TAG, "Firebase user was not created. ${exception.message}")
                    }


            } catch (e: UserAlreadyExistsException) {
                Log.e(TAG, e.message.toString())
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }

            isLoading.value = false

        } else {
            //condition when is empty
        }
    }
}