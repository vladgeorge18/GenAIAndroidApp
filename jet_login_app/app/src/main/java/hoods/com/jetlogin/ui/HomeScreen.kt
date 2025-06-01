@file:OptIn(ExperimentalMaterial3Api::class)

package hoods.com.jetlogin.ui
import androidx.compose.ui.text.AnnotatedString
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



import androidx.compose.foundation.ExperimentalFoundationApi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items

import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Brush


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
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
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState

import androidx.compose.foundation.horizontalScroll
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoFixOff
import androidx.compose.material.icons.filled.Check

import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.AssistChipDefaults.assistChipBorder
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Dialog
import androidx.core.net.toUri
import okio.BufferedSink
import okio.source


import coil.compose.AsyncImage
import coil.request.ImageRequest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import java.io.File
import kotlin.math.roundToInt

import androidx.compose.material.icons.filled.Brush        // “pencil”
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.ui.unit.Dp
import kotlin.math.PI
import kotlin.math.sin

import androidx.compose.foundation.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.Path  as ComposePath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.IntSize

// -----------------------------------------------------------------------------
// MAIN COMPOSABLE – updated "HomeScreen" that keeps *all* your existing logic
// while giving it a fresh Material‑3 look & feel.
// -----------------------------------------------------------------------------

// -----------------------------------------------------------------------------
//  HOME SCREEN – now with typing animation: "Welcome <first name>"
// -----------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onLogoutClick: () -> Unit,
    welcomeMessage: String, // still here in case you want to fall back
    userName: String,
    onEditImageClick: () -> Unit,
) {
    // ---------------------------------------------------------------------
    // Firebase + state
    // ---------------------------------------------------------------------
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    val scope = rememberCoroutineScope()

    // Typing‑effect state
    var typedGreeting by remember { mutableStateOf("") }

    // Sheet visibility
    var showRephraseScreen by remember { mutableStateOf(false) }
    var showGenerateImageScreen by remember { mutableStateOf(false) }
    var showGenerateDescriptionScreen by remember { mutableStateOf(false) }
    var showGenerateHashtagsScreen by remember { mutableStateOf(false) }
    var showSketchScreen by remember { mutableStateOf(false) }
    var showStyleTransferScreen by remember { mutableStateOf(false) }

    val onFeatureClick: (String) -> Unit = { name ->
        when (name) {
            "Rephrase Text" -> showRephraseScreen = true
            "Create an Image" -> showGenerateImageScreen = true
            "Edit Image" -> onEditImageClick()
            "Generate a Description" -> showGenerateDescriptionScreen = true
            "Generate Hashtags" -> showGenerateHashtagsScreen = true
            "Sketch to Image" -> showSketchScreen = true
            "Style Change" -> showStyleTransferScreen = true
        }
    }

    // ---------------------------------------------------------------------
    // SIDE‑EFFECTS
    // ---------------------------------------------------------------------
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            scope.launch {
                try {
                    val snap = firestore.collection("users").document(currentUser.uid).get().await()
                    userProfile = snap.toObject(UserProfile::class.java)
                } catch (e: Exception) {
                    Log.e("HomeScreen", "Profile fetch error", e)
                }
            }
        }
    }

    // Trigger typing effect when profile is ready
    LaunchedEffect(userProfile?.firstName) {
        userProfile?.let { profile ->
            val fullText = "Welcome, ${profile.firstName}!"
            typedGreeting = ""
            fullText.forEachIndexed { index, _ ->
                typedGreeting = fullText.substring(0, index + 1)
                delay(45) // speed of the typing
            }
        }
    }

    // ---------------------------------------------------------------------
    // UI – Scaffold with gradient, TopAppBar, Profile header & Feature grid
    // ---------------------------------------------------------------------
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = .35f),
            MaterialTheme.colorScheme.surface
        )
    )

    Scaffold(
        modifier = Modifier.background(gradient),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (typedGreeting.isNotBlank()) typedGreeting else "Welcome",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                actions = {
                    IconButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogoutClick()
                    }) { Icon(Icons.Default.ExitToApp, contentDescription = "Logout") }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // 1️⃣ PROFILE HEADER ---------------------------------------------------
            userProfile?.let { ProfileHeader(it) }

            // 2️⃣ FEATURE GRID ----------------------------------------------------
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(dummyFeatures, key = { it.name }) { feature ->
                    FeatureCard(feature) { onFeatureClick(feature.name) }
                }
            }
        }
    }

    // ---------------------------------------------------------------------
    // BOTTOM‑SHEETS (unchanged – just moved below Scaffold)
    // ---------------------------------------------------------------------
    if (showRephraseScreen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showRephraseScreen = false },
            sheetState = sheetState
        ) { RephraseScreen { showRephraseScreen = false } }
    }

    if (showGenerateImageScreen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showGenerateImageScreen = false },
            sheetState = sheetState
        ) {
            GenerateImageScreen(
                onClose = { showGenerateImageScreen = false },
                onImageGenerated = {
                    showGenerateImageScreen = false
                    onEditImageClick()
                }
            )
        }
    }

    if (showGenerateDescriptionScreen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showGenerateDescriptionScreen = false },
            sheetState = sheetState
        ) { GenerateDescriptionScreen { showGenerateDescriptionScreen = false } }
    }

    if (showGenerateHashtagsScreen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showGenerateHashtagsScreen = false },
            sheetState = sheetState
        ) { GenerateHashtagsScreen { showGenerateHashtagsScreen = false } }
    }

    if (showSketchScreen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showSketchScreen = false },
            sheetState = sheetState
        ) { SketchScreen(
            onClose      = { showSketchScreen = false },
            onImageReady = {
                showSketchScreen = false
                onEditImageClick()
            }
        ) }
    }

    if (showStyleTransferScreen) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showStyleTransferScreen = false },
            sheetState = sheetState
        ) {
            StyleTransferScreen(
                onClose      = { showStyleTransferScreen = false },
                onImageReady = {
                    showStyleTransferScreen = false
                    onEditImageClick()
                }
            )
        }
    }

}

// -----------------------------------------------------------------------------
// SUB‑COMPOSABLES – ProfileHeader & FeatureCard
// -----------------------------------------------------------------------------

