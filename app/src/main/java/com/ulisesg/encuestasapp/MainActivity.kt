package com.ulisesg.encuestasapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ulisesg.encuestasapp.freature.presentation.screens.CreatePollScreen
import com.ulisesg.encuestasapp.freature.presentation.screens.SurveysListScreen
import com.ulisesg.encuestasapp.ui.theme.EncuestasAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EncuestasAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    
                    NavHost(
                        navController = navController,
                        startDestination = "list"
                    ) {
                        composable("list") {
                            SurveysListScreen(navController = navController)
                        }
                        composable("create_poll") {
                            CreatePollScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
