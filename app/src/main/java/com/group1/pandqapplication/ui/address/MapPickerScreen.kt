package com.group1.pandqapplication.ui.address

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPickerScreen(
    onLocationSelected: (lat: Double, lon: Double, address: String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: MapPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val primaryColor = Color(0xFFec3713)
    val snackbarHostState = remember { SnackbarHostState() }
    
    var mapView: MapView? by remember { mutableStateOf(null) }
    var marker: Marker? by remember { mutableStateOf(null) }

    // Location permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        // Handle permission result
    }

    LaunchedEffect(Unit) {
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", 0))
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            // Search bar and back button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // Top bar with back button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF3F4F6))
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1F2937)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Chọn vị trí trên bản đồ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }

                // Search bar
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { 
                        viewModel.updateSearchQuery(it)
                        if (it.length > 2) {
                            viewModel.searchLocation(it)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Tìm kiếm địa điểm...") },
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = primaryColor)
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = Color(0xFFE5E7EB)
                    ),
                    singleLine = true
                )

                // Search results
                if (uiState.searchResults.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .background(Color.White)
                            .padding(horizontal = 16.dp)
                    ) {
                        items(uiState.searchResults) { result ->
                            SearchResultItem(
                                result = result,
                                onClick = {
                                    mapView?.controller?.setCenter(GeoPoint(result.lat, result.lon))
                                    mapView?.controller?.setZoom(17.0)
                                    viewModel.selectLocation(result.lat, result.lon)
                                    viewModel.clearSearch()
                                }
                            )
                        }
                    }
                }

                Divider(color = Color(0xFFE5E7EB))
            }
        },
        bottomBar = {
            // Address display and confirm button
            if (uiState.selectedAddress.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = primaryColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                uiState.selectedAddress,
                                fontSize = 14.sp,
                                color = Color(0xFF374151),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                uiState.selectedLat?.let { lat ->
                                    uiState.selectedLon?.let { lon ->
                                        onLocationSelected(lat, lon, uiState.selectedAddress)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                "Xác nhận vị trí",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // OSMDroid MapView
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        setTileSource(TileSourceFactory.MAPNIK)
                        setMultiTouchControls(true)
                        controller.setZoom(15.0)
                        controller.setCenter(GeoPoint(10.762622, 106.660172)) // Ho Chi Minh City
                        
                        mapView = this
                        
                        // Add tap listener
                        setOnClickListener {
                            val projection = this.projection
                            // Handle map click
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { view ->
                    // Update marker when location is selected
                    uiState.selectedLat?.let { lat ->
                        uiState.selectedLon?.let { lon ->
                            if (marker == null) {
                                marker = Marker(view).apply {
                                    position = GeoPoint(lat, lon)
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    title = "Vị trí đã chọn"
                                }
                                view.overlays.add(marker)
                            } else {
                                marker?.position = GeoPoint(lat, lon)
                            }
                            view.invalidate()
                        }
                    }
                }
            )

            // Center crosshair for selecting location
            if (uiState.selectedAddress.isEmpty()) {
                Icon(
                    Icons.Filled.MyLocation,
                    contentDescription = "Center",
                    tint = primaryColor,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp)
                )
            }

            // Floating action button to select current center
            if (uiState.selectedAddress.isEmpty()) {
                FloatingActionButton(
                    onClick = {
                        mapView?.let { map ->
                            val center = map.mapCenter as GeoPoint
                            viewModel.selectLocation(center.latitude, center.longitude)
                        }
                    },
                    containerColor = primaryColor,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(Icons.Filled.Check, contentDescription = "Select", tint = Color.White)
                }
            }

            // Loading indicator
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = primaryColor
                )
            }
        }
    }
}

@Composable
fun SearchResultItem(
    result: SearchResult,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Filled.LocationOn,
            contentDescription = null,
            tint = Color(0xFF6B7280),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            result.displayName,
            fontSize = 14.sp,
            color = Color(0xFF374151),
            modifier = Modifier.weight(1f)
        )
    }
    Divider(color = Color(0xFFF3F4F6))
}
