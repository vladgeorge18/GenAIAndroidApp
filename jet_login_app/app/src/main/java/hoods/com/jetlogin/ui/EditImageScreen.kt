package hoods.com.jetlogin.ui

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.text.TextStyle

import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode

import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.scrollable

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Brush

import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.CropRotate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults

import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hoods.com.jetlogin.ImageHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector


import java.util.concurrent.TimeUnit


enum class SaveIndicator {
    SUCCESS,
    NO_IMAGE
}


// Saves the given [bitmap] into the phone gallery using MediaStore.

fun saveImageToGallery(context: android.content.Context, bitmap: Bitmap): Boolean {
    val filename = "IMG_${System.currentTimeMillis()}.jpg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }
    }
    val contentResolver = context.contentResolver
    val uri: Uri? =
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    if (uri != null) {
        contentResolver.openOutputStream(uri)?.use { outputStream ->
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                return true
            }
        }
    }
    return false
}
/*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditImageScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var saveIndicator by remember { mutableStateOf<SaveIndicator?>(null) }
    var isProcessing by remember { mutableStateOf(false) } // For API call loading indicator

    // Bottom sheets
    var showBgReplaceSheet by remember { mutableStateOf(false) }
    var showInpaintMode by remember { mutableStateOf(false) }
    var showRemoveObjectMode by remember { mutableStateOf(false) }
    var showExtendScreen by remember { mutableStateOf(false) }

    // Load the ImageHolder image if not already set
    LaunchedEffect(Unit) {
        if (imageBitmap == null && ImageHolder.generatedImage != null) {
            imageBitmap = ImageHolder.generatedImage
        }
    }

    // Hide the save indicator after 2 seconds
    LaunchedEffect(saveIndicator) {
        if (saveIndicator != null) {
            delay(2000L)
            saveIndicator = null
        }
    }

    // Launcher to pick an image from the gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { inputStream ->
                val bitmap = BitmapFactory.decodeStream(inputStream)
                imageBitmap = bitmap
                ImageHolder.generatedImage = bitmap
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text("Edit Image") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = "Upload Image"
                        )
                    }
                    IconButton(onClick = {
                        if (imageBitmap != null) {
                            val result = saveImageToGallery(context, imageBitmap!!)
                            saveIndicator =
                                if (result) SaveIndicator.SUCCESS else SaveIndicator.NO_IMAGE
                        } else {
                            saveIndicator = SaveIndicator.NO_IMAGE
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Save Image"
                        )
                    }

                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier.navigationBarsPadding(),
                containerColor = Color.DarkGray,
                contentColor = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 8.dp)
                ) {
                    val bottomBarItems = listOf(

                        "Bg Remove" to Icons.Default.Crop,
                        "Bg Replace" to Icons.Default.Colorize,
                        "Inpaint" to Icons.Default.Brush,
                        "Remove Object" to Icons.Default.Delete,
                        "Extend" to Icons.Default.CropRotate
                    )

                    bottomBarItems.forEach { (label, icon) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    when (label) {
                                        "Gallery" -> galleryLauncher.launch("image/*")
                                        "Download" -> {
                                            if (imageBitmap != null) {
                                                val result =
                                                    saveImageToGallery(context, imageBitmap!!)
                                                saveIndicator =
                                                    if (result) SaveIndicator.SUCCESS
                                                    else SaveIndicator.NO_IMAGE
                                            } else {
                                                saveIndicator = SaveIndicator.NO_IMAGE
                                            }
                                        }
                                        "Bg Remove" -> {
                                            // Remove background
                                            if (imageBitmap != null && !isProcessing) {
                                                isProcessing = true
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    val processedBitmap =
                                                        removeBackgroundFromImage(imageBitmap!!)
                                                    if (processedBitmap != null) {
                                                        imageBitmap = processedBitmap
                                                        saveIndicator = SaveIndicator.SUCCESS
                                                    } else {
                                                        saveIndicator = SaveIndicator.NO_IMAGE
                                                    }
                                                    isProcessing = false
                                                }
                                            } else {
                                                saveIndicator = SaveIndicator.NO_IMAGE
                                            }
                                        }
                                        "Bg Replace" -> {
                                            if (imageBitmap != null) {
                                                showBgReplaceSheet = true
                                            } else {
                                                saveIndicator = SaveIndicator.NO_IMAGE
                                            }
                                        }
                                        "Inpaint" -> {
                                            if (imageBitmap != null) {
                                                showInpaintMode = true
                                            } else {
                                                saveIndicator = SaveIndicator.NO_IMAGE
                                            }
                                        }
                                        "Remove Object" -> {
                                            if (imageBitmap != null) {
                                                showRemoveObjectMode = true
                                            } else {
                                                saveIndicator = SaveIndicator.NO_IMAGE
                                            }
                                        }
                                        "Extend" -> {
                                            if (imageBitmap != null) {
                                                showExtendScreen = true
                                            } else {
                                                saveIndicator = SaveIndicator.NO_IMAGE
                                            }
                                        }
                                    }
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = Color.White
                            )
                            Text(
                                text = label,
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Just for layout spacing from the top
                Spacer(modifier = Modifier.height(64.dp))

                // The main image area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp)
                        .border(1.dp, Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    imageBitmap?.let { image ->
                        Image(
                            bitmap = image.asImageBitmap(),
                            contentDescription = "Generated Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .blur(if (isProcessing) 16.dp else 0.dp)
                        )
                    } ?: Text("No image selected")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Loading indicator if processing
            if (isProcessing) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // A small text overlay for success/failure messages
            saveIndicator?.let { indicator ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = paddingValues.calculateBottomPadding() + 16.dp)
                        .align(Alignment.BottomCenter),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (indicator) {
                            SaveIndicator.SUCCESS ->
                                if (isProcessing) "Processing Image..." else "Operation Successful"
                            SaveIndicator.NO_IMAGE -> "No image to process"
                        },
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }

            // Show Inpainting Overlay if requested
            if (showInpaintMode && imageBitmap != null) {
                InpaintingOverlay(
                    originalImage = imageBitmap!!,
                    onApply = { mask, prompt ->
                        showInpaintMode = false
                        CoroutineScope(Dispatchers.Main).launch {
                            isProcessing = true
                            // Call your real inpainting logic here:
                            val result = inpaintImage(imageBitmap!!, mask, prompt)
                            isProcessing = false
                            result?.let {
                                imageBitmap = it
                                saveIndicator = SaveIndicator.SUCCESS
                            } ?: run {
                                saveIndicator = SaveIndicator.NO_IMAGE
                            }
                            showInpaintMode = false
                        }
                    },
                    onClose = { showInpaintMode = false }
                )
            }

            // Show Inpainting Overlay if requested
            if (showRemoveObjectMode && imageBitmap != null) {
                RemoveObjectOverlay(
                    originalImage = imageBitmap!!,
                    onApply = { mask ->
                        showRemoveObjectMode = false
                        CoroutineScope(Dispatchers.Main).launch {
                            isProcessing = true
                            // Call your real inpainting logic here:
                            val result = removeObjectFromImage(imageBitmap!!, mask)
                            isProcessing = false
                            result?.let {
                                imageBitmap = it
                                saveIndicator = SaveIndicator.SUCCESS
                            } ?: run {
                                saveIndicator = SaveIndicator.NO_IMAGE
                            }
                            showRemoveObjectMode = false
                        }
                    },
                    onClose = { showRemoveObjectMode = false }
                )
            }
            // Show ExtendOverlay if requested
            if (showExtendScreen && imageBitmap != null) {
                ExtendOverlay(
                    originalImage = imageBitmap!!,
                    onApply = { leftP, rightP, upP, downP ->
                        // The user pressed "Apply" with the chosen percentages
                        showExtendScreen = false
                        CoroutineScope(Dispatchers.Main).launch {
                            isProcessing = true
                            val extendedBitmap = extendImage(imageBitmap!!, leftP, rightP, upP, downP)
                            isProcessing = false
                            if (extendedBitmap != null) {
                                imageBitmap = extendedBitmap
                                ImageHolder.generatedImage = extendedBitmap
                                saveIndicator = SaveIndicator.SUCCESS
                            } else {
                                saveIndicator = SaveIndicator.NO_IMAGE
                            }
                        }
                    },
                    onClose = { showExtendScreen = false }
                )
            }
        }
    }

    // Background Replacer bottom sheet
    if (showBgReplaceSheet && imageBitmap != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showBgReplaceSheet = false },
            sheetState = sheetState,
        ) {
            BackgroundReplacerScreen(
                currentImage = imageBitmap!!,
                onReplace = { newBitmap ->
                    imageBitmap = newBitmap
                    ImageHolder.generatedImage = newBitmap
                    showBgReplaceSheet = false
                    saveIndicator = SaveIndicator.SUCCESS
                },
                onClose = { showBgReplaceSheet = false }
            )
        }
    }
}
*/

