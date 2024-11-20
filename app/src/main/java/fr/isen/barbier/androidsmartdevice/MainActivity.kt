package fr.isen.barbier.androidsmartdevice

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.barbier.androidsmartdevice.ui.theme.AndroidSmartDeviceTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidSmartDeviceTheme {
                val context = LocalContext.current // Récupère le contexte
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Sheul's Devices", color = Color.Black)
                                }
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = Color.Transparent // Pas de fond
                            )
                        )
                    }
                ) { innerPadding ->
                    MainContentComponent(innerPadding, context) // Passe le contexte en paramètre
                }
            }
        }
    }
}

@Composable
fun MainContentComponent(innerPadding: PaddingValues, context: android.content.Context) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        contentAlignment = Alignment.Center // Centre le contenu
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Titre centré au milieu
            Text(
                text = "Welcome,",
                fontSize = 28.sp, // Taille ajustée
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Image Bluetooth
            Image(
                painter = painterResource(id = R.drawable.bluetooth),
                contentDescription = "Bluetooth Image",
                modifier = Modifier
                    .size(200.dp) // Agrandi l'image (2x)
                    .padding(bottom = 24.dp)
            )
            // Bouton rectangulaire sans angles arrondis
            Button(
                onClick = {
                    context.startActivity(Intent(context, ScanActivity::class.java))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                modifier = Modifier
                    .width(200.dp) // Largeur fixe
                    .height(50.dp), // Hauteur fixe
            ) {
                Text(text = "New Device", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidSmartDeviceTheme {
        val context = LocalContext.current // Contexte nécessaire pour l'aperçu
        MainContentComponent(innerPadding = PaddingValues(0.dp), context = context)
    }
}
