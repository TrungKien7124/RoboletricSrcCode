package com.example.roboletric

import android.content.BroadcastReceiver
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.roboletric.ui.theme.RoboletricTheme

class MainActivity : ComponentActivity() {
    var receiver: Receiver ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoboletricTheme {
                MainScreen()
            }
        }
        receiver = Receiver()
        registerReceiver(receiver, null)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
        receiver = null
    }
}

enum class ScreenEnum {
    FIRST, SECOND
}

@Composable
fun MainScreen() {
    var text by remember { mutableStateOf("") }
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ScreenEnum.valueOf(
        backStackEntry?.destination?.route ?: ScreenEnum.FIRST.name
    )
    Scaffold(
        topBar = { TopBar(
            navigateUp = { navController.navigateUp() },
            canNavigateBack = navController.previousBackStackEntry != null,
        ) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScreenEnum.FIRST.name,
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = ScreenEnum.FIRST.name) {
                FirstScreen(
                    onClick = { navController.navigate(ScreenEnum.SECOND.name) }
                )
            }
            composable(route = ScreenEnum.SECOND.name) {
                SecondScreen(
                    text = text,
                    onTextChange = {it -> text = it}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                text = "Roboletric UI Testing",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}