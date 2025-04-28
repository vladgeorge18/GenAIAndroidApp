@file:OptIn(ExperimentalMaterial3Api::class)

package hoods.com.jetlogin.ui

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit
import kotlin.io.encoding.ExperimentalEncodingApi
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import hoods.com.jetlogin.ImageHolder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import coil.compose.rememberAsyncImagePainter
import java.io.ByteArrayOutputStream
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toArgb
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hoods.com.jetlogin.data.UserProfile
import kotlinx.coroutines.tasks.await


@Composable
fun HomeScreen(onLogoutClick: () -> Unit, welcomeMessage: String, userName: String, onEditImageClick: () -> Unit) {

    // Instead of just showing the userName, let's fetch the user profile from Firestore
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    // We'll store the fetched profile data in a state
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    var dynamicMessage: String by remember { mutableStateOf(welcomeMessage) }
    var showRephraseScreen by remember { mutableStateOf(false) } // Controls the visibility
    var showGenerateImageScreen by remember { mutableStateOf(false) }
    var showGenerateDescriptionScreen by remember { mutableStateOf(false) }
    var showGenerateHashtagsScreen by remember { mutableStateOf(false) }
    var showSketchScreen by remember { mutableStateOf(false) }

    val onFeatureClick: (String) -> Unit = { featureName ->
        if (featureName == "Rephrase Text") {
            showRephraseScreen = true
        }
        if (featureName == "Create an Image"){
            showGenerateImageScreen = true
        }
        if (featureName == "Edit Image"){
            onEditImageClick()
        }
        if (featureName == "Generate a Description"){
            showGenerateDescriptionScreen = true
        }
        if (featureName == "Generate Hashtags"){
            showGenerateHashtagsScreen = true
        }
        if (featureName == "Sketch to Image"){
            showSketchScreen = true
        }
    }
    // API Call to greet_user
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
        val apiUrl =
            "https://us-central1-speedy-web-456812-s7.cloudfunctions.net/greet_user?username=$userName"
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

    // Firestore fetch side-effect
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            scope.launch {
                try {
                    val docSnap = firestore.collection("users")
                        .document(currentUser.uid)
                        .get()
                        .await()
                    val profile = docSnap.toObject(UserProfile::class.java)
                    userProfile = profile
                } catch (e: Exception) {
                    Log.e("HomeScreen", "Error: ${e.message}", e)
                }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        Text(
            text = welcomeMessage,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )


        // Scrollable Feature List in the Center
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // If userProfile is loaded, show a small "Profile" section
            userProfile?.let { profile ->
                Text("Profile Info:")
                Text("First Name: ${profile.firstName}")
                Text("Last Name: ${profile.lastName}")
                Text("User Name: ${profile.userName}")
                Text("Email: ${profile.email}")
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(dummyFeatures) { feature ->
                    FeatureItem(feature, onFeatureClick)
                }
            }
        }
        Button(
            onClick = {
                // Sign out from Firebase
                FirebaseAuth.getInstance().signOut()
                onLogoutClick()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("Logout")
        }
    }
    if (showRephraseScreen) {
        // Create a persistent sheet state without the initialValue parameter.
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        // Ensure the bottom sheet is shown when the sheet appears.
        LaunchedEffect(Unit) {
            sheetState.show()
        }
        ModalBottomSheet(
            onDismissRequest = { showRephraseScreen = false },
            sheetState = sheetState,
        ) {
            RephraseScreen(onClose = { showRephraseScreen = false })
        }
    }

    if (showGenerateImageScreen) {
        // Create a persistent sheet state without the initialValue parameter
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        // Ensure the bottom sheet is shown when the sheet appears
        LaunchedEffect(Unit) {
            sheetState.show()
        }
        ModalBottomSheet(
            onDismissRequest = { showGenerateImageScreen = false },
            sheetState = sheetState,
        ) {
            GenerateImageScreen(
                onClose = { showGenerateImageScreen = false },
                onImageGenerated = {
                    showGenerateImageScreen = false
                    // Navigate to EditImageScreen.
                    onEditImageClick() })
        }
    }

    if (showGenerateDescriptionScreen) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        LaunchedEffect(Unit) {
            sheetState.show()
        }

        ModalBottomSheet(
            onDismissRequest = { showGenerateDescriptionScreen = false },
            sheetState = sheetState,
        ) {
            GenerateDescriptionScreen(
                onClose = { showGenerateDescriptionScreen = false }
            )
        }
    }

    if (showGenerateHashtagsScreen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showGenerateHashtagsScreen = false },
            sheetState = sheetState,
        ) {
            GenerateHashtagsScreen(onClose = { showGenerateHashtagsScreen = false })
        }
    }
    if (showSketchScreen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showSketchScreen = false },
            sheetState = sheetState
        ) {
            SketchScreen(
                onClose = { showSketchScreen = false }
            )
        }
    }


}
// Dummy Data for Features
data class Feature(val name: String, val icon: ImageVector)

