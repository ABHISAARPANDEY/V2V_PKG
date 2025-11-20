package com.v2v.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.v2v.app.ui.screens.*
import com.v2v.app.viewmodel.AuthViewModel

@Composable
fun V2VNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val authState by authViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (authState.isLoggedIn) "main" else "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = { navController.navigate("main") { popUpTo("login") { inclusive = true } } }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onRegisterSuccess = { navController.navigate("main") { popUpTo("register") { inclusive = true } } }
            )
        }
        composable("main") {
            MainScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate("login") { popUpTo("main") { inclusive = true } }
                }
            )
        }
    }
}