*/
 */


//--------------------------------------------------------------
//  Animated gradient title composable (same vibe as Rephrase)
//--------------------------------------------------------------

@Composable
fun AnimatedGradientTitle(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineMedium
) {
    var visibleText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // On first composition animate the characters appearing
    LaunchedEffect(text) {
        visibleText = ""
        text.forEachIndexed { i, _ ->
            visibleText = text.substring(0, i + 1)
            delay(35)
        }
    }
    val gradientBrush = Brush.horizontalGradient(
        listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        )
    )
    Text(
        text = visibleText,
        style = style.merge(
            TextStyle(
                brush = gradientBrush,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
        ),
        modifier = modifier
    )
}

//--------------------------------------------------------------
//  Main screen – refreshed & on‑trend ✨
//--------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EditImageScreen(
    onBackClick: () -> Unit
) {


    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    /* ───── state ───── */
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isProcessing by remember { mutableStateOf(false) }

    // bottom‑sheet toggles
    var showBgReplaceSheet by remember { mutableStateOf(false) }
    var showInpaintMode     by remember { mutableStateOf(false) }
    var showRemoveObjectMode    by remember { mutableStateOf(false) }
    //var showExtendSheet     by remember { mutableStateOf(false) }


    //val context = LocalContext.current
    //var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var saveIndicator by remember { mutableStateOf<SaveIndicator?>(null) }
    //var isProcessing by remember { mutableStateOf(false) } // For API call loading indicator

    // Bottom sheets
    //var showBgReplaceSheet by remember { mutableStateOf(false) }
    //var showInpaintMode by remember { mutableStateOf(false) }
    //var showRemoveObjectMode by remember { mutableStateOf(false) }
    var showExtendScreen by remember { mutableStateOf(false) }

    // Load the ImageHolder image if not already set
    LaunchedEffect(Unit) {
        if (imageBitmap == null && ImageHolder.generatedImage != null) {
            imageBitmap = ImageHolder.generatedImage
        }
    }

    /* ───── gallery picker ───── */
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { stream ->
                imageBitmap = BitmapFactory.decodeStream(stream)
                ImageHolder.generatedImage = imageBitmap
            }
        }
    }
    fun CoroutineScope.replaceBackgroundAsync(
        prompt: String,
        bitmap: Bitmap,
        onResult: (Bitmap?) -> Unit,
        setProcessing: (Boolean) -> Unit
    ) = launch {
        setProcessing(true)
        val result = replaceBackgroundWithPrompt(bitmap, prompt)
        setProcessing(false)
        onResult(result)
    }
    /* ───── scaffold ───── */
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { AnimatedGradientTitle("Edit Image") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = "Upload")
                    }
                    IconButton(onClick = {
                        imageBitmap?.let {
                            val saved = saveImageToGallery(context, it)
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (saved) "Image saved to gallery!" else "Error saving image"
                                )
                            }
                        } ?: scope.launch {
                            snackbarHostState.showSnackbar("No image to save")
                        }
                    }) {
                        Icon(Icons.Default.Download, contentDescription = "Save")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (!showInpaintMode && !showBgReplaceSheet && !showRemoveObjectMode && !showExtendScreen) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)) {
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            if (imageBitmap != null) {
                                isProcessing = true
                                scope.launch {
                                    val processed = removeBackgroundFromImage(imageBitmap!!)
                                    if (processed != null) imageBitmap = processed
                                    isProcessing = false
                                }
                            } else scope.launch { snackbarHostState.showSnackbar("No image loaded") }
                        },
                        icon = { Icon(Icons.Default.Crop, null) },
                        label = { Text("BgRemove") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            if (imageBitmap != null) showBgReplaceSheet =
                                true else scope.launch { snackbarHostState.showSnackbar("No image loaded") }
                        },
                        icon = { Icon(Icons.Default.Colorize, null) },
                        label = { Text("BgReplace") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            if (imageBitmap != null) showInpaintMode =
                                true else scope.launch { snackbarHostState.showSnackbar("No image loaded") }
                        },
                        icon = { Icon(Icons.Default.Brush, null) },
                        label = { Text("Inpaint") }
                    )

                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            if (imageBitmap != null) showRemoveObjectMode =
                                true else scope.launch { snackbarHostState.showSnackbar("No image loaded") }
                        },
                        icon = { Icon(Icons.Default.Delete, null) },
                        label = { Text("Erase") }
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            if (imageBitmap != null) showExtendScreen =
                                true else scope.launch { snackbarHostState.showSnackbar("No image loaded") }
                        },
                        icon = { Icon(Icons.Default.CropRotate, null) },
                        label = { Text("Extend") }
                    )
                }
            }
        }
    ) { pv ->

        /* ───── content ───── */
        Box(
            Modifier
                .padding(pv)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            imageBitmap?.let { bmp ->
                //val ratio = if (maxWidth < maxHeight) 3f / 4f else 16f / 9f
                Card(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                        .fillMaxWidth()
                        .aspectRatio(3f/4f),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Image(
                        bitmap = bmp.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(if (isProcessing) 12.dp else 0.dp)
                    )
                }
            } ?: Text(
                "Select or generate an image to get started",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.Center)
            )

            if (isProcessing) {
                Box(Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        /* ───── sheets & overlays – keep existing implementation ───── */
        // Show Inpainting Overlay if requested
        if (showInpaintMode && imageBitmap != null) {
            InpaintingOverlay(
                originalImage = imageBitmap!!,
                onApply = { mask, prompt ->
                    showInpaintMode = false
                    CoroutineScope(Dispatchers.Main).launch {
                        isProcessing = true
                        // Call your real inpainting logic here:
                        val result = inpaintImage(imageBitmap!!, mask, prompt)
                        isProcessing = false
                        result?.let {
                            imageBitmap = it
                            saveIndicator = SaveIndicator.SUCCESS
                        } ?: run {
                            saveIndicator = SaveIndicator.NO_IMAGE
                        }
                        showInpaintMode = false
                    }
                },
                onClose = { showInpaintMode = false }
            )
        }

        // Show Inpainting Overlay if requested
        if (showRemoveObjectMode && imageBitmap != null) {
            RemoveObjectOverlay(
                originalImage = imageBitmap!!,
                onApply = { mask ->
                    showRemoveObjectMode = false
                    CoroutineScope(Dispatchers.Main).launch {
                        isProcessing = true
                        // Call your real inpainting logic here:
                        val result = removeObjectFromImage(imageBitmap!!, mask)
                        isProcessing = false
                        result?.let {
                            imageBitmap = it
                            saveIndicator = SaveIndicator.SUCCESS
                        } ?: run {
                            saveIndicator = SaveIndicator.NO_IMAGE
                        }
                        showRemoveObjectMode = false
                    }
                },
                onClose = { showRemoveObjectMode = false }
            )
        }
        // Show ExtendOverlay if requested
        if (showExtendScreen && imageBitmap != null) {
            ExtendOverlay(
                originalImage = imageBitmap!!,
                onApply = { leftP, rightP, upP, downP ->
                    // The user pressed "Apply" with the chosen percentages
                    showExtendScreen = false
                    CoroutineScope(Dispatchers.Main).launch {
                        isProcessing = true
                        val extendedBitmap = extendImage(imageBitmap!!, leftP, rightP, upP, downP)
                        isProcessing = false
                        if (extendedBitmap != null) {
                            imageBitmap = extendedBitmap
                            ImageHolder.generatedImage = extendedBitmap
                            saveIndicator = SaveIndicator.SUCCESS
                        } else {
                            saveIndicator = SaveIndicator.NO_IMAGE
                        }
                    }
                },
                onClose = { showExtendScreen = false }
            )
        }
    }
    // Background Replacer bottom sheet
    if (showBgReplaceSheet && imageBitmap != null) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        LaunchedEffect(Unit) { sheetState.show() }
        ModalBottomSheet(
            onDismissRequest = { showBgReplaceSheet = false },
            sheetState = sheetState
        ) {
            BackgroundReplacerScreen(
                currentImage = imageBitmap!!,
                onRequestReplace = { prompt ->
                    // 1) close sheet
                    showBgReplaceSheet = false
                    // 2) run replacement in parent scope
                    scope.replaceBackgroundAsync(
                        prompt,
                        imageBitmap!!,
                        onResult = { bmp ->
                            bmp?.let {
                                imageBitmap = it
                                ImageHolder.generatedImage = it
                                saveIndicator = SaveIndicator.SUCCESS
                            } ?: run { saveIndicator = SaveIndicator.NO_IMAGE }
                        },
                        setProcessing = { isProcessing = it }
                    )
                },
                onClose = { showBgReplaceSheet = false }
            )
        }
    }
}

