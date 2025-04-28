package hoods.com.jetlogin.ui.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import hoods.com.jetlogin.R
import hoods.com.jetlogin.ui.components.HeaderText
import hoods.com.jetlogin.ui.components.LoginTextField
import hoods.com.jetlogin.ui.theme.JetLoginTheme
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import com.android.volley.Request
import com.google.firebase.auth.FirebaseAuth
import hoods.com.jetlogin.ui.signup.LoadingOverlay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.Response
import java.io.IOException

val defaultPadding = 16.dp
val itemSpacing = 8.dp

@Composable
fun LoginScreen(
    onLoginClick: (String) -> Unit,
    onSignUpClick: () -> Unit
) {
    val (userName, setUsername) = rememberSaveable { mutableStateOf("") }
    val (password, setPassword) = rememberSaveable { mutableStateOf("") }
    val (checked, onCheckedChange) = rememberSaveable { mutableStateOf(false) }

    // Only enable the login button if these fields are not empty
    val isFieldsNotEmpty = userName.isNotEmpty() && password.isNotEmpty()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isLoading by rememberSaveable { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(defaultPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderText(
            text = "Login",
            modifier = Modifier
                .padding(vertical = defaultPadding)
                .align(alignment = Alignment.Start)
        )

        // Username / Email field
        LoginTextField(
            value = userName,
            onValueChange = setUsername,
            labelText = "Email",
            leadingIcon = Icons.Default.Person,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email
        )
        Spacer(Modifier.height(itemSpacing))

        // Password field
        LoginTextField(
            value = password,
            onValueChange = setPassword,
            labelText = "Password",
            leadingIcon = Icons.Default.Lock,
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Password,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(itemSpacing))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = checked, onCheckedChange = onCheckedChange)
                Text("Remember me")
            }
            TextButton(onClick = {}) {
                Text("Forgot Password?")
            }
        }
        Spacer(Modifier.height(itemSpacing))

        // Login Button
        Button(
            onClick = {
                if (isLoading) return@Button
                isLoading = true
                scope.launch {
                    // Attempt Firebase Auth sign-in
                    Log.d(
                        "LoginScreen",
                        "Login button clicked. Email=$userName, Password=$password"
                    )
                    signInUser(
                        email = userName,
                        password = password,
                        onSuccess = {
                            // Only navigate if sign-in is successful
                            Log.d("LoginScreen", "signInUser onSuccess -> navigating to Home")
                            scope.launch {
                                delay(1000)
                                onLoginClick(userName)
                                isLoading = false           // or pass `email` if you want
                            }
                        },
                        onFailure = { errorMsg ->
                            isLoading = false
                            // If sign-in fails, show a toast
                            Log.e("LoginScreen", "signInUser onFailure: $errorMsg")
                            Toast.makeText(context, "Login failed: $errorMsg", Toast.LENGTH_LONG)
                                .show()
                        }
                    )
                }
            },
            enabled = isFieldsNotEmpty && !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        // Alternative Logins & Sign Up
        AlternativeLoginOptions(
            onIconClick = { index ->
                when (index) {
                    0 -> Toast.makeText(context, "Instagram Login Click", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(context, "Github Login Click", Toast.LENGTH_SHORT).show()
                    2 -> Toast.makeText(context, "Google Login Click", Toast.LENGTH_SHORT).show()
                }
            },
            onSignUpClick = onSignUpClick,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.BottomCenter)
        )
    }
    if (isLoading) {
        LoadingOverlay()
    }
}
}

@Composable
fun AlternativeLoginOptions(
    onIconClick: (index: Int) -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconList = listOf(
        R.drawable.icon_instagram,
        R.drawable.icon_github,
        R.drawable.icon_google,
    )
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Or Sign in With")
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            iconList.forEachIndexed { index, iconResId ->
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = "alternative Login",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable { onIconClick(index) }
                )
                Spacer(Modifier.width(defaultPadding))
            }
        }
        Spacer(Modifier.height(itemSpacing))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Don't have an Account?")
            Spacer(Modifier.height(itemSpacing))
            TextButton(onClick = onSignUpClick) {
                Text("Sign Up")
            }
        }
    }
}

// Actual sign-in function that uses Firebase
suspend fun signInUser(
    email: String,
    password: String,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    try {
        Log.d("signInUser", "Attempting sign-in with email=$email")
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user
        if (user != null) {
            Log.d("signInUser", "Sign-in success: uid=${user.uid}, email=${user.email}")
            onSuccess()
        } else {
            Log.e("signInUser", "Sign-in failed: user is null!")
            onFailure("Authentication returned null user.")
        }
    } catch (e: Exception) {
        Log.e("signInUser", "signInWithEmailAndPassword exception: ${e.localizedMessage}", e)
        onFailure(e.localizedMessage ?: "Unknown error")
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevLoginScreen() {
    // Provide stub lambdas for preview
    LoginScreen(
        onLoginClick = { /* No-op */ },
        onSignUpClick = { /* No-op */ }
    )
}
