package com.labinot.bajrami.fitnessapp.presentation.screens.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.labinot.bajrami.fitnessapp.domain.model.BodyPart
import com.labinot.bajrami.fitnessapp.presentation.components.FitnessAppDialog
import com.labinot.bajrami.fitnessapp.presentation.components.ProfileBottomSheet
import com.labinot.bajrami.fitnessapp.presentation.components.ProfilePicPlaceholder
import com.labinot.bajrami.fitnessapp.presentation.util.UiEvent
import com.labinot.bajrami.fitnessapp.ui.theme.FitnessAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    snackbarHostState: SnackbarHostState,
    state: DashboardState,
    onFabClicked: () -> Unit,
    onItemCardClick: (String) -> Unit,
    onEvent: (DashboardEvent) -> Unit,
    uiEvent: Flow<UiEvent>,

    ){

    var isProfileBottomSheetOpen by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    LaunchedEffect(key1 = Unit) {
        uiEvent.collect { event ->
            when (event) {
                is UiEvent.Snackbar -> {

                    snackbarHostState.showSnackbar(event.message)

                }

                UiEvent.HideBottomSheet -> {

                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) isProfileBottomSheetOpen = false
                    }
                }
                UiEvent.Navigate -> {
                }
            }
        }
    }



    val context = LocalContext.current


    var isSignOutDialogOpen by rememberSaveable { mutableStateOf(false) }
    FitnessAppDialog(
        isOpen = isSignOutDialogOpen,
        title = "Sign Out",
        body = { Text(text = "Are you sure, you want to sign Out?") },
        onDialogDismiss = { isSignOutDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(DashboardEvent.SignOut)
            isSignOutDialogOpen = false
        }
    )

    val isUserAnonymous = state.user?.isAnonymous ?:true
    ProfileBottomSheet(
        isOpen = isProfileBottomSheetOpen,
        sheetState = bottomSheetState,
         user = state.user,
        buttonLoadingState = if (isUserAnonymous) state.isSignInButtonLoading else state.isSignOutButtonLoading,
        buttonPrimaryText = if (isUserAnonymous) "Sign in with Google" else "Sign out with Google",
        onBottomSheetDismiss = { isProfileBottomSheetOpen = false },
        onGoogleButtonClick = {


            if (isUserAnonymous) onEvent(DashboardEvent.AnonymousUserSignInWithGoogle(context))
            else isSignOutDialogOpen = true



        }
    )

    Box(modifier = Modifier.fillMaxSize())
    {

        Column(modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top)
        {

            DashBoardTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                onProfileClick = {

                 isProfileBottomSheetOpen = true
                },
                profilePicUrl = state.user?.profilePictureUrl
            )

            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 300.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {

                items(items = state.bodyParts) { bodyPart ->

                    AnimatedVisibility(
                        modifier = Modifier.animateItem(
                            fadeInSpec = null,
                            fadeOutSpec = null,
                            placementSpec = tween(100)
                        ),
                        visible = visible,
                        enter = slideInVertically(
                            initialOffsetY = { -it },
                            animationSpec = tween(durationMillis = 300)
                        ) + fadeIn(animationSpec = tween(durationMillis = 300))
                    ) {

                        ItemCard(bodyPart = bodyPart,
                            onItemCardClick = onItemCardClick)

                    }



                }


            }

        }


        FloatingActionButton(
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(20.dp),
            onClick = {

                onFabClicked()
            })
        {

            Icon(imageVector = Icons.Default.Add,
                contentDescription = "Add Icon")

        }

    }




}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashBoardTopAppBar(
    modifier: Modifier = Modifier,
    onProfileClick:() -> Unit,
    profilePicUrl: String?,
){

    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0, 0, 0, 0),
        title = {
            Text(text = "Fitness App")
        },
        actions = {

            IconButton(onClick = {
                onProfileClick()
            }) {

                ProfilePicPlaceholder(
                    placeholderSize = 30.dp,
                    borderWidth = 1.dp,
                    profilePictureUrl = profilePicUrl,
                    padding = 2.dp
                )

            }




        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemCard(
    modifier: Modifier = Modifier,
    bodyPart: BodyPart,
    onItemCardClick:(String) -> Unit
){




    Card(
        modifier = modifier,
        onClick = { bodyPart.bodyPartId?.let { onItemCardClick(it) } })
    {

        Row (modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically) {

            Text(
                modifier = Modifier.weight(8f),
                text = bodyPart.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis)

            Spacer(modifier = Modifier.weight(1f))

            Text(text = "${bodyPart.latestValue ?:""} ${bodyPart.measuringUnit}",
                style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.width(10.dp))

            Box(modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(4.dp)
            ){

                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Arrow Forward",
                    modifier = Modifier.size(15.dp)
                    )


            }


        }


    }



}


@PreviewScreenSizes
@Composable
private fun DashBoardPreview(){

    FitnessAppTheme {

        DashBoardScreen(
            snackbarHostState =  remember { SnackbarHostState()},
            state = DashboardState(),
            onFabClicked = {},
            onItemCardClick = { string ->


            },
            onEvent = {},
            uiEvent = flowOf()
        )

    }


}