@Composable
private fun ProfileHeader(profile: UserProfile) {
    /*
     * If you store an avatar/photo URL in Firestore, simply expose that property
     * inside your UserProfile data class and swap the Surface block below for an
     * AsyncImage. Until then, we display the user’s initials.
     */
    ElevatedCard(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.large,
                modifier = Modifier.size(56.dp)
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        profile.firstName.firstOrNull()?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text("${profile.firstName} ${profile.lastName}", style = MaterialTheme.typography.titleMedium)
                Text(profile.email, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun FeatureCard(feature: Feature, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(feature.icon, contentDescription = null, modifier = Modifier.size(36.dp))
            Spacer(Modifier.height(8.dp))
            Text(feature.name, fontSize = 14.sp)
        }
    }
}

// -----------------------------------------------------------------------------
// DUMMY DATA
// -----------------------------------------------------------------------------

data class Feature(val name: String, val icon: ImageVector)

val dummyFeatures = listOf(
    Feature("Rephrase Text", Icons.Filled.Edit),
    Feature("Create an Image", Icons.Outlined.Image),
    Feature("Edit Image", Icons.Filled.PhotoFilter),
    Feature("Generate a Description", Icons.Filled.Description),
    //("Generate Hashtags", Icons.Filled.LocalOffer),
    Feature("Sketch to Image", Icons.Filled.Brush),
    Feature("Style Change", Icons.Filled.Style),
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

/* ──────────────────────────────────────────────────────────────
   1.  RephraseTextScreen – UI that mirrors GenerateDescription
   ──────────────────────────────────────────────────────────── */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RephraseScreen(
    onClose: () -> Unit
) {
    val ctx   = LocalContext.current
    val scope = rememberCoroutineScope()

    /*  state  */
    var inputText     by rememberSaveable { mutableStateOf("") }
    var platform      by rememberSaveable { mutableStateOf("Instagram") }
    var audienceText  by rememberSaveable { mutableStateOf("") }
    var includeTags   by rememberSaveable { mutableStateOf(false) }

    var loading       by rememberSaveable { mutableStateOf(false) }
    var rephrased     by rememberSaveable { mutableStateOf("") }

    /*  static lists  */
    val platforms  = listOf("Instagram", "LinkedIn", "Facebook")
    val audiences  = listOf(
        "Casual users & lifestyle followers",
        "Influencers & content creators",
        "Gen Z & Millennials",
        "Photography & art lovers",
        "Brand & business marketing",
        "Corporate professionals"
    )

    /*  animated gradient title  */
    val fullTitle = "Rephrase Text"
    var title     by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fullTitle.forEachIndexed { i, _ ->
            title = fullTitle.substring(0, i + 1); delay(35)
        }
    }
    val gradientStyle = MaterialTheme.typography.headlineMedium.copy(
        brush         = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary
            )
        ),
        fontWeight    = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    )

    /*  scaffold identical to GenerateDescription  */
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
    ) {
        BottomWaveGradient(
            modifier   = Modifier.matchParentSize(),
            bandHeight = 100.dp,
            fadeHeight = 100.dp,
            waveAmp    = 24.dp,
            waveLen    = 600.dp
        )
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = { Text(title, style = gradientStyle) },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(Icons.Default.Close, null)
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = inputText.isNotBlank() && !loading,
                    enter   = scaleIn() + fadeIn(),
                    exit    = scaleOut() + fadeOut()
                ) {
                    ExtendedFloatingActionButton(
                        icon = {
                            if (loading)
                                CircularProgressIndicator(
                                    Modifier.size(18.dp),
                                    strokeWidth = 2.dp
                                )
                            else
                                Icon(Icons.Default.PlayArrow, null)
                        },
                        text = { Text("Rephrase") },
                        onClick = {
                            scope.launch {
                                loading = true; rephrased = ""
                                rephrased = fetchRephrasedText(
                                    text     = inputText,
                                    platform = platform,
                                    target   = audienceText.ifBlank { "General" },
                                    hashtags = if (includeTags) "Yes" else "No"
                                )
                                loading = false
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(Modifier.height(20.dp))

                /* ───── text to rephrase ───── */
                Text("Text to rephrase", style = MaterialTheme.typography.labelMedium)
                OutlinedTextField(
                    value         = inputText,
                    onValueChange = { inputText = it },
                    placeholder   = { Text("Paste or type your text here…") },
                    shape         = MaterialTheme.shapes.medium,
                    modifier      = Modifier
                        .fillMaxWidth()
                        .heightIn(100.dp, 180.dp)
                )

                Spacer(Modifier.height(20.dp))

                /* ───── platform chips ───── */
                Text("Platform", style = MaterialTheme.typography.labelMedium)
                Row(Modifier.padding(top = 4.dp)) {
                    platforms.forEach { p ->
                        FilterChip(
                            selected   = platform == p,
                            onClick    = { platform = p },
                            label      = { Text(p) },
                            leadingIcon = if (platform == p) {
                                { Icon(Icons.Default.Check, null) }
                            } else null,
                            modifier   = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                /* ───── target audience ───── */
                Text("Target audience", style = MaterialTheme.typography.labelMedium)
                OutlinedTextField(
                    value         = audienceText,
                    onValueChange = { audienceText = it },
                    placeholder   = { Text("Describe your audience…") },
                    leadingIcon   = { Icon(Icons.Default.Person, null) },
                    shape         = MaterialTheme.shapes.medium,
                    modifier      = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    audiences.forEach { suggestion ->
                        AssistChip(
                            onClick = { audienceText = suggestion },
                            label   = { Text(suggestion, maxLines = 1) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                /* ───── include hashtags option ───── */
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked          = includeTags,
                        onCheckedChange  = { includeTags = it }
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Outlined.Tag, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Include hashtags")
                }

                Spacer(Modifier.height(120.dp)) // keep clear of the FAB
            }

            /* ───── overlay loader & result card ───── */
            if (loading) SweepLoader()

            RephrasedTextCard(
                text      = rephrased,
                title =     "Rephrased Text",
                onDismiss = { rephrased = "" }
            )
        }
    }
}

/* ──────────────────────────────────────────────────────────────
   2.  Result-card for the rephrased text
   ──────────────────────────────────────────────────────────── */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RephrasedTextCard(
    text: String,
    title: String,
    onDismiss: () -> Unit
) {
    if (text.isBlank()) return
    val clipboardManager = LocalClipboardManager.current
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val maxCardHeight = (screenHeight * 0.5f).dp

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it != SwipeToDismissBoxValue.Settled) { onDismiss(); true } else false
        }
    )

    Dialog(onDismissRequest = onDismiss) {

        /* ① take the whole overlay & decide where the card lives   */
        Box(
            modifier = Modifier
                .fillMaxSize(),                 // full screen
            contentAlignment = Alignment.TopCenter  // still center-horiz, top-vert
        ) {

            /* ② vertical shift ↓ (choose whichever feels best) */
            val yOffset = 360.dp                // <-- tweak this distance
            SwipeToDismissBox(
                state = dismissState,
                modifier = Modifier.offset {
                    // convert dp → px
                    IntOffset(0, yOffset.roundToPx())
                },
                backgroundContent = {}
            ) {
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)  // horizontal margin only
                        .sizeIn(maxHeight = maxCardHeight)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(title, style = MaterialTheme.typography.titleMedium)
                            Row {
                                // 2. Copy button
                                IconButton(onClick = {
                                    // Copy the entire 'text' into clipboard
                                    clipboardManager.setText(AnnotatedString(text))
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy text"
                                    )
                                }

                                IconButton(onClick = onDismiss) {
                                    Icon(Icons.Default.Close, null)
                                }

                            }
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(text, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

/* ──────────────────────────────────────────────────────────────
   3.  fetchRephrasedText – multipart, matches Cloud Function
   ──────────────────────────────────────────────────────────── */
suspend fun fetchRephrasedText(
    text: String,
    platform: String,
    target: String,
    hashtags: String
): String = withContext(Dispatchers.IO) {
    try {
        /* --- multipart body ----- */
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("text", text)
            .addFormDataPart("platform", platform)
            .addFormDataPart("target", target)
            .addFormDataPart("hashtags", hashtags)
            .build()

        val request = Request.Builder()
            .url("https://us-central1-speedy-web-456812-s7.cloudfunctions.net/rephrase_text")
            .post(body)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string() ?: ""
            val jsonObj      = JSONObject(responseBody)
            /*  Cloud Function returns { "caption": "…" }  */
            jsonObj.optString("caption", "No text returned")
        } else {
            "Error: Code ${response.code} – ${response.message}"
        }
    } catch (e: Exception) {
        "Exception: ${e.message}"
    }
}

@Composable
fun BottomWaveGradient(
    modifier:   Modifier = Modifier,
    bandHeight: Dp = 260.dp,   // total coloured thickness
    fadeHeight: Dp = 180.dp,   // how far it fades to white
    waveAmp:    Dp = 24.dp,    // crest height   (smaller → smoother)
    waveLen:    Dp = 600.dp,   // crest spacing  (larger → smoother)
    speedMs:    Int = 12_000   // time for a full sideways cycle
) {
    /* grab the colours while we’re still in @Composable scope */
    val c1 = MaterialTheme.colorScheme.primary
    val c2 = MaterialTheme.colorScheme.tertiary

    val density = LocalDensity.current

    /* horizontal phase animation only */
    val phase by rememberInfiniteTransition("phase").animateFloat(
        initialValue = 0f,
        targetValue  = (2f * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = speedMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    ComposeCanvas(modifier) {
        val bandH  = with(density) { bandHeight.toPx() }
        val fadeH  = with(density) { fadeHeight.toPx() }
        val amp    = with(density) { waveAmp.toPx() }
        val lambda = with(density) { waveLen.toPx() }
        val step   = 16.dp.toPx()              // sampling granularity

        /* top of coloured band sits a fixed distance from bottom */
        val topY = size.height - bandH

        /* build the wavy top edge */
        val path = ComposePath().apply {
            moveTo(0f, topY)
            var x = 0f
            while (x <= size.width) {
                val y = topY + amp * sin((x / lambda) * 2f * PI.toFloat() + phase)
                lineTo(x, y)
                x += step
            }
            /* close downward so the area below gets filled */
            lineTo(size.width, size.height)
            lineTo(0f,          size.height)
            close()
        }

        /* vertical gradient: colour → transparent white */
        drawPath(
            path = path,
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0.0f to c1,                        // deepest part
                    0.6f to c2,                        // mid‑blend
                    1.0f to Color.Transparent          // fade to screen bg
                ),
                startY = topY + bandH,                // bottom of screen
                endY   = topY - fadeH                 // height where it’s clear
            ),
            style = Fill
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalEncodingApi::class)
@Composable
fun GenerateImageScreen(
    onClose: () -> Unit,
    onImageGenerated: (Bitmap) -> Unit
) {
    val scope = rememberCoroutineScope()

    var promptText by remember { mutableStateOf("") }
    var generatedImage by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // -------- Typing animation for header --------
    val fullHeader = "Generate an Image"
    var typedHeader by remember { mutableStateOf("") }

    val density = LocalDensity.current                   // for dp ↔ px
    val bgShift   = rememberInfiniteTransition(label = "bgShift")
    val yOffset by bgShift.animateFloat(
        initialValue = 0f,
        targetValue  = with(density) { 400.dp.toPx() },  // how far the band travels
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 2000, easing = LinearEasing), // one full cycle = 20 s
            RepeatMode.Reverse
        ),
        label = "bgOffset"
    )

    val gradientStyle = MaterialTheme.typography.headlineMedium.copy(
        brush = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary
            )
        ),
        fontWeight    = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    )

    LaunchedEffect(Unit) {
        fullHeader.forEachIndexed { i, _ ->
            typedHeader = fullHeader.substring(0, i + 1)
            delay(45)
        }
    }


    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)
            //.background(animatedBg)
    ) {
        BottomWaveGradient(
            modifier   = Modifier.matchParentSize(),
            bandHeight = 200.dp,           // tweak to taste
            fadeHeight = 100.dp,
            waveAmp    = 24.dp,
            waveLen    = 600.dp
        )
        // ------------- Main content -------------
        Column(Modifier.matchParentSize().padding(24.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(typedHeader, style = gradientStyle)          // ← was headlineSmall
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onClose) { Icon(Icons.Default.Close, null) }
            }

            Spacer(Modifier.height(8.dp))
            Text("Prompt", style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(
                value = promptText,
                onValueChange = { promptText = it },
                placeholder = { Text("Describe what you’d like to see…") },
                leadingIcon = { Icon(Icons.Default.Image, null) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                maxLines = 4
            )

            val suggestions = listOf(
                "A neon cyberpunk alley at night",
                "A watercolor cat astronaut",
                "Papercraft tropical island",
                "Unicorn in the desert"
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                suggestions.forEach { s ->
                    AssistChip(
                        onClick = { promptText = s },
                        label = { Text(s, maxLines = 1) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            AnimatedVisibility(generatedImage != null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Crossfade(targetState = generatedImage) { bmp ->
                        bmp?.let {
                            androidx.compose.foundation.Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier.fillMaxWidth().height(300.dp)
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(70.dp))
        }
        AnimatedVisibility(
            visible = promptText.isNotBlank() && !isLoading,
            enter   = scaleIn() + fadeIn(),
            exit    = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(y = (-96).dp)
                .offset(x = (-24).dp)
        ) {
            ExtendedFloatingActionButton(
                icon = {
                    if (isLoading)
                        CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                    else
                        Icon(Icons.Default.PlayArrow, null)
                },
                text = { Text("Generate") },
                onClick = {
                    scope.launch {
                        isLoading      = true
                        generatedImage = null
                        val bmp = fetchGeneratedImage(promptText)
                        isLoading      = false
                        bmp?.let {
                            ImageHolder.generatedImage = it
                            generatedImage             = it
                            onImageGenerated(it)
                        } ?: Log.e("GenerateImageScreen", "Failed to generate image")
                    }
                }
            )
        }

        // ------------- Loading overlay -------------
        if (isLoading) {
            // Infinite transition animating vertical offset 0f -> 1f
            val transition = rememberInfiniteTransition(label = "fadeSweep")
            val anim by transition.animateFloat(
                initialValue = -1f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    tween(durationMillis = 1600, easing = LinearEasing),
                    RepeatMode.Restart
                ),
                label = "offset"
            )

            // Gradient for sweeping fade
            val fadeBrush = Brush.verticalGradient(
                0f to Color.Transparent,
                0.3f to Color.White.copy(alpha = 0.55f),
                0.7f to Color.White.copy(alpha = 0.55f),
                1f to Color.Transparent,
                startY = 0f,
                endY = 300f
            )

            // Overlay box with animated alpha/translationY
            Box(
                Modifier
                    .matchParentSize()
                    .graphicsLayer { translationY = anim * size.height }
                    .background(fadeBrush)
            )
        }
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun GenerateDescriptionScreen(
    onClose: () -> Unit
) {
    val ctx   = LocalContext.current
    val scope = rememberCoroutineScope()

    /* ───── state ─────────────────────────────────────────────── */
    var imageUri       by rememberSaveable { mutableStateOf<Uri?>(null) }
    var expandedImage  by remember { mutableStateOf<Uri?>(null) }
    var contextText    by rememberSaveable { mutableStateOf("") }
    var caption        by rememberSaveable { mutableStateOf("") }
    var loading        by rememberSaveable { mutableStateOf(false) }

    /* — platform / audience chips — */
    var platform       by rememberSaveable { mutableStateOf("Instagram") }
    val platforms        = listOf("Instagram", "LinkedIn", "Facebook")

    var audienceText   by rememberSaveable { mutableStateOf("") }
    val audiences        = listOf(
        "Casual users & lifestyle followers",
        "Influencers & content creators",
        "Gen Z & Millennials",
        "Photography & art lovers",
        "Brand & business marketing",
        "Corporate professionals"
    )

    var includeTags    by rememberSaveable { mutableStateOf(false) }

    /* — image picker — */
    val pickImage = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> imageUri = uri }

    /* ───── animated gradient title (same values as StyleTransfer) ───── */
    val fullTitle = "Generate a Description"
    var title     by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fullTitle.forEachIndexed { i, _ ->
            title = fullTitle.substring(0, i + 1)
            delay(35)
        }
    }

    val gradientStyle = MaterialTheme.typography.headlineMedium.copy(
        brush         = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary
            )
        ),
        fontWeight    = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    )

    /* ───── scaffolding identical to StyleTransferScreen ───── */
    val bg             = Brush.verticalGradient(
        listOf(
            MaterialTheme.colorScheme.secondaryContainer.copy(.25f),
            MaterialTheme.colorScheme.surface
        )
    )
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        /* 🎨 bottom‑anchored, side‑to‑side waves */
        BottomWaveGradient(
            modifier = Modifier.matchParentSize(),
            bandHeight = 100.dp,              // ← your requested params
            fadeHeight = 100.dp,
            waveAmp = 24.dp,
            waveLen = 600.dp
        )
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                //.background(bg)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = { Text(title, style = gradientStyle) },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(
                                Icons.Default.Close,
                                null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color.Transparent),
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = imageUri != null && !loading,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut(),
                ) {
                    ExtendedFloatingActionButton(
                        icon = {
                            if (loading)
                                CircularProgressIndicator(Modifier.size(18.dp), strokeWidth = 2.dp)
                            else
                                Icon(Icons.Default.PlayArrow, null)
                        },
                        text = { Text("Generate") },
                        onClick = {
                            scope.launch {
                                loading = true; caption = ""
                                caption = fetchGeneratedCaption(
                                    context = ctx,
                                    imageUri = imageUri!!,
                                    platform = platform,
                                    target = audienceText.ifBlank { "General" },
                                    hashtags = if (includeTags) "Yes" else "No",
                                    imgDescription = contextText
                                )
                                loading = false
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->

            Column(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(Modifier.height(16.dp))

                /* ───── image picker CARD (click‑to‑zoom) ───── */
                SelectionCard(label = "Pick image", uri = imageUri) {
                    if (imageUri == null) pickImage.launch("image/*")
                    else expandedImage = imageUri
                }

                Spacer(Modifier.height(20.dp))

                /* ───── rest of the original UI (platform, audience …) ───── */
                Text("Platform", style = MaterialTheme.typography.labelMedium)
                Row(Modifier.padding(top = 4.dp)) {
                    platforms.forEach { p ->
                        FilterChip(
                            selected = platform == p,
                            onClick = { platform = p },
                            label = { Text(p) },
                            leadingIcon = if (platform == p) {
                                { Icon(Icons.Default.Check, null) }
                            } else null,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Text("Target audience", style = MaterialTheme.typography.labelMedium)
                OutlinedTextField(
                    value = audienceText,
                    onValueChange = { audienceText = it },
                    placeholder = { Text("Describe your audience…") },
                    leadingIcon = { Icon(Icons.Default.Person, null) },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.horizontalScroll(rememberScrollState())) {
                    audiences.forEach { suggestion ->
                        AssistChip(
                            onClick = { audienceText = suggestion },
                            label = { Text(suggestion, maxLines = 1) },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = includeTags,
                        onCheckedChange = { includeTags = it }
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(                 // 🏷️ or #   (use whichever icon set you have)
                        imageVector = Icons.Outlined.Tag,   // requires `import androidx.compose.material.icons.outlined.Tag`
                        contentDescription = null
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Include hashtags")
                }

                Spacer(Modifier.height(12.dp))
                Text("Provide context (optional):")
                OutlinedTextField(
                    value = contextText,
                    onValueChange = { contextText = it },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(56.dp, 100.dp)
                )

                Spacer(Modifier.height(120.dp)) // keep content clear of the FAB
            }

            /* ───── same loader & zoom dialog you already have ───── */
            if (loading) SweepLoader()

            if (expandedImage != null) {
                Dialog(onDismissRequest = { expandedImage = null }) {
                    ZoomableImage(expandedImage!!)
                }
            }

            /* ───── the caption‑card logic from your original screen ───── */
            caption.takeIf { it.isNotBlank() }?.let { generated ->
                RephrasedTextCard(
                    text = generated,
                    title = "Generated caption",
                    onDismiss = { caption = "" }
                )
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


/*
enum class Tool(val label: String, val icon: ImageVector) {
    Brush("Brush", Icons.Filled.Brush),
    Eraser("Erase", Icons.Filled.CleaningServices)
}

@Composable
private fun ToolSelector(
    selected: Tool,
    onSelect: (Tool) -> Unit,
    modifier: Modifier = Modifier
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        Tool.entries.forEachIndexed { index, tool ->
            SegmentedButton(
                shape     = SegmentedButtonDefaults.itemShape(index, Tool.entries.size),
                selected  = selected == tool,
                onClick   = { onSelect(tool) },
                icon      = { Icon(tool.icon, contentDescription = tool.label) },
                label     = { Text(tool.label) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PromptField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text("Describe the image") },
        trailingIcon = {
            if (text.isNotBlank()) {
                IconButton(onClick = { onTextChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28),

        // ⬇️  new API name + new color‑slot names
        colors = TextFieldDefaults.colors(
            focusedContainerColor   = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .40f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .40f),
            disabledContainerColor  = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .20f),

            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor  = Color.Transparent
        ),

        modifier = modifier.fillMaxWidth()
    )
}



@Composable
fun SketchScreen(
    onClose: () -> Unit,
    onImageReady: () -> Unit         // navigate to Edit screen
) {
    val ctx   = LocalContext.current
    val scope = rememberCoroutineScope()

    /* ─── State ─────────────────────────────────────────────────────────── */
    var sketchBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var brushMode    by remember { mutableStateOf(true) }    // true = pencil
    var promptText   by remember { mutableStateOf("") }
    var loading      by remember { mutableStateOf(false) }
    var error        by remember { mutableStateOf<String?>(null) }
    var hasSketch   by remember { mutableStateOf(false) }   // ⇦ set to true on first stroke

    // convenience flag for FAB
    val canGenerate = hasSketch && promptText.isNotBlank() && !loading

    if (sketchBitmap == null) {
        sketchBitmap = Bitmap.createBitmap(512, 700, Bitmap.Config.ARGB_8888).apply {
            eraseColor(android.graphics.Color.WHITE)
        }
    }

    /* ─── Header type‑writer with gradient ─────────────────────────────── */
    val fullTitle = "Sketch to Image"
    var typedTitle by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fullTitle.forEachIndexed { i, _ ->
            typedTitle = fullTitle.substring(0, i + 1); delay(35)
        }
    }
    val gradientStyle = MaterialTheme.typography.headlineMedium.copy(
        brush = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary
            )
        ),
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    )

    /* ─── Background tint ──────────────────────────────────────────────── */
    val bg = Brush.linearGradient(
        0f to MaterialTheme.colorScheme.primary.copy(.25f),
        .5f to MaterialTheme.colorScheme.secondaryContainer.copy(.12f),
        1f to MaterialTheme.colorScheme.background
    )
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

        /* 🎨 bottom‑anchored, side‑to‑side waves */
        BottomWaveGradient(
            modifier = Modifier.matchParentSize(),
            bandHeight = 100.dp,              // ← your requested params
            fadeHeight = 100.dp,
            waveAmp = 24.dp,
            waveLen = 600.dp
        )
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                //.background(bg)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = { Text(typedTitle, style = gradientStyle) },
                    navigationIcon = {
                        IconButton(onClick = onClose) { Icon(Icons.Default.Close, null) }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            /* ─── Floating Generate FAB  ───────────────────────────────── */
            floatingActionButton = {
                AnimatedVisibility(
                    visible = canGenerate,                 // only when sketch + prompt
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    ExtendedFloatingActionButton(
                        icon = {
                            if (loading)
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(18.dp)
                                )
                            else
                                Icon(Icons.Default.PlayArrow, null)
                        },
                        text = { Text(if (loading) "Generating…" else "Generate") },
                        onClick = {
                            scope.launch {
                                if (sketchBitmap == null) return@launch
                                loading = true; error = null
                                val result = fetchSketchToImage(sketchBitmap!!, promptText)
                                loading = false
                                if (result != null) {
                                    ImageHolder.generatedImage = result
                                    onImageReady()
                                } else {
                                    error = "Failed to generate image"
                                }
                            }
                        },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }
            }
        ) { inner ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                /* ─── Canvas ──────────────────────────────────────────────── */
                sketchBitmap?.let { bmp ->
                    DrawCanvas(bitmap = bmp, brushMode = brushMode, onStroke = { hasSketch = true })
                }

                Spacer(Modifier.height(16.dp))

                /* ─── Tools row ──────────────────────────────────────────── */
                /* ─── NEW tool picker ────────────────────────────────────── */
                ToolSelector(
                    selected = if (brushMode) Tool.Brush else Tool.Eraser,
                    onSelect = { tool -> brushMode = (tool == Tool.Brush) }
                )

                Spacer(Modifier.height(16.dp))

                /* ─── NEW prompt field ───────────────────────────────────── */
                PromptField(
                    text = promptText,
                    onTextChange = { promptText = it }
                )

                Spacer(Modifier.height(24.dp))
                error?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }

            }

            if (loading) SweepLoader()
        }
    }
}



/* ─────────────────────────────────────────────────────────────────────────── */
@Composable
fun DrawCanvas(
    bitmap: Bitmap,
    brushMode: Boolean,
    onStroke: () -> Unit
) {
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
        }
    }
    var drawCounter by remember { mutableStateOf(0) }
    fun invalidate() { drawCounter++ }

    var lastPoint by remember { mutableStateOf<Offset?>(null) }
    val density = LocalDensity.current.density
    val aspect = bitmap.width.toFloat() / bitmap.height

    Box(
        Modifier
            .fillMaxWidth()
            .aspectRatio(aspect)
            .border(1.dp, Color.Gray)
            .pointerInput(brushMode) {
                detectDragGestures(
                    onDragStart = { offset ->
                        lastPoint = offset
                        val canvas = Canvas(bitmap)
                        val container = Size(size.width.toFloat(), size.height.toFloat())
                        val (x, y) = offset.toPixel(container, bitmap, density)
                        canvas.drawCircle(
                            x, y, brushSizePx / 2,
                            if (brushMode) paintBrush else paintEraser
                        )
                    },
                    onDrag = { change, _ ->
                        val current = change.position
                        val canvas = Canvas(bitmap)
                        val container = Size(size.width.toFloat(), size.height.toFloat())
                        lastPoint?.let { lp ->
                            val (sx, sy) = lp.toPixel(container, bitmap, density)
                            val (ex, ey) = current.toPixel(container, bitmap, density)
                            canvas.drawLine(
                                sx, sy, ex, ey,
                                if (brushMode) paintBrush else paintEraser
                            )
                            invalidate()
                            onStroke()
                        }
                        lastPoint = current
                    },
                    onDragEnd = { lastPoint = null }
                )
            }
    ) {
        drawCounter
        Image(bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier.fillMaxSize())
    }
}
*/






/* coord helper */

/*
private fun Offset.toPixel(
    container: Size, bmp: Bitmap, density: Float
): Pair<Float, Float> {
    val sx = bmp.width / container.width
    val sy = bmp.height / container.height
    return x * sx to y * sy
}
*/

private fun Offset.toPxInBitmap(
    containerSize: IntSize,   // <-- now IntSize
    bmp: Bitmap,
    density: Float
): Pair<Float, Float> {
    val sx = bmp.width  / containerSize.width.toFloat()
    val sy = bmp.height / containerSize.height.toFloat()
    return x * sx to y * sy
}
/* ─── Canvas with variable brush / eraser size ─────────────────────── */
@Composable
fun DrawCanvas(
    bitmap: Bitmap,
    brushMode: Boolean,
    brushSizeDp: Float,
    onStroke: () -> Unit
) {
    val density  = LocalDensity.current.density

    /* two android.graphics.Paint instances reused on every draw */
    val brushPaint  = remember {
        Paint().apply {
            color       = android.graphics.Color.BLACK
            style       = Paint.Style.STROKE
            strokeCap   = Paint.Cap.ROUND
            isAntiAlias = true
        }
    }
    val eraserPaint = remember {
        Paint().apply {
            color       = android.graphics.Color.WHITE
            style       = Paint.Style.STROKE
            strokeCap   = Paint.Cap.ROUND
            //xfermode    = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            isAntiAlias = true
        }
    }

    var prev by remember { mutableStateOf<Offset?>(null) }

    ComposeCanvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(bitmap.width / bitmap.height.toFloat())
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant)
            .pointerInput(brushMode, brushSizeDp) {
                detectDragGestures(
                    onDragStart = { start ->
                        val canvas   = android.graphics.Canvas(bitmap)
                        val (x, y)   = start.toPxInBitmap(size, bitmap, density)
                        val strokePx = brushSizeDp * density

                        (if (brushMode) brushPaint else eraserPaint).also {
                            it.strokeWidth = strokePx
                            canvas.drawCircle(x, y, strokePx / 2, it)
                        }
                        prev = start
                        onStroke()
                    },
                    onDragEnd    = { prev = null },
                    onDragCancel = { prev = null },
                    onDrag       = { change, _ ->
                        val cur = change.position
                        prev?.let { p0 ->
                            val canvas   = android.graphics.Canvas(bitmap)
                            val strokePx = brushSizeDp * density
                            val (x0, y0) = p0.toPxInBitmap(size, bitmap, density)
                            val (x1, y1) = cur.toPxInBitmap(size, bitmap, density)

                            (if (brushMode) brushPaint else eraserPaint).also {
                                it.strokeWidth = strokePx
                                canvas.drawLine(x0, y0, x1, y1, it)
                            }
                            prev = cur
                            onStroke()
                        }
                    }
                )
            }
    ) {
        /*  <-- we’re now inside the DrawScope, so we can call the
                DrawScope-level extension of drawImage directly.       */
        drawImage(
            image   = bitmap.asImageBitmap(),
            dstSize = IntSize(size.width.toInt(), size.height.toInt()) // Scale to canvas
        )
    }
}

/* ────────────────────────────────────────────────────────────────────── */
/* UI elements reused from your original file                            */
/* -------------------------------------------------------------------- */

@Composable
private fun PromptField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text("Describe the image") },
        trailingIcon = {
            if (text.isNotBlank()) {
                IconButton(onClick = { onTextChange("") }) {
                    Icon(Icons.Filled.Close, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(28),
        colors = TextFieldDefaults.colors(
            focusedContainerColor   = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .40f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .40f),
            disabledContainerColor  = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .20f),
            focusedIndicatorColor   = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor  = Color.Transparent
        ),
        modifier = modifier.fillMaxWidth()
    )
}

/* ────────────────────────────────────────────────────────────────────── */
/*                >>>  Sketch to Image Screen  <<<                       */
/* -------------------------------------------------------------------- */

@Composable
fun SketchScreen(
    onClose: () -> Unit,
    onImageReady: () -> Unit         // navigate to Edit screen
) {
    val ctx   = LocalContext.current
    val scope = rememberCoroutineScope()

    /* ─── State ─────────────────────────────────────────────────────── */
    var sketchBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var brushMode    by remember { mutableStateOf(true) }  // true = pencil
    var brushSizeDp  by remember { mutableStateOf(8f) }
    var promptText   by remember { mutableStateOf("") }
    var loading      by remember { mutableStateOf(false) }
    var error        by remember { mutableStateOf<String?>(null) }
    var hasSketch    by remember { mutableStateOf(false) } // ⇦ set true on first stroke

    // convenience flag for FAB
    val canGenerate = hasSketch && promptText.isNotBlank() && !loading

    if (sketchBitmap == null) {
        sketchBitmap = Bitmap.createBitmap(512, 700, Bitmap.Config.ARGB_8888).apply {
            eraseColor(android.graphics.Color.WHITE)
        }
    }

    /* ─── Header type-writer with gradient ─────────────────────────── */
    val fullTitle = "Sketch to Image"
    var typedTitle by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fullTitle.forEachIndexed { i, _ ->
            typedTitle = fullTitle.substring(0, i + 1)
            delay(35)
        }
    }
    val gradientStyle = MaterialTheme.typography.headlineMedium.copy(
        brush = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary
            )
        ),
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    )

    /* ─── Background tint ──────────────────────────────────────────── */
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        /* 🎨 backdrop wave (keep your implementation if present) */
        BottomWaveGradient(
            modifier = Modifier.matchParentSize(),
            bandHeight = 100.dp,
            fadeHeight = 100.dp,
            waveAmp = 24.dp,
            waveLen = 600.dp
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = { Text(typedTitle, style = gradientStyle) },
                    navigationIcon = {
                        IconButton(onClick = onClose) { Icon(Icons.Default.Close, null) }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            /* ─── Floating Generate FAB  ─────────────────────────── */
            floatingActionButton = {
                AnimatedVisibility(
                    visible = canGenerate,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    ExtendedFloatingActionButton(
                        icon = {
                            if (loading)
                                CircularProgressIndicator(
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(18.dp)
                                )
                            else
                                Icon(Icons.Default.PlayArrow, null)
                        },
                        text = { Text(if (loading) "Generating…" else "Generate") },
                        onClick = {
                            scope.launch {
                                if (sketchBitmap == null) return@launch
                                loading = true; error = null
                                val result = fetchSketchToImage(sketchBitmap!!, promptText)
                                loading = false
                                if (result != null) {
                                    ImageHolder.generatedImage = result
                                    onImageReady()
                                } else {
                                    error = "Failed to generate image"
                                }
                            }
                        },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(bottom = 32.dp)
                    )
                }
            }
        ) { inner ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))

                /* ─── Canvas ──────────────────────────────────────────── */
                sketchBitmap?.let { bmp ->
                    DrawCanvas(
                        bitmap       = bmp,
                        brushMode    = brushMode,
                        brushSizeDp  = brushSizeDp,
                        onStroke     = { hasSketch = true }
                    )
                }

                Spacer(Modifier.height(16.dp))

                /* ─── Brush / Erase controls (chips + slider) ─────────── */
                BrushSizeBar(
                    brushMode    = brushMode,
                    onModeChange = { brushMode = it },
                    brushSizeDp  = brushSizeDp,
                    onSizeChange = { brushSizeDp = it }
                )

                Spacer(Modifier.height(16.dp))

                /* ─── Prompt field ────────────────────────────────────── */
                PromptField(
                    text = promptText,
                    onTextChange = { promptText = it }
                )

                Spacer(Modifier.height(24.dp))
                error?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }

            if (loading) SweepLoader()
        }
    }
}

