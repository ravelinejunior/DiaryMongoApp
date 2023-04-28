package com.raveline.diarymongoapp.presentation.screens.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.raveline.diarymongoapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    onValueEmailChange: (String) -> Unit,
    onValuePasswordChange: (String) -> Unit,
    onClick: () -> Unit,
    onNavigateToSignUp: () -> Unit,
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .navigationBarsPadding()
            .background(
                MaterialTheme.colorScheme.surface
            ),
        content = {
            item {
                Column {
                    // Top curved shape
                    CurvedShape(
                        Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surface,
                        height = 120.dp,
                        curvature = 120.dp,
                        horizontalOffset = 44.dp
                    )

                    // Logo
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .padding(top = 64.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    // Email text field
                    TextField(
                        value = "",
                        onValueChange = onValueEmailChange,
                        placeholder = { Text("Email") },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.dp,
                                Color.Gray.copy(alpha = 0.25f),
                                RoundedCornerShape(16.dp)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Unspecified,
                            disabledIndicatorColor = Color.Unspecified,
                            unfocusedIndicatorColor = Color.Unspecified,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.38f
                            )
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
                        value = "",
                        onValueChange = onValuePasswordChange,
                        placeholder = { Text("Password") },
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(
                                1.dp,
                                Color.Gray.copy(alpha = 0.25f),
                                RoundedCornerShape(16.dp)
                            ),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Unspecified,
                            disabledIndicatorColor = Color.Unspecified,
                            unfocusedIndicatorColor = Color.Unspecified,
                            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.38f
                            )
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

                    // Sign in button
                    Button(
                        onClick = onClick,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 32.dp)
                            .fillMaxWidth()
                    ) {
                        Text("Sign in")
                    }

                    // Navigate to signup screen text
                    Text(
                        text = "Already have an account? Sign in",
                        style = TextStyle(
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                onNavigateToSignUp()
                            }
                    )
                }

            }
        })


}

@Composable
fun CurvedShape(
    modifier: Modifier,
    color: Color,
    height: Dp,
    curvature: Dp,
    horizontalOffset: Dp
) {
    Box(
        modifier = modifier
            .height(height)
            .background(color = color)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawPath(
                Path().apply {
                    val topY = 0f
                    val bottomY = height.toPx()
                    val leftX = -curvature.toPx() - horizontalOffset.toPx()
                    val rightX = size.width + curvature.toPx() + horizontalOffset.toPx()
                    moveTo(leftX, topY)
                    cubicTo(
                        leftX, bottomY / 2,
                        rightX, bottomY / 2,
                        rightX, bottomY
                    )
                    lineTo(rightX, topY)
                    close()
                },
                color = color,
                style = Stroke(width = 0.3f)
            )
        }
    }
}