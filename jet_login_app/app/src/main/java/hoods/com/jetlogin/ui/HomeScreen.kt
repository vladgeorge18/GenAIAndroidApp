package hoods.com.jetlogin.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import hoods.com.jetlogin.Route
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request


@Composable
fun HomeScreen(onLogoutClick: () -> Unit, welcomeMessage: String, userName: String) {
    val context = LocalContext.current
    var dynamicMessage: String by remember { mutableStateOf(welcomeMessage) }

    // API Call to greet_user
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
        val apiUrl =
            "https://europe-central2-dummyproject-447312.cloudfunctions.net/greet_user?username=$userName"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .build()
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: "Failed to parse response"
                dynamicMessage = responseBody
            } else {
                dynamicMessage = "Failed to fetch welcome message1."
            }
        } catch (e: Exception) {
            dynamicMessage = "Failed to fetch welcome message2."
            Log.e("HomeScreen", "Exception: ${e.message}", e)
        }
    }
}
    Box(Modifier.fillMaxSize()) {
        Text(
            text = dynamicMessage,
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Logout")
        }
    }
}