/* ────────────────────────────────────────────────────────────────────── */
/*            Brush / Eraser chips + size slider (reusable)              */
/* -------------------------------------------------------------------- */

@Composable
private fun BrushSizeBar(
    brushMode: Boolean,
    onModeChange: (Boolean) -> Unit,
    brushSizeDp: Float,
    onSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val neutral     = MaterialTheme.colorScheme.onSurfaceVariant
    val greyChip    = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .9f)
    val controlTint = neutral

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconToggleButton(
            checked = brushMode,
            onCheckedChange = { if (!brushMode) onModeChange(true) },
            colors = IconButtonDefaults.iconToggleButtonColors(
                contentColor          = neutral,
                containerColor        = Color.Transparent,
                checkedContainerColor = greyChip,
                checkedContentColor   = neutral
            )
        ) { Icon(Icons.Default.Brush, null) }

        Spacer(Modifier.width(8.dp))

        IconToggleButton(
            checked = !brushMode,
            onCheckedChange = { if (brushMode) onModeChange(false) },
            colors = IconButtonDefaults.iconToggleButtonColors(
                contentColor          = neutral,
                containerColor        = Color.Transparent,
                checkedContainerColor = greyChip,
                checkedContentColor   = neutral
            )
        ) { Icon(Icons.Default.CleaningServices, null) }

        Spacer(Modifier.width(16.dp))

        Slider(
            value = brushSizeDp,
            onValueChange = onSizeChange,
            valueRange = 2f..16f,
            steps = 6,
            colors = SliderDefaults.colors(
                activeTrackColor   = controlTint,
                inactiveTrackColor = controlTint.copy(alpha = 0.3f),
                thumbColor         = controlTint
            ),
            modifier = Modifier.weight(1f)
        )
    }
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