/*
@Composable
fun BackgroundReplacerScreen(
    currentImage: Bitmap,
    onReplace: (Bitmap) -> Unit,
    onClose: () -> Unit
) {
    var promptText by remember { mutableStateOf("") }
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

        Text("Background Replacer", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Enter text for new background:")

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

        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    // Replace background with prompt
                    val replacedImage = replaceBackgroundWithPrompt(currentImage, promptText)
                    isLoading = false
                    if (replacedImage != null) {
                        onReplace(replacedImage)
                    } else {
                        // Handle error
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Replace")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
*/
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun BackgroundReplacerScreen(
    currentImage: Bitmap,
    onRequestReplace: (String) -> Unit,
    onClose: () -> Unit,

) {
    /* ── state & helpers ─────────────────────────────────── */
    var promptText by remember { mutableStateOf("") }
    val scope      = rememberCoroutineScope()

    /* typing animation for the header */
    val fullHeader   = "Replace Background"
    var typedHeader  by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        fullHeader.forEachIndexed { i, _ ->
            typedHeader = fullHeader.substring(0, i + 1)
            delay(45)
        }
    }

    /* gradient headline style (same as GenerateImageScreen) */
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

    /* ── layout ──────────────────────────────────────────── */
    Box(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.65f)        // height similar to GenerateImageScreen
    ) {
        BottomWaveGradient(                     // reuse your wave backdrop
            modifier   = Modifier.matchParentSize(),
            bandHeight = 200.dp,
            fadeHeight = 100.dp,
            waveAmp    = 24.dp,
            waveLen    = 600.dp
        )

        /* main content */
        Column(Modifier.matchParentSize().padding(24.dp)) {

            /* header row */
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(typedHeader, style = gradientStyle)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = onClose) { Icon(Icons.Default.Close, null) }
            }

            Spacer(Modifier.height(8.dp))

            /* prompt input */
            Text("New background prompt", style = MaterialTheme.typography.labelMedium)
            OutlinedTextField(
                value         = promptText,
                onValueChange = { promptText = it },
                placeholder   = { Text("Describe the background you want…") },
                leadingIcon   = { Icon(Icons.Default.Photo, null) },
                shape         = MaterialTheme.shapes.medium,
                maxLines      = 4,
                modifier      = Modifier.fillMaxWidth()
            )

            /* quick suggestions */
            val suggestions = listOf(
                "Sunset beach",
                "Snow-covered mountains",
                "Studio gradient",
                "Futuristic cityscape"
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.horizontalScroll(rememberScrollState())) {
                suggestions.forEach { s ->
                    AssistChip(
                        onClick = { promptText = s },
                        label   = { Text(s, maxLines = 1) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            /* original image preview (optional) */
            Card(
                shape     = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(),
                modifier  = Modifier.fillMaxWidth()
            ) {
                Image(
                    bitmap           = currentImage.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }

            Spacer(Modifier.height(70.dp))       // keep space for the FAB
        }

        /* floating REPLACE button */
        AnimatedVisibility(
            visible = promptText.isNotBlank(),
            enter   = scaleIn() + fadeIn(),
            exit    = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-24).dp, y = (-24).dp)
        ) {
            ExtendedFloatingActionButton(
                icon  = { Icon(Icons.Default.Check, null) },
                text  = { Text("Replace") },
                onClick = { onRequestReplace(promptText) }   // just fire the request
            )
        }

    }
}



