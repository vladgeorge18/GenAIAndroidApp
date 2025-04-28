package hoods.com.jetlogin

import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import hoods.com.jetlogin.ui.HomeScreen
import hoods.com.jetlogin.ui.login.LoginScreen
import hoods.com.jetlogin.ui.signup.PolicyScreen
import hoods.com.jetlogin.ui.signup.PrivacyScreen
import hoods.com.jetlogin.ui.signup.SignUpScreen
import hoods.com.jetlogin.ui.EditImageScreen
import hoods.com.jetlogin.ui.GenerateImageScreen

sealed class Route {
    data class LoginScreen(val name: String = "Login") : Route()
    data class SignUpScreen(val name: String = "SignUp") : Route()
    data class PrivacyScreen(val name: String = "Privacy") : Route()
    data class PolicyScreen(val name: String = "Policy") : Route()
    //data class HomeScreen(val name: String = "Home") : Route()
    data class HomeScreen(val name: String = "Home/{username}") : Route() {
        fun createRoute(username: String): String = "Home/$username"
        }
    data class EditImageScreen(val name: String = "EditImage") : Route()
    data class GenerateImageScreen(val name: String = "GenerateImage") : Route()
}


@Composable
fun MyNavigation(navHostController: NavHostController) {

    NavHost(
        navController = navHostController,
        startDestination = "login_flow",
    ) {
        navigation(startDestination = Route.LoginScreen().name, route = "login_flow") {
            composable(route = Route.LoginScreen().name) {
                LoginScreen(
                    onLoginClick = { userName ->
                        navHostController.navigate(
                            Route.HomeScreen().createRoute(userName)
                        ) {
                            popUpTo(route = "login_flow")
                        }
                    },
                    onSignUpClick = {
                        navHostController.navigateToSingleTop(
                            Route.SignUpScreen().name
                        )
                    }
                )
            }

            composable(route = Route.SignUpScreen().name) {

                SignUpScreen(
                    onSignUpClick = { userName ->
                        Log.d("DBG", "➡️ onSignUpClick – navigating to Home, arg=$userName")

                        Log.d("DBG", "🧭 navigate to => ${Route.HomeScreen().createRoute(userName)}")
                        navHostController.navigate(

                            Route.HomeScreen().createRoute(userName)
                        ) {

                            popUpTo(route = "login_flow")
                        }
                    },
//                    onLoginClick = {
//                        navHostController.navigateToSingleTop(
//                            Route.LoginScreen().name
//                        )
//                    },
                    onLoginClick = {
                        // Called after success or if user taps "Already have account? Sign In"
                        navHostController.navigate(Route.LoginScreen().name) {
                            // This ensures going back doesn't show SignUp again
                            popUpTo(Route.SignUpScreen().name) {
                                inclusive = true
                            }
                        }
                    },
                    onPrivacyClick = {
                        navHostController.navigate(
                            Route.PrivacyScreen().name
                        ){
                            launchSingleTop = true
                        }
                    },
                    onPolicyClick = {
                        navHostController.navigate(
                            Route.PolicyScreen().name
                        ){
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = Route.PolicyScreen().name) {
                PolicyScreen {
                    navHostController.navigateUp()
                }
            }
            composable(route = Route.PrivacyScreen().name) {
                PrivacyScreen {
                    navHostController.navigateUp()

                }
            }
        }
        /*composable(route = Route.HomeScreen().name) {
            HomeScreen(onLogoutClick = {
                navHostController.navigate(Route.LoginScreen().name) {
                    popUpTo(Route.HomeScreen().name) { inclusive = true }
                }
            }, welcomeMessage = "Welcome")
        }*/
        composable(
            route = Route.HomeScreen().name,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val userName = backStackEntry.arguments?.getString("username") ?: "Guest"
            HomeScreen(
                onLogoutClick = {
                    navHostController.navigate(Route.LoginScreen().name) {
                        popUpTo(Route.HomeScreen().name) { inclusive = true }
                    }
                },
                welcomeMessage = "Welcome to the Home Screen",
                userName = userName,
                onEditImageClick = {
                    // Navigate to the Edit Image Screen
                    navHostController.navigate(Route.EditImageScreen().name)
                }
                //onFeatureClick = {}
            )
        }
        composable(route = Route.GenerateImageScreen().name) {
            GenerateImageScreen(
                onClose = { navHostController.navigateUp() },
                onImageGenerated = { image ->
                    // ImageHolder is updated in the GenerateImageScreen already.
                    navHostController.navigate(Route.EditImageScreen().name)
                }
            )
        }
        composable(route = Route.EditImageScreen().name) {
            EditImageScreen(
                onBackClick = { navHostController.navigateUp() }
            )
        }
    }
}

fun NavController.navigateToSingleTop(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}