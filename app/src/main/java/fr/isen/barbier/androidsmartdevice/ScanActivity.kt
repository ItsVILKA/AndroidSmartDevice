package fr.isen.barbier.androidsmartdevice

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.Manifest
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.barbier.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class ScanActivity : ComponentActivity() {
    private lateinit var bluetoothAdapter: BluetoothAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            Toast.makeText(this, "Bluetooth permissions granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Bluetooth permissions required", Toast.LENGTH_SHORT).show()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Toast.makeText(this, "Bluetooth error", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setContent {
            AndroidSmartDeviceTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Sheul's Devices",
                                    color = Color.Black, // Black title
                                    fontWeight = FontWeight.Bold, // Bold text
                                    fontSize = 20.sp, // Font size
                                    modifier = Modifier.fillMaxWidth(), // Center title
                                    textAlign = TextAlign.Center // Center title
                                )
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = Color.Transparent, // Transparent background
                                titleContentColor = Color.Black // Title in black
                            ),
                            modifier = Modifier.fillMaxWidth() // Ensure TopAppBar takes full width
                        )
                    }
                ) { innerPadding ->
                    MainContentComponent(
                        modifier = Modifier.padding(innerPadding),
                        onStartScan = { startBluetoothScan() }
                    )
                }
            }
        }
    }

    private fun startBluetoothScan() {
        Toast.makeText(this, "Starting BLE scan...", Toast.LENGTH_SHORT).show()
    }
}

@Composable
fun MainContentComponent(
    modifier: Modifier = Modifier,
    onStartScan: () -> Unit
) {
    var isScanning by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScButton(
            onClick = { isScanning = !isScanning }, // Toggle scan state
            isScanning = isScanning,
            onStartScan = { onStartScan() }
        )
    }
}

@Composable
fun ScButton(onClick: () -> Unit, isScanning: Boolean, onStartScan: () -> Unit) {
    var isProgressVisible by remember { mutableStateOf(false) }

    // Check if the button was clicked and perform actions
    Column(
        modifier = Modifier
            .clickable {
                onClick()
                if (!isScanning) {
                    onStartScan()  // Start scan when button is pressed
                    isProgressVisible = true
                }
            }
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                text = if (isScanning) "Scanning..." else "Start a Scan", // Modify text based on state
                color = Color.Gray,
                fontSize = 22.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Image(
                painter = painterResource(id = if (isScanning) R.drawable.cross else R.drawable.play),
                contentDescription = if (isScanning) "Pause" else "Play",
                modifier = Modifier.size(60.dp)
            )
        }

        // Show progress bar only during scan
        if (isScanning) {
            LinearProgressIndicator(
                color = Color.Black, // Black color
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List of devices
        ListDevice(devices = listOf("Device 1", "Device 2", "Device 3"))
    }
}

@Composable
fun ListDevice(devices: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        devices.forEachIndexed { index, device ->
            Device(deviceName = device, deviceNumber = "Number ${index + 1}")
            if (index < devices.size - 1) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun Device(deviceName: String, deviceNumber: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .background(Color.Black, shape = CircleShape) // Black background
        ) {
            Text(
                text = deviceNumber.split(" ").last(),
                color = Color.White,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = deviceName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = deviceNumber,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}
