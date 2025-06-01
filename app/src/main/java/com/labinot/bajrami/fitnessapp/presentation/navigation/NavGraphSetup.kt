package com.labinot.bajrami.fitnessapp.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.toRoute
import com.example.measuremate.presentation.details.DetailsEvent
import com.labinot.bajrami.fitnessapp.presentation.screens.addItem.AddItemScreen
import com.labinot.bajrami.fitnessapp.presentation.screens.addItem.AddItemViewModel
import com.labinot.bajrami.fitnessapp.presentation.screens.dashboard.DashBoardScreen
import com.labinot.bajrami.fitnessapp.presentation.screens.dashboard.DashboardViewModel
import com.labinot.bajrami.fitnessapp.presentation.screens.details.DetailScreen
import com.labinot.bajrami.fitnessapp.presentation.screens.details.DetailViewModel
import com.labinot.bajrami.fitnessapp.presentation.screens.signIn.SignInScreen
import com.labinot.bajrami.fitnessapp.presentation.screens.signIn.SignInViewModel
import com.labinot.bajrami.fitnessapp.presentation.util.UiEvent

@Composable
fun NavGraphSetup(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    windowSize: WindowWidthSizeClass,
    paddingValues: PaddingValues,
    signInViewModel: SignInViewModel
) {

    LaunchedEffect(key1 = Unit) {

        signInViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Snackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                UiEvent.HideBottomSheet -> {}
                UiEvent.Navigate -> {}
            }
        }

    }


    NavHost(
        modifier = Modifier.padding(paddingValues),
        navController = navController,
        startDestination = Routes.DashboardScreen)
    {

        composable<Routes.SignInScreen> {



            val state by signInViewModel.state.collectAsStateWithLifecycle()


            SignInScreen(windowSize = windowSize,
                state = state,
                onEvent = signInViewModel::onEven,
                )


        }


        composable<Routes.DashboardScreen> {

            val viewModel: DashboardViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            DashBoardScreen(
                snackbarHostState = snackbarHostState,
                state = state,
                onFabClicked = {navController.navigate(Routes.AddItemScreen)},
                onItemCardClick = { bodyPartId ->

                    navController.navigate(Routes.DetailsScreen(bodyPartId))

                },
                onEvent = viewModel::onEvent,
                uiEvent = viewModel.uiEvent,
            )


        }

        composable<Routes.AddItemScreen>(

            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }


        ){

            val viewModel: AddItemViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            AddItemScreen(
                snackbarHostState = snackbarHostState,
                state = state,
                uiEvent = viewModel.uiEvent,
                onEvent = viewModel::onEvent,
                onBackIconClick = {
                    navController.navigateUp()
                }
            )


        }

        composable<Routes.DetailsScreen>(

            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(durationMillis = 500),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }

        ){

            val viewModel: DetailViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            DetailScreen(
                windowSizeClass = windowSize,
                snackbarHostState = snackbarHostState,
                state = state,
                onEvent = viewModel::onEvents,
                uiEvent = viewModel.uiEvent,
                onBackIconClick = {navController.navigateUp()})

        }


    }




}