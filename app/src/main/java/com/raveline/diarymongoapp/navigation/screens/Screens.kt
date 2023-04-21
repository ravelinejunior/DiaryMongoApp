package com.raveline.diarymongoapp.navigation.screens

import com.raveline.diarymongoapp.common.utlis.Constants.AUTHENTICATION_SCREEN
import com.raveline.diarymongoapp.common.utlis.Constants.HOME_SCREEN
import com.raveline.diarymongoapp.common.utlis.Constants.HOME_SPLASH_SCREEN
import com.raveline.diarymongoapp.common.utlis.Constants.LOGIN_SCREEN
import com.raveline.diarymongoapp.common.utlis.Constants.SIGN_UP_SCREEN
import com.raveline.diarymongoapp.common.utlis.Constants.WRITE_SCREEN
import com.raveline.diarymongoapp.common.utlis.Constants.WRITE_SCREEN_ARGUMENT_ID

sealed class Screens(val route: String) {
    object Authentication : Screens(route = AUTHENTICATION_SCREEN)
    object SignUp : Screens(route = SIGN_UP_SCREEN)
    object Login : Screens(route = LOGIN_SCREEN)
    object HomeSplash : Screens(route = HOME_SPLASH_SCREEN)
    object Home : Screens(route = HOME_SCREEN)
    object Write :
        Screens(route = "$WRITE_SCREEN?$WRITE_SCREEN_ARGUMENT_ID={$WRITE_SCREEN_ARGUMENT_ID}") {
        fun passDiaryId(diaryId: String) =
            "$WRITE_SCREEN?$WRITE_SCREEN_ARGUMENT_ID=$diaryId"
    }

}
