package com.example.taskflow.ui.navigation

import LoginScreen
import RegisterScreen
import UserProfileScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.taskflow.ui.screens.admin.AdminPanelScreen
import com.example.taskflow.ui.screens.home.HomeScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit
) {
    NavHost(navController = navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(navController)
        }
        composable(Routes.REGISTER) {
            RegisterScreen(navController)
        }
        composable(Routes.HOME) {
            HomeScreen(navController)
        }
        composable(Routes.PROFILE) {
            UserProfileScreen(isDarkMode = isDarkMode, onToggleTheme = onToggleTheme)
        }
        composable(Routes.ADMIN) {
            AdminPanelScreen(navController)
        }
    }
}