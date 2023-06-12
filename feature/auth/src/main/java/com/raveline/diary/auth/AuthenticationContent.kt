package com.raveline.diary.auth

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raveline.diary.ui.components.GoogleButton

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationContent(
    loadingState: Boolean,
    onButtonClicked: () -> Unit,
    onNavigateToLogin: () -> Unit,
) {

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .navigationBarsPadding(), content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .weight(9f)
                        .fillMaxWidth()
                        .padding(all = 40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .weight(weight = 10f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .size(120.dp)
                                .clickable {
                                    onNavigateToLogin()
                                },
                            painter = painterResource(id = R.drawable.google_logo),
                            contentDescription = stringResource(R.string.google_logo_desc)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.welcome_back_msg),
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Text(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                            text = stringResource(R.string.sign_in_continue_msg),
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    }

                    Column(
                        modifier = Modifier.weight(weight = 2f),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        GoogleButton(
                            onClick = onButtonClicked,
                            loadingState = loadingState
                        )
                    }
                }
            }
        })


}