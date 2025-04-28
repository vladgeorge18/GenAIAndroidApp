package hoods.com.jetlogin.ui.signup

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hoods.com.jetlogin.R
import hoods.com.jetlogin.data.UserProfile
import hoods.com.jetlogin.ui.components.HeaderText
import hoods.com.jetlogin.ui.components.LoginTextField
import hoods.com.jetlogin.ui.login.defaultPadding
import hoods.com.jetlogin.ui.login.signInUser
import hoods.com.jetlogin.ui.theme.JetLoginTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.size
import androidx.compose.ui.unit.dp

@Composable
fun SignUpScreen(
    onSignUpClick: (String) -> Unit,
    onLoginClick: () -> Unit,
    onPolicyClick: () -> Unit,
    onPrivacyClick: () -> Unit,
) {
    // State variables for the text fields
    val (firstName, onFirstNameChange) = rememberSaveable { mutableStateOf("") }
    val (lastName, onLastNameChange) = rememberSaveable { mutableStateOf("") }
    val (email, onEmailChange) = rememberSaveable { mutableStateOf("") }
    val (userName, onUserNameChange) = rememberSaveable { mutableStateOf("") }
    val (password, onPasswordChange) = rememberSaveable { mutableStateOf("") }
    val (confirmPassword, onConfirmPasswordChange) = rememberSaveable { mutableStateOf("") }
    val (agree, onAgreeChange) = rememberSaveable { mutableStateOf(false) }

    var isLoading by rememberSaveable { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            Log.d("DBG", "👤 currentUser = ${auth.currentUser?.email}")
        }
        FirebaseAuth.getInstance().addAuthStateListener(listener)
        onDispose { FirebaseAuth.getInstance().removeAuthStateListener(listener) }
    }

    // Check if password and confirmation do not match
    var passwordMismatch by remember { mutableStateOf(false) }

    // Single LocalContext instance
    val context = LocalContext.current

    // Check if required fields are not empty and terms are accepted
    val isFieldsNotEmpty = firstName.isNotEmpty() &&
            lastName.isNotEmpty() &&
            email.isNotEmpty() &&
            userName.isNotEmpty() &&
            password.isNotEmpty() &&
            confirmPassword.isNotEmpty() &&
            agree

    // 1) For showing snackbars in Material 3, use SnackbarHostState + Scaffold's snackbarHost param
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 2) Scaffold in Material 3
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { innerPadding ->
            // The innerPadding comes from Scaffold (to avoid overlapping the snackbar)
            Column(
                modifier = Modifier
                    .padding(innerPadding)       // respect the snackbar area
                    .padding(defaultPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Show an error message if passwords do not match
                AnimatedVisibility(visible = passwordMismatch) {
                    Text(
                        text = "Passwords do not match",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                HeaderText(
                    text = "Sign Up",
                    modifier = Modifier
                        .padding(vertical = defaultPadding)
                        .align(Alignment.Start)
                )

                LoginTextField(
                    value = firstName,
                    onValueChange = onFirstNameChange,
                    labelText = "First Name",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(defaultPadding))

                LoginTextField(
                    value = lastName,
                    onValueChange = onLastNameChange,
                    labelText = "Last Name",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(defaultPadding))

                LoginTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    labelText = "Email",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(defaultPadding))

                LoginTextField(
                    value = userName,
                    onValueChange = onUserNameChange,
                    labelText = "Username",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(defaultPadding))

                LoginTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    labelText = "Password",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Password
                )
                Spacer(Modifier.height(defaultPadding))

                LoginTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    labelText = "Confirm Password",
                    modifier = Modifier.fillMaxWidth(),
                    keyboardType = KeyboardType.Password
                )
                Spacer(Modifier.height(defaultPadding))

                // Terms and Conditions (Privacy and Policy) row
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val privacyText = "Privacy"
                    val policyText = "Policy"
                    val annotatedString = buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                            append("I Agree with")
                        }
                        append(" ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            pushStringAnnotation(tag = privacyText, annotation = privacyText)
                            append(privacyText)
                        }
                        append(" And ")
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            pushStringAnnotation(tag = policyText, annotation = policyText)
                            append(policyText)
                        }
                    }

                    Checkbox(checked = agree, onCheckedChange = onAgreeChange)
                    ClickableText(
                        text = annotatedString,
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(offset, offset).forEach {
                                when (it.tag) {
                                    privacyText -> {
                                        Toast.makeText(
                                            context,
                                            "Privacy Clicked",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onPrivacyClick()
                                    }

                                    policyText -> {
                                        Toast.makeText(
                                            context,
                                            "Policy Clicked",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onPolicyClick()
                                    }
                                }
                            }
                        }
                    )
                }

                Spacer(Modifier.height(defaultPadding + 8.dp))

                // Sign‑Up button
                Button(
                    onClick = {
                        if (isLoading) return@Button
                        Log.d("DBG", "🟢 Sign‑up button tapped")
                        passwordMismatch = password != confirmPassword
                        if (passwordMismatch) return@Button            // stop here if mismatch
                        isLoading = true
                        scope.launch {
                            createUser(

                                email = email,
                                password = password,
                                userProfile = UserProfile(
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    userName = userName
                                ),
                                onSuccess = {
                                    Log.d("DBG", "✅ createUser success")
                                    scope.launch {
                                    delay(500)
                                    onSignUpClick(userName)
                                    isLoading = false           // or pass `email` if you want
                                                  }
                                },
                                onFailure = { errorMsg ->
                                    isLoading = false
                                    Log.d("DBG", "❌ createUser failed: $errorMsg")
                                    Toast.makeText(context, "Failed: $errorMsg", Toast.LENGTH_LONG)
                                        .show()
                                }
                            )
                        }
                    },
                    enabled = isFieldsNotEmpty && !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Sign Up") }

                Spacer(Modifier.height(defaultPadding))

                // "Already have an account? Sign In"
                val signInTag = "Sign In"
                val signInAnnotation = buildAnnotatedString {
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                        append("Already have an account?")
                    }
                    append("  ")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        pushStringAnnotation(tag = signInTag, annotation = signInTag)
                        append(signInTag)
                    }
                }
                ClickableText(
                    text = signInAnnotation,
                    onClick = { offset ->
                        signInAnnotation.getStringAnnotations(offset, offset).forEach {
                            if (it.tag == signInTag) {
                                Toast.makeText(context, "Sign in Clicked", Toast.LENGTH_SHORT)
                                    .show()
                                onLoginClick()
                            }
                        }
                    }
                )
            }
        }
        if (isLoading) {
            LoadingOverlay()
        }
    }
}


@Composable
fun LoadingOverlay() {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.my_loading)
    )
    // loop forever:
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(120.dp)
        )
    }
}
/**
 * Helper function to create user + store data in Firestore.
 */
suspend fun createUser(
    email: String,
    password: String,
    userProfile: UserProfile,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    try {
        // 1) Create user in Firebase Auth
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val userId = result.user?.uid ?: throw Exception("UID is null")

        // 2) Store user data in Firestore -> /users/{uid}
        firestore.collection("users")
            .document(userId)
            .set(userProfile)
            .await()

        onSuccess()
    } catch (e: Exception) {
        onFailure(e.localizedMessage ?: "Unknown error")
    }
}

@Preview(showSystemUi = true)
@Composable
fun PrevSignUp() {
    JetLoginTheme {
        SignUpScreen({}, {}, {}, {})
    }
}











