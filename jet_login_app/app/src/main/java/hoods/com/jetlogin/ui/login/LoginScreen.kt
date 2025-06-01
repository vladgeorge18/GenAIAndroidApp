package hoods.com.jetlogin.ui.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import hoods.com.jetlogin.ui.BottomWaveGradient
import hoods.com.jetlogin.ui.signup.LoadingOverlay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextDecoration
import okhttp3.Response
import java.io.IOException
/*
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
*/


val defaultPadding = 16.dp
val itemSpacing    = 8.dp

/* ───────── login screen ───────── */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LoginScreen(
    onLoginClick: (String) -> Unit,
    onSignUpClick: () -> Unit
) {
    /* state */
    var userName  by rememberSaveable { mutableStateOf("") }
    var password  by rememberSaveable { mutableStateOf("") }
    var rememberMe by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val ctx   = LocalContext.current
    val scope = rememberCoroutineScope()

    val enableLogin = userName.isNotBlank() && password.isNotBlank() && !isLoading

    /* typing gradient header */
    val fullHeader = "Login to your account"
    var typedHeader by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fullHeader.forEachIndexed { i, _ ->
            typedHeader = fullHeader.substring(0, i + 1)
            delay(45)
        }
    }
    val headerStyle = MaterialTheme.typography.headlineMedium.copy(
        brush = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary
            )
        ),
        fontWeight    = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    )

    /* backdrop waves (same helper you already have) */
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
    ) {
        BottomWaveGradient(
            modifier   = Modifier.matchParentSize(),
            bandHeight = 200.dp,
            fadeHeight = 100.dp,
            waveAmp    = 24.dp,
            waveLen    = 600.dp
        )

        /* main column */
        Column(
            Modifier
                .matchParentSize()
                .padding(defaultPadding)
        ) {

            /* header */
            Text(typedHeader, style = headerStyle)
            Spacer(Modifier.height(24.dp))

            /* email */
            //Text("Email", style = MaterialTheme.typography.labelMedium, color = Color.Gray, fontWeight = FontWeight.Bold)
            LoginTextField(
                value = userName,
                onValueChange = { userName = it },
                labelText = "Email",
                leadingIcon = Icons.Default.Person,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(itemSpacing))

            /* password */
            //Text("Password", style = MaterialTheme.typography.labelMedium, color = Color.Gray, fontWeight = FontWeight.Bold)
            LoginTextField(
                value = password,
                onValueChange = { password = it },
                labelText = "Password",
                leadingIcon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(itemSpacing))

            /* remember + forgot */
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(rememberMe, onCheckedChange = { rememberMe = it }, colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF7A73A5),              // fill when checked
                        uncheckedColor = Color(0xFFBDBDBD),            // outline when unchecked
                        checkmarkColor = Color.White,                  // the checkmark itself
                        disabledCheckedColor = Color(0xFFAAAAAA),      // fill when disabled + checked
                        disabledUncheckedColor = Color(0xFFEEEEEE),    // outline when disabled + unchecked
                        disabledIndeterminateColor = Color.Gray      // when disabled
                    ) )
                    Text("Remember me")
                }
                TextButton(onClick = { /* TODO forgot */ }) { Text("Forgot Password?", color = Color.Gray) }
            }

            Spacer(Modifier.height(80.dp))   // bottom FAB clearance

            /* alt logins + sign-up at bottom center */
            AlternativeLoginOptions(
                onIconClick   = { idx ->
                    val msg = when (idx) {
                        0 -> "Instagram login coming soon"
                        1 -> "Github login coming soon"
                        else -> "Google login coming soon"
                    }
                    Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show()
                },
                onSignUpClick = onSignUpClick,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.BottomCenter)
            )
        }

        /* floating LOGIN button */
        AnimatedVisibility(
            visible = enableLogin,
            enter   = scaleIn() + fadeIn(),
            exit    = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-24).dp, y = (-500).dp)
        ) {
            ExtendedFloatingActionButton(
                icon = {
                    if (isLoading)
                        CircularProgressIndicator(
                            Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )
                    else Icon(Icons.Default.Login, null)
                },
                text = { Text("Login") },
                onClick = {
                    if (isLoading) return@ExtendedFloatingActionButton
                    isLoading = true
                    scope.launch {
                        signInUser(
                            email = userName,
                            password = password,
                            onSuccess = {
                                onLoginClick(userName)
                                isLoading = false
                            },
                            onFailure = { err ->
                                isLoading = false
                                Toast.makeText(
                                    ctx,
                                    "Login failed: $err",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        )
                    }
                }
            )
        }

        /* optional sweeping loading overlay (same as GenerateImageScreen) */
        if (isLoading) {
            val sweep = rememberInfiniteTransition(label = "loginSweep")
            val animY by sweep.animateFloat(
                initialValue = -1f,
                targetValue  = 1f,
                animationSpec = infiniteRepeatable(
                    tween(1600, easing = LinearEasing),
                    RepeatMode.Restart
                ),
                label = "offset"
            )
            val fade = Brush.verticalGradient(
                0f to Color.Transparent,
                .3f to Color.White.copy(alpha = .55f),
                .7f to Color.White.copy(alpha = .55f),
                1f to Color.Transparent
            )
            Box(
                Modifier
                    .matchParentSize()
                    .graphicsLayer { translationY = animY * size.height }
                    .background(fade)
            )
        }
    }
}

/* ───────── unchanged helper composables ───────── */

@Composable
fun LoginTextField(
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(labelText) },
        leadingIcon = { Icon(leadingIcon, null) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        visualTransformation = visualTransformation,
        shape = MaterialTheme.shapes.medium,
        singleLine = keyboardType != KeyboardType.Password,
        modifier = modifier
    )
}

@Composable
fun AlternativeLoginOptions(
    onIconClick: (Int) -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconList = listOf(
        R.drawable.icon_instagram,
        R.drawable.icon_github,
        R.drawable.icon_google,
    )
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Or sign in with", color = Color.White) // , fontWeight = FontWeight.Bold
        Spacer(Modifier.height(itemSpacing))
        Row {
            iconList.forEachIndexed { idx, res ->
                Icon(
                    painter = painterResource(res),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable { onIconClick(idx) }

                )
                if (idx < iconList.lastIndex) Spacer(Modifier.width(defaultPadding))
            }
        }
        Spacer(Modifier.height(itemSpacing))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don’t have an account?", color = Color.White) // fontWeight = FontWeight.Bold
            TextButton(onClick = onSignUpClick) { Text("Sign up", color = Color.White, textDecoration = TextDecoration.Underline) }
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
