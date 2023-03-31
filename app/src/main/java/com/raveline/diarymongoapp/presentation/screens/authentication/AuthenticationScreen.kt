package com.raveline.diarymongoapp.presentation.screens.authentication

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.raveline.diarymongoapp.common.utlis.Constants
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.MessageBarState
import com.stevdzasan.onetap.OneTapSignInState
import com.stevdzasan.onetap.OneTapSignInWithGoogle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AuthenticationScreen(
    loadingState: Boolean,
    onButtonClicked: () -> Unit,
    oneTapSignInState: OneTapSignInState,
    messageBarState: MessageBarState,
) {

    Scaffold(content = {
        ContentWithMessageBar(messageBarState = messageBarState) {
            AuthenticationContent(
                loadingState = loadingState,
                onButtonClicked = onButtonClicked
            )
        }
    })

    OneTapSignInWithGoogle(
        state = oneTapSignInState,
        clientId = Constants.MONGO_SERVER_CLIENT_ID,
        onTokenIdReceived = { tokenId ->
            Log.i("AuthenticationScreen", "TokenId = $tokenId")
            messageBarState.addSuccess("Successfully signed in!")
        },
        onDialogDismissed = { message ->
            Log.i("AuthenticationScreen", "Message = $message")
            messageBarState.addError(Exception(message))
        }
    )

}