@Composable
fun StyleTransferScreen(
    onClose: () -> Unit,
    onImageReady: () -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // ─── State ────────────────────────────────────────────────────────────────
    var contentUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var styleUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var outputUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    var loading by rememberSaveable { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var expandedImage by remember { mutableStateOf<Uri?>(null) }

    val pickContent = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { contentUri = it }

    val pickStyle = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { styleUri = it }

    // animated type‑writer title with gradient style
    val fullTitle = "Art‑Style Transfer"
    var title by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fullTitle.forEachIndexed { i, _ ->
            title = fullTitle.substring(0, i + 1)
            delay(35)
        }
    }

    val gradientStyle = MaterialTheme.typography.headlineMedium.copy(
        brush = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary
            )
        ),
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = 1.5.sp
    )

    val bg = Brush.linearGradient(
        0f to MaterialTheme.colorScheme.primary.copy(.25f),
        .5f to MaterialTheme.colorScheme.secondaryContainer.copy(.12f),
        1f to MaterialTheme.colorScheme.background
    )

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(.85f)
    ) {

        /* 🎨 bottom‑anchored, side‑to‑side waves */
        BottomWaveGradient(
            modifier = Modifier.matchParentSize(),
            bandHeight = 100.dp,              // ← your requested params
            fadeHeight = 100.dp,
            waveAmp = 24.dp,
            waveLen = 600.dp
        )

        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = { Text(title, style = gradientStyle) },
                    navigationIcon = {
                        IconButton(onClick = onClose) {
                            Icon(
                                Icons.Default.Close,
                                null
                            )
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = contentUri != null && styleUri != null && !loading,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    ExtendedFloatingActionButton(
                        icon = { Icon(Icons.Default.PlayArrow, null) },
                        text = { Text("Stylise") },
                        onClick = {
                            scope.launch {
                                loading = true; error = null; outputUri = null
                                fetchStylisedImage(ctx, contentUri!!, styleUri!!)
                                    .onSuccess { bytes ->
                                        val bmp =
                                            BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                        ImageHolder.generatedImage = bmp   // <‑‑ keep ImageHolder
                                        onImageReady()
                                    }
                                    .onFailure { e ->
                                        error = e.localizedMessage ?: "Unknown error"
                                    }
                                loading = false
                            }
                        }
                    )
                }
            }
        ) { inner ->
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(inner)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(16.dp))
                SelectionCard("Content image", contentUri) {
                    if (contentUri == null) {
                        // no image yet → open picker
                        pickContent.launch("image/*")
                    } else {
                        // thumbnail already shown → open zoom dialog
                        expandedImage = contentUri
                    }
                }
                Spacer(Modifier.height(16.dp))
                SelectionCard("Style image", styleUri) {
                    if (styleUri == null) {
                        pickStyle.launch("image/*")
                    } else {
                        expandedImage = styleUri
                    }
                }

                AnimatedVisibility(outputUri != null) {
                    Column {
                        Spacer(Modifier.height(24.dp))
                        SelectionCard("Stylised", outputUri) {
                            // outputUri is never null inside this block
                            expandedImage = outputUri
                        }
                    }
                }

                error?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
                Spacer(Modifier.height(120.dp)) // keep content clear of FAB
            }

            if (loading) SweepLoader()
            if (expandedImage != null) {
                Dialog(onDismissRequest = { expandedImage = null }) {
                    ZoomableImage(expandedImage!!)
                }
            }
        }

    }
}
/* ──────────────────────────────────────────────────────────────────────────── */
@Composable
private fun SelectionCard(
    label: String,
    uri:   Uri?,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        if (uri == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddPhotoAlternate, null, Modifier.size(48.dp))
                    Spacer(Modifier.height(4.dp))
                    Text(label, style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center)
                }
            }
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uri).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ZoomableImage(uri: Uri) {
    // Track zoom + pan
    var scale  by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)        // dark backdrop
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale  = (scale * zoom).coerceIn(1f, 4f)  // limit 1x‑4x
                    offset += pan
                }
            }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(uri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,          // show entire bitmap
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer {
                    translationX = offset.x
                    translationY = offset.y
                    scaleX = scale
                    scaleY = scale
                }
        )
    }
}

