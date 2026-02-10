package com.example.joshcomposetesting

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// Sealed class to define the navigation routes for better type safety and organization.
sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object DetailScreen : Screen("detail_screen/{screenTitle}") {
        // Helper function to create the route with a specific title.
        fun createRoute(screenTitle: String) = "detail_screen/$screenTitle"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FourButtonAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

/**
 * The main composable that sets up the navigation controller and the NavHost.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.MainScreen.route) {
        // Route for the Main Screen
        composable(route = Screen.MainScreen.route) {
            MainScreen(navController = navController)
        }
        // Route for the Detail Screen, which accepts a title as an argument.
        composable(
            route = Screen.DetailScreen.route,
            arguments = listOf(navArgument("screenTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            // Retrieve the argument from the navigation back stack.
            val screenTitle = backStackEntry.arguments?.getString("screenTitle") ?: "Unknown Screen"
            DetailScreen(navController = navController, title = screenTitle)
        }
    }
}

/**
 * The main screen of the application, featuring four buttons for navigation.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Main Screen") },
                modifier = Modifier.testTag("home-screen-title"))
        },
        modifier = Modifier.semantics { testTagsAsResourceId = true }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag("welcome-text")
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Button to navigate to the first screen
            NavigationButton(
                text = "Go to Screen 1",
                onClick = {
                    navController.navigate(Screen.DetailScreen.createRoute("Screen 1"))
                },
                modifier = Modifier.testTag("screen1")
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Button to navigate to the second screen
            NavigationButton(
                text = "Go to Screen 2",
                onClick = {
                    navController.navigate(Screen.DetailScreen.createRoute("Screen 2"))
                },
                modifier = Modifier.testTag("screen2")
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Button to navigate to the third screen
            NavigationButton(
                text = "Go to Screen 3",
                onClick = {
                    navController.navigate(Screen.DetailScreen.createRoute("Screen 3"))
                },
                modifier = Modifier.testTag("screen3")
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Button to navigate to the fourth screen
            NavigationButton(
                text = "Go to Screen 4",
                onClick = {
                    navController.navigate(Screen.DetailScreen.createRoute("Screen 4"))
                },
                modifier = Modifier.testTag("screen4")
            )
        }
    }
}

/**
 * A reusable button composable for navigation.
 */
@Composable
fun NavigationButton(text: String, onClick: () -> Unit, modifier: Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.width(200.dp)
    ) {
        Text(text = text, fontSize = 16.sp)
    }
}


/**
 * A generic detail screen that displays a title and a back button.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, title: String) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                modifier = Modifier.testTag("detail-screen-title")
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "This is $title",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Navigate back to the previous screen in the back stack.
                navController.popBackStack()
            }, modifier = Modifier.testTag("go-back")) {
                Text("Go Back")
            }
        }
    }
}

// Dummy theme for previewing. In a real app, this would be in its own file.
@Composable
fun FourButtonAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    FourButtonAppTheme {
        // Preview requires a dummy NavController
        MainScreen(navController = rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    FourButtonAppTheme {
        DetailScreen(navController = rememberNavController(), title = "Preview Screen")
    }
}