/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InpaintingOverlay(
    originalImage: Bitmap,
    onApply: (mask: Bitmap, prompt: String) -> Unit,
    onClose: () -> Unit
) {
    var promptText by remember { mutableStateOf("") }
    var brushMode by remember { mutableStateOf(true) } // true = brush, false = eraser
    var refreshTrigger by remember { mutableStateOf(0) }
    var previousPoint by remember { mutableStateOf<Offset?>(null) }

    // The mask must match the original image dimensions (in pixels)
    val maskBitmap = remember {
        Bitmap.createBitmap(
            originalImage.width,
            originalImage.height,
            Bitmap.Config.ARGB_8888
        ).apply { eraseColor(androidx.compose.ui.graphics.Color.Transparent.toArgb()) }
    }

    // Paint configs
    val rawBrushSize = 20f

    val brushPaint = remember {
        Paint().apply {
            color = android.graphics.Color.WHITE
            strokeWidth = rawBrushSize
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    }
    val eraserPaint = remember {
        Paint().apply {
            color = android.graphics.Color.TRANSPARENT
            strokeWidth = rawBrushSize
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    }

    val context = LocalContext.current
    val aspectRatio = originalImage.width.toFloat() / originalImage.height.toFloat()
    // The density factor (dp → px)
    val density = LocalDensity.current.density

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        // Center the image (and Canvas) with the original aspect ratio
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        ) {
            // Draw the original image behind
            Image(
                bitmap = originalImage.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            // Canvas for drawing the mask on top
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(brushMode) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                // Convert from Compose coordinates (dp) to mask's pixel coordinates
                                val scaleX = maskBitmap.width / (size.width * density)
                                val scaleY = maskBitmap.height / (size.height * density)

                                val canvas = android.graphics.Canvas(maskBitmap)
                                val x = offset.x * density * scaleX
                                val y = offset.y * density * scaleY

                                // Optionally scale the strokeWidth if you want it in dp:
                                if (brushMode) {
                                    brushPaint.strokeWidth = rawBrushSize * density
                                } else {
                                    eraserPaint.strokeWidth = rawBrushSize * density
                                }

                                canvas.drawCircle(
                                    x,
                                    y,
                                    (rawBrushSize * density) / 2,
                                    if (brushMode) brushPaint else eraserPaint
                                )
                                previousPoint = offset
                                refreshTrigger++
                            },
                            onDrag = { change, _ ->
                                val currentPoint = change.position
                                val scaleX = maskBitmap.width / (size.width * density)
                                val scaleY = maskBitmap.height / (size.height * density)

                                val canvas = android.graphics.Canvas(maskBitmap)
                                previousPoint?.let { prev ->
                                    val prevX = prev.x * density * scaleX
                                    val prevY = prev.y * density * scaleY
                                    val curX = currentPoint.x * density * scaleX
                                    val curY = currentPoint.y * density * scaleY

                                    if (brushMode) {
                                        brushPaint.strokeWidth = rawBrushSize * density
                                        canvas.drawLine(prevX, prevY, curX, curY, brushPaint)
                                    } else {
                                        eraserPaint.strokeWidth = rawBrushSize * density
                                        canvas.drawLine(prevX, prevY, curX, curY, eraserPaint)
                                    }
                                }
                                previousPoint = currentPoint
                                refreshTrigger++
                            },
                            onDragEnd = {
                                previousPoint = null
                            }
                        )
                    }
            ) {
                // Use refreshTrigger to force redrawing when user draws
                val dummy = refreshTrigger

                // Draw the mask (scaled to the canvas size) as a semi-transparent overlay
                drawImage(
                    image = maskBitmap.asImageBitmap(),
                    dstSize = IntSize(this.size.width.toInt(), this.size.height.toInt()),
                    alpha = 0.5f
                )
            }
        }

        // Bottom panel with controls
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(16.dp)
                    .heightIn(min = 200.dp, max = 250.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    label = { Text("Enter inpainting prompt") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                val scrollState = rememberScrollState()
                Row(
                    modifier = Modifier.fillMaxWidth()
                    .horizontalScroll(scrollState),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { brushMode = true }) {
                        Text("Brush")
                    }
                    Button(onClick = { brushMode = false }) {
                        Text("Eraser")
                    }
                    Button(onClick = {
                        // Optional debug save just the mask to gallery
                        val result = saveImageToGallery(context, maskBitmap)
                        Toast
                            .makeText(
                                context,
                                if (result) "Mask saved successfully" else "Failed to save mask",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }) {
                        Text("Save Mask")
                    }
                    Button(onClick = {
                        // Send back the mask + prompt
                        onApply(maskBitmap, promptText)
                    }) {
                        Text("Apply")
                    }
                    Button(onClick = onClose) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun InpaintingOverlay(
    originalImage: Bitmap,
    onApply: (mask: Bitmap, prompt: String) -> Unit,
    onClose: () -> Unit
) {
    /* ── state ─────────────────────────────────────────────── */
    var brushMode   by remember { mutableStateOf(true) }   // true = brush, false = erase
    var promptText  by remember { mutableStateOf("") }
    var prevPoint   by remember { mutableStateOf<Offset?>(null) }
    var refreshKey  by remember { mutableStateOf(0) }
    var brushSizeDp by remember { mutableStateOf(20f) }

    val maskBitmap = remember {
        Bitmap
            .createBitmap(originalImage.width, originalImage.height, Bitmap.Config.ARGB_8888)
            .apply { eraseColor(Color.Transparent.toArgb()) }
    }

    val brushPaint = remember {
        Paint().apply {
            color       = android.graphics.Color.WHITE
            style       = Paint.Style.STROKE
            strokeCap   = Paint.Cap.ROUND
            isAntiAlias = true
        }
    }
    val eraserPaint = remember {
        Paint().apply {
            color       = android.graphics.Color.TRANSPARENT
            style       = Paint.Style.STROKE
            strokeCap   = Paint.Cap.ROUND
            xfermode    = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            isAntiAlias = true
        }
    }

    val ctx        = LocalContext.current
    val density    = LocalDensity.current.density
    val aspect     = originalImage.width.toFloat() / originalImage.height
    val controlTint = MaterialTheme.colorScheme.onSurfaceVariant   // ← neutral icon colour
    val neutral   = MaterialTheme.colorScheme.onSurfaceVariant        // matches top/bottom-bar icons
    val greyChip  = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .9f)

    /* ── UI ─────────────────────────────────────────────────── */
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
    ) {

        /* ── image + drawing canvas ── */
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .offset(y = (-64).dp)
                .aspectRatio(aspect)
        ) {
            Image(
                bitmap = originalImage.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            Canvas(
                Modifier
                    .fillMaxSize()
                    .pointerInput(brushMode, brushSizeDp) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val canvas  = android.graphics.Canvas(maskBitmap)
                                val scaleX  = maskBitmap.width  / (size.width * density)
                                val scaleY  = maskBitmap.height / (size.height * density)
                                val pxSize  = brushSizeDp * density
                                val x       = offset.x * density * scaleX
                                val y       = offset.y * density * scaleY

                                if (brushMode) brushPaint.strokeWidth = pxSize
                                else           eraserPaint.strokeWidth = pxSize

                                canvas.drawCircle(
                                    x, y, pxSize / 2,
                                    if (brushMode) brushPaint else eraserPaint
                                )
                                prevPoint = offset; refreshKey++
                            },
                            onDrag = { change, _ ->
                                val cur     = change.position
                                val canvas  = android.graphics.Canvas(maskBitmap)
                                val scaleX  = maskBitmap.width  / (size.width * density)
                                val scaleY  = maskBitmap.height / (size.height * density)
                                val pxSize  = brushSizeDp * density

                                prevPoint?.let { p ->
                                    val pX = p.x * density * scaleX
                                    val pY = p.y * density * scaleY
                                    val cX = cur.x * density * scaleX
                                    val cY = cur.y * density * scaleY

                                    if (brushMode) {
                                        brushPaint.strokeWidth = pxSize
                                        canvas.drawLine(pX, pY, cX, cY, brushPaint)
                                    } else {
                                        eraserPaint.strokeWidth = pxSize
                                        canvas.drawLine(pX, pY, cX, cY, eraserPaint)
                                    }
                                }
                                prevPoint = cur; refreshKey++
                            },
                            onDragEnd = { prevPoint = null }
                        )
                    }
            ) {
                val dummy = refreshKey
                drawImage(
                    maskBitmap.asImageBitmap(),
                    dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                    alpha   = 0.5f
                )
            }
        }

        /* ── bottom controls sheet ── */
        Surface(
            tonalElevation = 3.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(Modifier.fillMaxWidth().padding(24.dp)) {

                /* prompt */
                OutlinedTextField(
                    value         = promptText,
                    onValueChange = { promptText = it },
                    label         = { Text("Prompt") },
                    leadingIcon   = { Icon(Icons.Default.TextFields, null, tint = controlTint) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = controlTint,
                        focusedLabelColor  = controlTint,
                        cursorColor        = controlTint
                    ),
                    modifier      = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                /* brush / eraser + size */
                Row(verticalAlignment = Alignment.CenterVertically) {

                    /* Brush chip */
                    IconToggleButton(
                        checked           = brushMode,
                        onCheckedChange   = { brushMode = true },
                        colors = IconButtonDefaults.iconToggleButtonColors(
                            contentColor         = neutral,
                            containerColor       = Color.Transparent,
                            checkedContainerColor = greyChip,
                            checkedContentColor   = neutral
                        )
                    ) { Icon(Icons.Default.Brush, null) }

                    Spacer(Modifier.width(8.dp))

                    IconToggleButton(
                        checked           = !brushMode,
                        onCheckedChange   = { brushMode = false },
                        colors = IconButtonDefaults.iconToggleButtonColors(
                            contentColor         = neutral,
                            containerColor       = Color.Transparent,
                            checkedContainerColor = greyChip,
                            checkedContentColor   = neutral
                        )
                    ) { Icon(Icons.Default.Delete, null) }

                    Spacer(Modifier.width(16.dp))

                    Slider(
                        value = brushSizeDp,
                        onValueChange = { brushSizeDp = it },
                        valueRange = 4f..64f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            activeTrackColor   = controlTint,
                            inactiveTrackColor = controlTint.copy(alpha = 0.3f),
                            thumbColor         = controlTint
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(4.dp))

                /* back + save mask */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = controlTint
                        )
                    }

                    Spacer(Modifier.weight(1f))

                }
            }
        }

        /* ── floating APPLY FAB ── */
        AnimatedVisibility(
            visible  = promptText.isNotBlank(),
            enter    = scaleIn() + fadeIn(),
            exit     = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-24).dp, y = (-24).dp)
        ) {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Default.PlayArrow, null) },
                text = { Text("Apply") },
                onClick = { onApply(maskBitmap, promptText) },
                containerColor = greyChip,   // same grey used for selected chips
                contentColor   = neutral
            )
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemoveObjectOverlay(
    originalImage: Bitmap,
    onApply: (mask: Bitmap) -> Unit,
    onClose: () -> Unit
) {
    /* ── state ─────────────────────────────────────────────── */
    var brushMode   by remember { mutableStateOf(true) }   // true = brush, false = erase
    var hasDrawn      by remember { mutableStateOf(false) }
    //var promptText  by remember { mutableStateOf("") }
    var prevPoint   by remember { mutableStateOf<Offset?>(null) }
    var refreshKey  by remember { mutableStateOf(0) }
    var brushSizeDp by remember { mutableStateOf(20f) }

    val maskBitmap = remember {
        Bitmap
            .createBitmap(originalImage.width, originalImage.height, Bitmap.Config.ARGB_8888)
            .apply { eraseColor(Color.Transparent.toArgb()) }
    }

    val brushPaint = remember {
        Paint().apply {
            color       = android.graphics.Color.WHITE
            style       = Paint.Style.STROKE
            strokeCap   = Paint.Cap.ROUND
            isAntiAlias = true
        }
    }
    val eraserPaint = remember {
        Paint().apply {
            color       = android.graphics.Color.TRANSPARENT
            style       = Paint.Style.STROKE
            strokeCap   = Paint.Cap.ROUND
            xfermode    = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
            isAntiAlias = true
        }
    }

    val ctx        = LocalContext.current
    val density    = LocalDensity.current.density
    val aspect     = originalImage.width.toFloat() / originalImage.height
    val controlTint = MaterialTheme.colorScheme.onSurfaceVariant   // ← neutral icon colour
    val neutral   = MaterialTheme.colorScheme.onSurfaceVariant        // matches top/bottom-bar icons
    val greyChip  = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .9f)

    /* ── UI ─────────────────────────────────────────────────── */
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.55f))
    ) {

        /* ── image + drawing canvas ── */
        Box(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .offset(y = (-16).dp)
                .aspectRatio(aspect)
        ) {
            Image(
                bitmap = originalImage.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            Canvas(
                Modifier
                    .fillMaxSize()
                    .pointerInput(brushMode, brushSizeDp) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val canvas  = android.graphics.Canvas(maskBitmap)
                                val scaleX  = maskBitmap.width  / (size.width * density)
                                val scaleY  = maskBitmap.height / (size.height * density)
                                val pxSize  = brushSizeDp * density
                                val x       = offset.x * density * scaleX
                                val y       = offset.y * density * scaleY

                                if (brushMode)
                                {
                                    brushPaint.strokeWidth = pxSize
                                    hasDrawn = true
                                }
                                else           eraserPaint.strokeWidth = pxSize

                                canvas.drawCircle(
                                    x, y, pxSize / 2,
                                    if (brushMode) brushPaint else eraserPaint
                                )
                                prevPoint = offset; refreshKey++
                            },
                            onDrag = { change, _ ->
                                val cur     = change.position
                                val canvas  = android.graphics.Canvas(maskBitmap)
                                val scaleX  = maskBitmap.width  / (size.width * density)
                                val scaleY  = maskBitmap.height / (size.height * density)
                                val pxSize  = brushSizeDp * density

                                prevPoint?.let { p ->
                                    val pX = p.x * density * scaleX
                                    val pY = p.y * density * scaleY
                                    val cX = cur.x * density * scaleX
                                    val cY = cur.y * density * scaleY

                                    if (brushMode) {
                                        brushPaint.strokeWidth = pxSize
                                        canvas.drawLine(pX, pY, cX, cY, brushPaint)
                                        hasDrawn = true
                                    } else {
                                        eraserPaint.strokeWidth = pxSize
                                        canvas.drawLine(pX, pY, cX, cY, eraserPaint)
                                    }
                                }
                                prevPoint = cur; refreshKey++
                            },
                            onDragEnd = { prevPoint = null }
                        )
                    }
            ) {
                val dummy = refreshKey
                drawImage(
                    maskBitmap.asImageBitmap(),
                    dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                    alpha   = 0.5f
                )
            }
        }

        /* ── bottom controls sheet ── */
        Surface(
            tonalElevation = 3.dp,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(Modifier.fillMaxWidth().padding(24.dp)) {


                /* brush / eraser + size */
                Row(verticalAlignment = Alignment.CenterVertically) {

                    /* Brush chip */
                    IconToggleButton(
                        checked           = brushMode,
                        onCheckedChange   = { brushMode = true },
                        colors = IconButtonDefaults.iconToggleButtonColors(
                            contentColor         = neutral,
                            containerColor       = Color.Transparent,
                            checkedContainerColor = greyChip,
                            checkedContentColor   = neutral
                        )
                    ) { Icon(Icons.Default.Brush, null) }

                    Spacer(Modifier.width(8.dp))

                    IconToggleButton(
                        checked           = !brushMode,
                        onCheckedChange   = { brushMode = false },
                        colors = IconButtonDefaults.iconToggleButtonColors(
                            contentColor         = neutral,
                            containerColor       = Color.Transparent,
                            checkedContainerColor = greyChip,
                            checkedContentColor   = neutral
                        )
                    ) { Icon(Icons.Default.Delete, null) }

                    Spacer(Modifier.width(16.dp))

                    Slider(
                        value = brushSizeDp,
                        onValueChange = { brushSizeDp = it },
                        valueRange = 4f..64f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            activeTrackColor   = controlTint,
                            inactiveTrackColor = controlTint.copy(alpha = 0.3f),
                            thumbColor         = controlTint
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(16.dp))

                /* back + save mask */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = controlTint
                        )
                    }

                    Spacer(Modifier.weight(1f))

                }
            }
        }

        /* ── floating APPLY FAB ── */
        AnimatedVisibility(
            visible  = hasDrawn,
            enter    = scaleIn() + fadeIn(),
            exit     = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-24).dp, y = (-24).dp)
        ) {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Default.PlayArrow, null) },
                text = { Text("Apply") },
                onClick = { onApply(maskBitmap) },
                containerColor = greyChip,   // same grey used for selected chips
                contentColor   = neutral
            )
        }
    }
}
/*
@Composable
fun ExtendOverlay(
    originalImage: Bitmap,
    onApply: (leftPixels: Int, rightPixels: Int, upPixels: Int, downPixels: Int) -> Unit,
    onClose: () -> Unit
) {
    // Sliders store percentages 0..100
    var leftPercent by remember { mutableStateOf(0) }
    var rightPercent by remember { mutableStateOf(0) }
    var upPercent by remember { mutableStateOf(0) }
    var downPercent by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Extend Image",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // LEFT Slider
            Text("Left: $leftPercent%")
            Slider(
                value = leftPercent.toFloat(),
                onValueChange = { newVal ->
                    leftPercent = newVal.toInt().coerceIn(0, 100)
                },
                valueRange = 0f..100f
            )

            Spacer(modifier = Modifier.height(16.dp))

            // RIGHT Slider
            Text("Right: $rightPercent%")
            Slider(
                value = rightPercent.toFloat(),
                onValueChange = { newVal ->
                    rightPercent = newVal.toInt().coerceIn(0, 100)
                },
                valueRange = 0f..100f
            )

            Spacer(modifier = Modifier.height(16.dp))

            // UP Slider
            Text("Up: $upPercent%")
            Slider(
                value = upPercent.toFloat(),
                onValueChange = { newVal ->
                    upPercent = newVal.toInt().coerceIn(0, 100)
                },
                valueRange = 0f..100f
            )

            Spacer(modifier = Modifier.height(16.dp))

            // DOWN Slider
            Text("Down: $downPercent%")
            Slider(
                value = downPercent.toFloat(),
                onValueChange = { newVal ->
                    downPercent = newVal.toInt().coerceIn(0, 100)
                },
                valueRange = 0f..100f
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onClose) {
                    Text("Cancel")
                }
                Button(onClick = {
                    // Convert the slider percentages to pixel values:
                    val leftPixels = ((leftPercent / 100f) * originalImage.width).toInt()
                    val rightPixels = ((rightPercent / 100f) * originalImage.width).toInt()
                    val upPixels = ((upPercent / 100f) * originalImage.height).toInt()
                    val downPixels = ((downPercent / 100f) * originalImage.height).toInt()

                    // Now pass those real pixel values to your onApply callback
                    onApply(leftPixels, rightPixels, upPixels, downPixels)
                }) {
                    Text("Apply")
                }
            }
        }
    }
}
*/

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ExtendOverlay(
    originalImage: Bitmap,
    onApply: (leftPixels: Int, rightPixels: Int, upPixels: Int, downPixels: Int) -> Unit,
    onClose: () -> Unit
) {
    /* internal state – keep the logic exactly the same */
    var leftPct  by remember { mutableStateOf(0) }
    var rightPct by remember { mutableStateOf(0) }
    var upPct    by remember { mutableStateOf(0) }
    var downPct  by remember { mutableStateOf(0) }

    /* palette helpers to stay consistent with InpaintingOverlay */
    val neutral  = MaterialTheme.colorScheme.onSurfaceVariant
    val greyChip = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = .9f)

    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = .55f))
    ) {

        /* ───────── bottom sheet style container ───────── */
        Surface(
            tonalElevation = 3.dp,
            color  = Color.White,
            shape  = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                /* gradient title for brand consistency */
                val titleGradient = Brush.horizontalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary
                    )
                )
                Text(
                    "Extend Image",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        brush       = titleGradient,
                        fontWeight  = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                )

                Spacer(Modifier.height(24.dp))

                /* direction sliders ---------------------------------------------------- */
                @Composable
                fun DirectionRow(
                    label: String,
                    percent: Int,
                    onChange: (Int) -> Unit,
                    icon: ImageVector
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, null, tint = neutral)
                        Spacer(Modifier.width(8.dp))
                        Text("$label: $percent%", style = MaterialTheme.typography.bodyMedium)
                    }
                    Slider(
                        value = percent.toFloat(),
                        onValueChange = { onChange(it.toInt().coerceIn(0, 100)) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            activeTrackColor   = neutral,
                            inactiveTrackColor = neutral.copy(alpha = .3f),
                            thumbColor         = neutral
                        )
                    )
                    Spacer(Modifier.height(16.dp))
                }

                DirectionRow("Left" , leftPct , { leftPct  = it }, Icons.Default.KeyboardArrowLeft )
                DirectionRow("Right", rightPct, { rightPct = it }, Icons.Default.KeyboardArrowRight)
                DirectionRow("Up"   , upPct   , { upPct    = it }, Icons.Default.KeyboardArrowUp   )
                DirectionRow("Down" , downPct , { downPct  = it }, Icons.Default.KeyboardArrowDown )

                /* back arrow only */
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = neutral)
                    }
                }
            }
        }

        /* floating APPLY fab ---------------------------------------------------------- */
        val anyChange = leftPct + rightPct + upPct + downPct > 0
        AnimatedVisibility(
            visible = anyChange,
            enter   = scaleIn() + fadeIn(),
            exit    = scaleOut() + fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-24).dp, y = (-24).dp)
        ) {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Default.PlayArrow, null) },
                text = { Text("Apply") },
                containerColor = greyChip,   // same grey used for selected chips
                contentColor   = neutral,
                onClick = {
                    val lp = ((leftPct  / 100f) * originalImage.width ).toInt()
                    val rp = ((rightPct / 100f) * originalImage.width ).toInt()
                    val up = ((upPct    / 100f) * originalImage.height).toInt()
                    val dp = ((downPct  / 100f) * originalImage.height).toInt()
                    onApply(lp, rp, up, dp)
                }

            )
        }
    }
}

