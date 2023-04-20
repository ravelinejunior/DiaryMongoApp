package com.raveline.diarymongoapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.raveline.diarymongoapp.R
import com.raveline.diarymongoapp.common.utlis.RequestState
import com.raveline.diarymongoapp.data.repository.Diaries

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    diaries: Diaries,
    onMenuClicked: () -> Unit,
    navigateToWrite: () -> Unit,
    navigateToWriteWithArgs: (String) -> Unit,
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit
) {

    // Control padding of fab to not cause overlap
    var padding by remember {
        mutableStateOf(PaddingValues())
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    NavigationDrawer(drawerState = drawerState, onSignOutClicked = onSignOutClicked) {
        Scaffold(
            modifier = Modifier.nestedScroll(
                scrollBehavior.nestedScrollConnection
            ),
            topBar = {
                HomeTopBar(scrollBehavior = scrollBehavior, onMenuClicked = onMenuClicked)
            },
            content = {

                padding = it

                when (diaries) {
                    is RequestState.Idle,
                    is RequestState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(100.dp),
                                strokeWidth = 8.dp
                            )
                        }
                    }

                    is RequestState.Success -> {
                        HomeContent(
                            diaryNotes = diaries.data,
                            onClick = navigateToWriteWithArgs,
                            paddingValues = it
                        )
                    }

                    is RequestState.Error -> {
                        EmptyPage(
                            title = stringResource(R.string.something_went_wrong_str),
                            subtitle = stringResource(R.string.internet_connection_verify_str)
                        )
                    }

                }


            },
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(end = padding.calculateEndPadding(LayoutDirection.Ltr)),
                    onClick = navigateToWrite
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.new_diary_icon_content_msg)
                    )
                }
            }
        )
    }
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState,
    onSignOutClicked: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Image(
                    modifier = Modifier
                        .width(160.dp)
                        .height(160.dp)
                        .align(Alignment.CenterHorizontally),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(
                        id = R.string.google_logo_desc,
                    )
                )

                NavigationDrawerItem(
                    label = {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = stringResource(
                                    id = R.string.google_logo_desc
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = stringResource(R.string.sign_out_str))
                        }
                    },
                    selected = false,
                    onClick = onSignOutClicked,
                )
            }
        },
        content = content,
    )
}









