package com.labinot.bajrami.fitnessapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.labinot.bajrami.fitnessapp.domain.model.AuthStatus
import com.labinot.bajrami.fitnessapp.presentation.navigation.NavGraphSetup
import com.labinot.bajrami.fitnessapp.presentation.navigation.Routes
import com.labinot.bajrami.fitnessapp.presentation.screens.signIn.SignInViewModel
import com.labinot.bajrami.fitnessapp.ui.theme.FitnessAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            FitnessAppTheme {

                val windowSizeClass = calculateWindowSizeClass(activity = this)
                val snackbarHostState = remember { SnackbarHostState() }
                val navController = rememberNavController()
                val signInViewModel: SignInViewModel = hiltViewModel()

                val authStatus by signInViewModel.authStatus.collectAsStateWithLifecycle()
                var previousAuthStatus by rememberSaveable { mutableStateOf<AuthStatus?>(null) }

                LaunchedEffect(key1 = authStatus) {
                    if (previousAuthStatus != authStatus) {
                        when (authStatus) {
                            AuthStatus.AUTHORISED -> {
                                navController.navigate(Routes.DashboardScreen) { popUpTo(0) }
                              //  Log.d(APP_LOG, "authStatus: AUTHORISED; navigateToDashboard")
                            }

                            AuthStatus.UNAUTHORISED -> {
                                navController.navigate(Routes.SignInScreen) { popUpTo(0) }
                               // Log.d(APP_LOG, "authStatus: UNAUTHORISED; navigateToSignIn")
                            }

                            AuthStatus.LOADING -> {}
                        }
                        previousAuthStatus = authStatus
                    }
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState){

                            Snackbar(
                                snackbarData = it,
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )

                        }
                                   },
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) { paddingValues ->

                    NavGraphSetup(
                        navController = navController,
                        snackbarHostState = snackbarHostState,
                        windowSize = windowSizeClass.widthSizeClass,
                        paddingValues = paddingValues,
                        signInViewModel = signInViewModel
                    )


                }


            }
        }
    }
}