suspend fun inpaintImage(
    original: Bitmap,
    mask: Bitmap,
    prompt: String
): Bitmap? = withContext(Dispatchers.IO) {
    try {
        // Convert original image and mask to byte arrays
        val originalStream = ByteArrayOutputStream()
        original.compress(Bitmap.CompressFormat.JPEG, 90, originalStream)
        val originalBytes = originalStream.toByteArray()

        val maskStream = ByteArrayOutputStream()
        // Use PNG to preserve transparency if needed
        mask.compress(Bitmap.CompressFormat.PNG, 100, maskStream)
        val maskBytes = maskStream.toByteArray()

        // Create the multipart request body
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "original", "original.jpg",
                originalBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            .addFormDataPart(
                "mask", "mask.png",
                maskBytes.toRequestBody("image/png".toMediaTypeOrNull())
            )
            .addFormDataPart("prompt", prompt)
            .build()

        val request = Request.Builder()
            .url("https://us-central1-speedy-web-456812-s7.cloudfunctions.net/insert_object")
            .post(multipartBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
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

suspend fun removeObjectFromImage(
    original: Bitmap,
    mask: Bitmap,
): Bitmap? = withContext(Dispatchers.IO) {
    try {
        // Convert original image and mask to byte arrays
        val originalStream = ByteArrayOutputStream()
        original.compress(Bitmap.CompressFormat.JPEG, 90, originalStream)
        val originalBytes = originalStream.toByteArray()

        val maskStream = ByteArrayOutputStream()
        // Use PNG to preserve transparency if needed
        mask.compress(Bitmap.CompressFormat.PNG, 100, maskStream)
        val maskBytes = maskStream.toByteArray()

        // Create the multipart request body
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "original", "original.jpg",
                originalBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            .addFormDataPart(
                "mask", "mask.png",
                maskBytes.toRequestBody("image/png".toMediaTypeOrNull())
            )
            .build()

        val request = Request.Builder()
            .url("https://us-central1-speedy-web-456812-s7.cloudfunctions.net/remove_object")
            .post(multipartBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
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

suspend fun removeBackgroundFromImage(bitmap: Bitmap): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            // Convert bitmap to JPEG byte array
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            val byteArray = stream.toByteArray()

            // Create the requestBody for the image
            val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())

            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg", requestBody)
                .build()

            val request = Request.Builder()
                .url("https://us-central1-speedy-web-456812-s7.cloudfunctions.net/remove_background")
                .post(multipartBody)
                .build()

            // Create OkHttpClient
            val client = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            // Execute request
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                // Get the image bytes from the response and decode them into a Bitmap
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

suspend fun replaceBackgroundWithPrompt(bitmap: Bitmap, prompt: String): Bitmap? =
    withContext(Dispatchers.IO) {
        try {
            // Convert the bitmap to a JPEG byte array
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            val byteArray = stream.toByteArray()
            // Create the RequestBody for the image
            val imageRequestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull())

            // Build the multipart body
            val multipartBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "image.jpg", imageRequestBody)
                .addFormDataPart("prompt", prompt)
                .build()

            // Update the URL to your backend endpoint.
            val request = Request.Builder()
                .url("https://us-central1-speedy-web-456812-s7.cloudfunctions.net/replace_background")
                .post(multipartBody)
                .build()

            val client = OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
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

suspend fun extendImage(
    original: Bitmap,
    leftPercent: Int,
    rightPercent: Int,
    upPercent: Int,
    downPercent: Int
): Bitmap? = withContext(Dispatchers.IO) {
    try {
        // Convert the bitmap to JPEG (or PNG) bytes
        val stream = ByteArrayOutputStream()
        original.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        val originalBytes = stream.toByteArray()

        // We'll send the image plus the 4 percentages to your new "extend_image" backend
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "original", "original.jpg",
                originalBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            // These must match your backend param names exactly
            .addFormDataPart("left", leftPercent.toString())
            .addFormDataPart("right", rightPercent.toString())
            .addFormDataPart("up", upPercent.toString())
            .addFormDataPart("down", downPercent.toString())
            .build()

        val request = Request.Builder()
            // Update with your actual endpoint
            .url("https://us-central1-speedy-web-456812-s7.cloudfunctions.net/extend_image")
            .post(multipartBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
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