@Composable
private fun SweepLoader() {
    val offset = rememberInfiniteTransition().animateFloat(
        initialValue = -1f, targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(1600, easing = LinearEasing), RepeatMode.Restart
        )
    )
    val sweep = Brush.verticalGradient(
        0f to Color.Transparent,
        0.35f to Color.White.copy(alpha = .55f),
        0.65f to Color.White.copy(alpha = .55f),
        1f to Color.Transparent,
        startY = 0f, endY = 300f
    )
    Box(
        Modifier
            .fillMaxSize()
            .graphicsLayer { translationY = offset.value * size.height }
            .background(sweep)
    )
}

suspend fun fetchStylisedImage(
    context: Context,
    contentUri: Uri,
    styleUri:   Uri
): Result<ByteArray> = withContext(Dispatchers.IO) {
    try {
        val cr   = context.contentResolver
        val cSrc = cr.openInputStream(contentUri) ?: return@withContext Result.failure(Exception("Bad content URI"))
        val sSrc = cr.openInputStream(styleUri  ) ?: return@withContext Result.failure(Exception("Bad style URI"))

        // ---------- multipart body --------------------------------------------------
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "content",
                "content.jpg",
                object : RequestBody() {
                    override fun contentType(): MediaType = "image/jpeg".toMediaType()

                    override fun writeTo(sink: BufferedSink) {
                        // copy the InputStream into the sink, ignore the returned byte‑count
                        cSrc.source().use { source -> sink.writeAll(source) }
                    }
                }
            )
            .addFormDataPart(
                "style",
                "style.jpg",
                object : RequestBody() {
                    override fun contentType(): MediaType = "image/jpeg".toMediaType()
                    override fun writeTo(sink: BufferedSink) {
                        sSrc.source().use { source -> sink.writeAll(source) }
                    }
                }
            )
            .build()

        val req = Request.Builder()
            .url("https://vlad-manolache18--stytr-stytr-inference-serve.modal.run")
            .post(body)
            .header("Origin", "https://vlad-manolache18--stytr-stytr-inference-serve.modal.run")
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)   // TCP + TLS handshake
            .writeTimeout(60, TimeUnit.SECONDS)     // sending the images
            .readTimeout(3,  TimeUnit.MINUTES)      // waiting while Modal renders
            .callTimeout(5, TimeUnit.MINUTES)       // whole operation upper‑bound
            .build()

        client.newCall(req).execute().use { res ->
            if (res.isSuccessful) {
                val bytes = res.body?.bytes() ?: return@withContext Result.failure(Exception("Empty body"))
                Result.success(bytes)
            } else {
                Result.failure(Exception("HTTP ${res.code}: ${res.message}"))
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}