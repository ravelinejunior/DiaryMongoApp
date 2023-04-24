package com.raveline.diarymongoapp.presentation.screens.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.raveline.diarymongoapp.R
import com.raveline.diarymongoapp.navigation.screens.Screens
import com.raveline.diarymongoapp.presentation.screens.login.CurvedShape
import com.raveline.diarymongoapp.presentation.viewmodel.SignUpViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignUpScreen(
    navController: NavHostController,
    onNavigateToLogin: () -> Unit
) {

    val viewModel: SignUpViewModel = viewModel()

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Create mutable state variables for the email and password fields
    val emailState = remember {
        mutableStateOf("")
    }
    val nameState = remember {
        mutableStateOf("")
    }
    val passwordState = remember {
        mutableStateOf("")
    }
    val confirmPasswordState = remember {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        item {
            Column {

                // Top curved shape
                CurvedShape(
                    Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.24f),
                    height = 100.dp,
                    curvature = 64.dp,
                    horizontalOffset = 32.dp
                )

                // Logo
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .padding(top = 64.dp)
                        .align(Alignment.CenterHorizontally)
                        .size(120.dp)
                )

                // Name text field
                TextField(
                    value = nameState.value,
                    onValueChange = {
                        nameState.value = it
                    },
                    placeholder = { Text("Name") },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Gray.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Unspecified,
                        disabledIndicatorColor = Color.Unspecified,
                        unfocusedIndicatorColor = Color.Unspecified,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            scope.launch {
                                scrollState.animateScrollTo(Int.MAX_VALUE)
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        }
                    ),
                    maxLines = 1,
                    singleLine = true
                )

                // Email text field
                TextField(
                    value = emailState.value,
                    onValueChange = {
                        emailState.value = it
                    },
                    placeholder = { Text("Email") },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Gray.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Unspecified,
                        disabledIndicatorColor = Color.Unspecified,
                        unfocusedIndicatorColor = Color.Unspecified,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            scope.launch {
                                scrollState.animateScrollTo(Int.MAX_VALUE)
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        }
                    ),
                    maxLines = 1,
                    singleLine = true
                )

                // Password text field
                TextField(
                    value = passwordState.value,
                    onValueChange = {
                        passwordState.value = it
                    },
                    placeholder = { Text("Password") },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Gray.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Unspecified,
                        disabledIndicatorColor = Color.Unspecified,
                        unfocusedIndicatorColor = Color.Unspecified,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            scope.launch {
                                scrollState.animateScrollTo(Int.MAX_VALUE)
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        }
                    ),
                    maxLines = 1,
                    singleLine = true
                )

                // Confirm Password text field
                TextField(
                    value = confirmPasswordState.value,
                    onValueChange = {
                        confirmPasswordState.value = it
                    },
                    placeholder = { Text("Confirm Password") },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Gray.copy(alpha = 0.25f), RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Unspecified,
                        disabledIndicatorColor = Color.Unspecified,
                        unfocusedIndicatorColor = Color.Unspecified,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            scope.launch {
                                scrollState.animateScrollTo(Int.MAX_VALUE)
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        }
                    ),
                    maxLines = 1,
                    singleLine = true
                )

                // Is loading
                if (viewModel.isLoading.value) {
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.onSurface,
                            strokeWidth = 8.dp
                        )
                    }
                }

                // Sign up button
                Button(
                    onClick = {
                        viewModel.signup(emailState.value, passwordState.value)
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                        .fillMaxWidth()
                ) {
                    Text("Sign up")
                }

                // Navigate to login screen text
                Text(
                    text = "Don't have an account? Sign up",
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            onNavigateToLogin()
                        }
                )

                // Validate user and go to home Screen
                if (viewModel.userSignedUp.value) {
                    navController.navigate(Screens.Home.route)
                }
            }
        }

    }


}