val dummyFeatures = listOf(
    Feature("Rephrase Text", Icons.Default.Edit),
    Feature("Create an Image", Icons.Outlined.Image),
    Feature("Edit Image", Icons.Default.PhotoFilter),
    Feature("Generate a Description", Icons.Default.Description),
    Feature("Generate Hashtags", Icons.Default.LocalOffer),
    Feature("Sketch to Image", Icons.Default.Brush),

)

// Feature Item UI
@Composable
fun FeatureItem(feature: Feature, onFeatureClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable { onFeatureClick(feature.name) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = feature.icon,
            contentDescription = feature.name,
            modifier = Modifier.size(48.dp)
        )
        Text(text = feature.name, fontSize = 16.sp)
    }
}

@Composable
fun RephraseScreen(onClose: () -> Unit) {
    var inputText by remember { mutableStateOf("") }
    var rephrasedText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Dropdown states
    var expanded by remember { mutableStateOf(false) }
    var selectedStyle by remember { mutableStateOf("Instagram") }
    val styles = listOf("Instagram", "LinkedIn", "Facebook")
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.90f)
            .padding(16.dp)
    ) {
        // Close Button
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "X",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { onClose() }
                    .padding(8.dp),
                fontSize = 24.sp
            )
        }
        // Title
        Text("Rephrase Text", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        // Input Text Field with max height & scrolling
        Text("Enter text to rephrase:")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 200.dp) // Limits size but allows scrolling
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            TextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                maxLines = 6,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown for selecting rephrase style
        Text("Select rephrase style:")
        RephraseDropdown(selectedStyle = selectedStyle, onStyleSelected = { selectedStyle = it })

        Spacer(modifier = Modifier.height(16.dp))

        // Rephrase Button (disabled while loading)
        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    // Call  API
                    rephrasedText = fetchRephrasedText(inputText, selectedStyle)
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading // Disable the button when loading
        ) {
            Text("Rephrase")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Output Text Box
        if (rephrasedText.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp, max = 200.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = rephrasedText,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun RephraseDropdown(
    selectedStyle: String,
    onStyleSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val styles = listOf("Instagram", "LinkedIn", "Facebook")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        // Anchor TextField
        TextField(
            readOnly = true,
            value = selectedStyle,
            onValueChange = { },
            label = { Text("Rephrase Style") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            styles.forEach { style ->
                DropdownMenuItem(
                    onClick = {
                        onStyleSelected(style)
                        expanded = false
                    },
                    text = { Text(text = style) }
                )
            }
        }
    }
}


// Dummy rephrase function based on the selected style.
fun rephraseText(text: String, style: String): String {
    return when (style) {
        "Instagram" -> "Instagram-style: $text"
        "LinkedIn" -> "LinkedIn-style: $text"
        "Facebook" -> "Facebook-style: $text"
        else -> "Rephrased: $text"
    }
}


suspend fun fetchRephrasedText(text: String, platform: String): String =
    withContext(Dispatchers.IO) {
        try {
            // URL-encode the text
            val encodedText = URLEncoder.encode(text, "UTF-8")
            // Construct the URL (update with your Cloud Function's URL)
            val url = "https://us-central1-speedy-web-456812-s7.cloudfunctions.net/rephrase_text?text=$encodedText&platform=$platform"
            val client = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string() ?: ""
                val jsonObj = JSONObject(responseBody)
                jsonObj.optString("rephrased_text", "No text returned")
            } else {
                "Error: Code ${response.code}"
            }
        } catch (e: Exception) {
            "Exception: ${e.message}"
        }
    }


////

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun GenerateImageScreen(onClose: () -> Unit, onImageGenerated: (Bitmap) -> Unit) {
    var promptText by remember { mutableStateOf("") }
    var generatedImage by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.60f)
            .padding(16.dp)
    ) {
        // Close Button
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "X",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { onClose() }
                    .padding(8.dp),
                fontSize = 24.sp
            )
        }

        // Title
        Text("Generate Image", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Input Text Field for prompt
        Text("Enter prompt for image generation:")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 200.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .background(Color.White)
        ) {
            TextField(
                value = promptText,
                onValueChange = { promptText = it },
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                maxLines = 6,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Generate Button
        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    // Call real API function to fetch the image using the prompt text.
                    val image = fetchGeneratedImage(promptText)
                    isLoading = false
                    if (image != null) {
                        Log.d("GenerateImageScreen", "Real image generated: ${image.width}x${image.height}")
                        // Store the generated image in the singleton
                        ImageHolder.generatedImage = image
                        // Pass the generated image so that navigation can occur.
                        onImageGenerated(image)
                    } else {
                        Log.e("GenerateImageScreen", "Failed to generate image")

                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        // Display the generated image if available.
       // generatedImage?.let { image ->
       //     Image(
       //         bitmap = image.asImageBitmap(),
        //        contentDescription = "Generated Image",
        //        modifier = Modifier
        //            .fillMaxWidth()
        //            .height(300.dp)
        //    )
       // }
    }
}

suspend fun fetchGeneratedImage(prompt: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build()

                val encodedPrompt = URLEncoder.encode(prompt, "UTF-8")
                val url = "https://us-central1-speedy-web-456812-s7.cloudfunctions.net/generate_image_vertex?prompt=$encodedPrompt"
                val request = Request.Builder().url(url).build()
                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseBody = response.body?.string() ?: return@withContext null
                    val jsonObject = JSONObject(responseBody)
                    val base64Image = jsonObject.getString("image")
                    // Remove any data URI prefix if present.
                    val base64Data = if (base64Image.startsWith("data:image/png;base64,")) {
                        base64Image.substringAfter("data:image/png;base64,")
                    } else {
                        base64Image
                    }
                    val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                    BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenerateDescriptionScreen(
    onClose: () -> Unit
) {
    // Use rememberSaveable so that state persists correctly even when other fields change
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var imageDescription by rememberSaveable { mutableStateOf("") }
    var generatedCaption by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    // Dropdown states
    var selectedPlatform by rememberSaveable { mutableStateOf("Instagram") }
    var expandedPlatform by remember { mutableStateOf(false) }
    val platformOptions = listOf("Instagram", "LinkedIn", "Facebook")

    var selectedTarget by rememberSaveable { mutableStateOf("Casual users & lifestyle followers") }
    var expandedTarget by remember { mutableStateOf(false) }
    val targetOptions = listOf(
        "Casual users & lifestyle followers",
        "Influencers & content creators",
        "Gen Z & Millennials",
        "Photography & art lovers",
        "Brand & business marketing",
        "Corporate professionals",
        "Entrepreneurs & startup founders",
        "Tech enthusiasts & engineers",
        "Recruiters & job seekers",
        "Thought leaders & business executives",
        "Family & community-focused users",
        "Boomers & Gen X",
        "Hobbyists & interest groups"
    )

    var selectedHashtags by rememberSaveable { mutableStateOf("No") }
    var expandedHashtags by remember { mutableStateOf(false) }
    val hashtagsOptions = listOf("Yes", "No")

    // ActivityResultLauncher for picking images
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }
    val scrollState = rememberScrollState()
    val captionScrollState = rememberScrollState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Close button
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "X",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { onClose() }
                    .padding(8.dp),
                fontSize = 24.sp
            )
        }

        // Title
        Text(
            text = "Generate a Description",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to pick an image
        Button(
            onClick = { pickImageLauncher.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pick Image from Gallery")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Show selected image or a message if none selected
        if (selectedImageUri == null) {
            Text("No image selected.")
        } else {
            Image(
                painter = rememberAsyncImagePainter(selectedImageUri),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))



        // Platform dropdown
        Text("Select platform:")
        ExposedDropdownMenuBox(
            expanded = expandedPlatform,
            onExpandedChange = { expandedPlatform = !expandedPlatform },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = selectedPlatform,
                onValueChange = {},
                label = { Text("Platform") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPlatform)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedPlatform,
                onDismissRequest = { expandedPlatform = false }
            ) {
                platformOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedPlatform = option
                            expandedPlatform = false
                        },
                        text = { Text(text = option) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Target audience dropdown
        Text("Select target audience:")
        ExposedDropdownMenuBox(
            expanded = expandedTarget,
            onExpandedChange = { expandedTarget = !expandedTarget },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = selectedTarget,
                onValueChange = {},
                label = { Text("Target Audience") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTarget)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedTarget,
                onDismissRequest = { expandedTarget = false }

            ) {
                targetOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedTarget = option
                            expandedTarget = false
                        },
                        text = { Text(text = option) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hashtags dropdown
        Text("Include Hashtags?")
        ExposedDropdownMenuBox(
            expanded = expandedHashtags,
            onExpandedChange = { expandedHashtags = !expandedHashtags },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = selectedHashtags,
                onValueChange = {},
                label = { Text("Hashtags") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedHashtags)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedHashtags,
                onDismissRequest = { expandedHashtags = false }
            ) {
                hashtagsOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedHashtags = option
                            expandedHashtags = false
                        },
                        text = { Text(text = option) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Optional image description field
        Text("Provide context (optional):")
        TextField(
            value = imageDescription,
            onValueChange = { imageDescription = it },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp, max = 100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Generate button
        Button(
            onClick = {
                if (selectedImageUri != null) {
                    coroutineScope.launch {
                        isLoading = true
                        generatedCaption = fetchGeneratedCaption(
                            context = context,
                            imageUri = selectedImageUri!!,
                            platform = selectedPlatform,
                            target = selectedTarget,
                            hashtags = selectedHashtags,
                            imgDescription = imageDescription
                        )
                        isLoading = false
                    }
                } else {
                    // Optionally show a message if no image is selected
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Generate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Display the generated caption if available
        if (generatedCaption.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .verticalScroll(captionScrollState)
            ) {
                Text(text = generatedCaption, fontSize = 18.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GenerateHashtagsScreen(onClose: () -> Unit) {
    // Mode selection: "Text" or "Image"
    var mode by remember { mutableStateOf("Text") }
    var inputText by remember { mutableStateOf("") }
    var selectedImageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var selectedPlatform by rememberSaveable { mutableStateOf("Instagram") }
    var expandedPlatform by remember { mutableStateOf(false) }
    val platformOptions = listOf("Instagram", "LinkedIn", "Facebook")
    var selectedTarget by rememberSaveable { mutableStateOf("Gen Z") }
    var expandedTarget by remember { mutableStateOf(false) }
    val targetOptions = listOf("Gen Z", "Boomers", "Corporate", "Casual Audience")
    var generatedHashtags by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    // For image mode: launcher to pick an image from the gallery
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val resultScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Close Button
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "X",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { onClose() }
                    .padding(8.dp),
                fontSize = 24.sp
            )
        }
        // Title
        Text("Generate Hashtags", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        // Mode selection buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { mode = "Text" },
                colors = if (mode == "Text")
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                else ButtonDefaults.buttonColors()
            ) {
                Text("From Text")
            }
            Button(
                onClick = { mode = "Image" },
                colors = if (mode == "Image")
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                else ButtonDefaults.buttonColors()
            ) {
                Text("From Image")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (mode == "Text") {
            // Text input mode
            Text("Enter text to generate hashtags:")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp, max = 200.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .background(Color.White)
            ) {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    maxLines = 6,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Text)
                )
            }
        } else {
            // Image mode: show button to pick an image and display the image
            Button(
                onClick = { pickImageLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick Image from Gallery")
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (selectedImageUri == null) {
                Text("No image selected.")
            } else {
                Image(
                    painter = rememberAsyncImagePainter(selectedImageUri),
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Platform dropdown
        Text("Select platform:")
        ExposedDropdownMenuBox(
            expanded = expandedPlatform,
            onExpandedChange = { expandedPlatform = !expandedPlatform },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = selectedPlatform,
                onValueChange = {},
                label = { Text("Platform") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPlatform) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedPlatform,
                onDismissRequest = { expandedPlatform = false }
            ) {
                platformOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedPlatform = option
                            expandedPlatform = false
                        },
                        text = { Text(option) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Target audience dropdown
        Text("Select target audience:")
        ExposedDropdownMenuBox(
            expanded = expandedTarget,
            onExpandedChange = { expandedTarget = !expandedTarget },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                readOnly = true,
                value = selectedTarget,
                onValueChange = {},
                label = { Text("Target Audience") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTarget) },
                modifier = Modifier.fillMaxWidth().menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedTarget,
                onDismissRequest = { expandedTarget = false }
            ) {
                targetOptions.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            selectedTarget = option
                            expandedTarget = false
                        },
                        text = { Text(option) }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Apply Button to call backend
        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    generatedHashtags = if (mode == "Text") {
                        fetchGeneratedHashtagsFromText(inputText, selectedPlatform, selectedTarget)
                    } else {
                        if (selectedImageUri != null) {
                            fetchGeneratedHashtagsFromImage(context, selectedImageUri!!, selectedPlatform, selectedTarget)
                        } else {
                            "No image selected."
                        }
                    }
                    isLoading = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Apply")
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        // Display generated hashtags
        if (generatedHashtags.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp, max = 200.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
                    .verticalScroll(resultScrollState)
            ) {
                Text(text = generatedHashtags, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun SketchScreen(
    onClose: () -> Unit
) {
    val context = LocalContext.current
    // The user's current sketch or returned image
    var sketchBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // If the user hasn't drawn yet, initialize a blank bitmap.
    // We need real width/height in pixels. For simplicity, pick a fixed size or
    // wait for layout measurement. Below is a simple approach with a "remember" sized 512x512:
    if (sketchBitmap == null) {
        sketchBitmap = Bitmap.createBitmap(512, 700, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.White.toArgb()) // blank white background
        }
    }

    var brushMode by remember { mutableStateOf(true) } // pencil (true) or eraser (false)
    var promptText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxHeight(1f)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Close "X" in top-right corner
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "X",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .clickable { onClose() }
                    .padding(8.dp),
                fontSize = 24.sp
            )
        }

        // The column with canvas, brushes, prompt, and apply
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp), // leave space for "X"
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sketch an Image", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            // The Canvas area
            if (sketchBitmap != null) {
                DrawCanvas(
                    bitmap = sketchBitmap!!,
                    brushMode = brushMode
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons for pencil / eraser
            Row {
                Button(
                    onClick = { brushMode = true },
                    colors = if (brushMode)
                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    else
                        ButtonDefaults.buttonColors()
                ) {
                    Text("Pencil")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { brushMode = false },
                    colors = if (!brushMode)
                        ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    else
                        ButtonDefaults.buttonColors()
                ) {
                    Text("Eraser")
                }

                Button(
                    onClick = {
                        sketchBitmap?.let { bmp ->
                            val success = saveImageToGallery(context, bmp)
                            Toast
                                .makeText(
                                    context,
                                    if (success) "Saved to gallery" else "Failed to save",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
                ) {
                    Text("Save to Gallery")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Prompt text field
            TextField(
                value = promptText,
                onValueChange = { promptText = it },
                label = { Text("Prompt") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // "Apply" button
            Button(
                onClick = {
                    coroutineScope.launch {
                        if (sketchBitmap == null) return@launch
                        isLoading = true

                        // Send the user's sketch + prompt to your backend
                        val resultImage = fetchSketchToImage(sketchBitmap!!, promptText)

                        isLoading = false
                        resultImage?.let {
                            // Replace the local canvas with the returned image
                            sketchBitmap = it
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(.7f),
                enabled = !isLoading
            ) {
                Text("Apply")
            }

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }
    }
}


/**
 * A simple composable that draws [bitmap] on a Canvas,
 * allowing the user to draw or erase with gestures.
 */
@Composable
fun DrawCanvas(
    bitmap: Bitmap,
    brushMode: Boolean
) {
    // For simplicity, we'll define a fixed brush size
    val brushSizePx = 12f
    val paintBrush = remember {
        Paint().apply {
            color = android.graphics.Color.BLACK
            style = Paint.Style.STROKE
            strokeWidth = brushSizePx
            strokeCap = Paint.Cap.ROUND
        }
    }
    val paintEraser = remember {
        Paint().apply {
            color = android.graphics.Color.WHITE
            style = Paint.Style.STROKE
            strokeWidth = brushSizePx
            strokeCap = Paint.Cap.ROUND
            //xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }
    // This counter forces a re-render each time we draw on the bitmap
    var drawCounter by remember { mutableStateOf(0) }
    fun invalidateCanvas() { drawCounter++ }

    var lastPoint by remember { mutableStateOf<Offset?>(null) }
    val density = LocalDensity.current.density

    // We'll pick a size that matches the underlying bitmap aspect ratio
    val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .border(1.dp, Color.Gray)
            .pointerInput(brushMode) {
                detectDragGestures(
                    onDragStart = { offset ->
                        lastPoint = offset
                        // Draw a small circle at the start if wanted
                        val canvas = Canvas(bitmap)
                        val containerSize = Size(this.size.width.toFloat(), this.size.height.toFloat())
                        val (x, y) = offset.toPixelCoords(containerSize, bitmap, density)
                        canvas.drawCircle(
                            x,
                            y,
                            brushSizePx / 2,
                            if (brushMode) paintBrush else paintEraser
                        )
                    },
                    onDrag = { change, _ ->
                        val current = change.position
                        val canvas = Canvas(bitmap)
                        val containerSize = Size(this.size.width.toFloat(), this.size.height.toFloat())
                        lastPoint?.let { lp ->
                            val (startX, startY) = lp.toPixelCoords(containerSize, bitmap, density)
                            val (endX, endY) = current.toPixelCoords(containerSize, bitmap, density)
                            canvas.drawLine(
                                startX, startY,
                                endX, endY,
                                if (brushMode) paintBrush else paintEraser
                            )
                            // Force recompose so we see the updated bitmap
                            invalidateCanvas()
                        }
                        lastPoint = current
                    },
                    onDragEnd = {
                        lastPoint = null
                    }
                )
            }
    ) {
        val dummy = drawCounter
        // Show the updated bitmap
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = "Sketch Canvas",
            modifier = Modifier.fillMaxSize()
        )
    }
}

/**
 * Helper function to convert gesture coordinates (within the Composable)
 * into the corresponding pixel coordinates in the [bitmap].
 */
fun Offset.toPixelCoords(
    containerSize: Size,
    bitmap: Bitmap,
    density: Float
): Pair<Float, Float> {
    // containerSize is the Composable's size in px (not dp).
    val scaleX = bitmap.width / containerSize.width
    val scaleY = bitmap.height / containerSize.height
    val x = this.x * scaleX
    val y = this.y * scaleY
    return x to y
}

suspend fun fetchSketchToImage(sketch: Bitmap, prompt: String): Bitmap? = withContext(Dispatchers.IO) {
    try {
        // 1) Convert the user's drawing to PNG or JPEG bytes
        val stream = ByteArrayOutputStream()
        sketch.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val sketchBytes = stream.toByteArray()

        // 2) Build the multipart body
        val requestBody = sketchBytes.toRequestBody("image/png".toMediaTypeOrNull())
        val multipart = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("original", "sketch.png", requestBody)
            .addFormDataPart("prompt", prompt)
            .build()

        // 3) Construct the request to your "sketch to image" endpoint
        val request = Request.Builder()
            .url("https://us-central1-speedy-web-456812-s7.cloudfunctions.net/sketch2image")
            .post(multipart)
            .build()

        // 4) Make the call
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            // 5) Convert the response body back into a Bitmap
            response.body?.byteStream()?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } else {
            null
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



suspend fun fetchGeneratedCaption(
    context: Context,
    imageUri: Uri,
    platform: String,
    target: String,
    hashtags: String,
    imgDescription: String?
): String = withContext(Dispatchers.IO) {
    try {
        // Convert the Uri into a byte array
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bytes: ByteArray = inputStream?.readBytes() ?: return@withContext "Error reading image bytes."
        inputStream.close()

        // Build the multipart body
        val fileRequestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, bytes.size)
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "image.jpg", fileRequestBody)
            .addFormDataPart("platform", platform)
            .addFormDataPart("target", target)
            .addFormDataPart("hashtags", hashtags)
            .addFormDataPart("img_description", imgDescription ?: "")
            .build()

        val request = Request.Builder()
            .url("https://us-central1-speedy-web-456812-s7.cloudfunctions.net/generate_caption")
            .post(multipartBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: ""
            val jsonObj = JSONObject(responseBody)
            jsonObj.optString("caption", "No caption returned")
        } else {
            "Error: Code ${response.code} - ${response.message}"
        }
    } catch (e: Exception) {
        "Exception: ${e.message}"
    }
}

suspend fun fetchGeneratedHashtagsFromText(
    text: String,
    platform: String,
    target: String
): String = withContext(Dispatchers.IO) {
    try {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("text", text)
            .addFormDataPart("platform", platform)
            .addFormDataPart("target", target)
            .build()

        // Use the base endpoint URL without query parameters.
        val url = "https://us-central1-speedy-web-456812-s7.cloudfunctions.net/generate_hashtags_text"
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody) // Send a POST request with the form-data.
            .build()
        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: ""
            val jsonObj = JSONObject(responseBody)
            // Make sure the key here matches what your backend returns.
            jsonObj.optString("caption", "No hashtags returned")
        } else {
            "Error: Code ${response.code}"
        }
    } catch (e: Exception) {
        "Exception: ${e.message}"
    }
}


suspend fun fetchGeneratedHashtagsFromImage(
    context: Context,
    imageUri: Uri,
    platform: String,
    target: String
): String = withContext(Dispatchers.IO) {
    try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val bytes: ByteArray = inputStream?.readBytes() ?: return@withContext "Error reading image bytes."
        inputStream.close()
        val fileRequestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, bytes.size)
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "image.jpg", fileRequestBody)
            .addFormDataPart("platform", platform)
            .addFormDataPart("target", target)
            .build()
        // Replace with your backend endpoint for image-based hashtags generation.
        val request = Request.Builder()
            .url("https://us-central1-speedy-web-456812-s7.cloudfunctions.net/generate_hashtags_image")
            .post(multipartBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: ""
            val jsonObj = JSONObject(responseBody)
            jsonObj.optString("caption", "No hashtags returned")
        } else {
            "Error: Code ${response.code}"
        }
    } catch (e: Exception) {
        "Exception: ${e.message}"
    }
}
