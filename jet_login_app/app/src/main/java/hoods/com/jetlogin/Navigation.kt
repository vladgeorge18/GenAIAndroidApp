package hoods.com.jetlogin

import androidx.compose.runtime.Composable
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

sealed class Route {
    data class LoginScreen(val name: String = "Login") : Route()
    data class SignUpScreen(val name: String = "SignUp") : Route()
    data class PrivacyScreen(val name: String = "Privacy") : Route()
    data class PolicyScreen(val name: String = "Policy") : Route()
    //data class HomeScreen(val name: String = "Home") : Route()
    data class HomeScreen(val name: String = "Home/{username}") : Route() {
        fun createRoute(username: String): String = "Home/$username"
        }
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
                        navHostController.navigate(
                            Route.HomeScreen().createRoute(userName)
                        ){
                            popUpTo("login_flow")
                        }
                    },
                    onLoginClick = {
                        navHostController.navigateToSingleTop(
                            Route.LoginScreen().name
                        )
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
                userName = userName
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