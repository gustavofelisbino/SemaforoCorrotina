package com.example.semaforocorrotina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.semaforocorrotina.ui.theme.SemaforoCorrotinaTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SemaforoCorrotinaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    SemaforoScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun SemaforoScreen(modifier: Modifier = Modifier) {
    var luzAtiva by remember { mutableStateOf("vermelho") }
    var piscando by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var job by remember { mutableStateOf<Job?>(null) }

    fun iniciarCicloNormal() {
        job?.cancel()
        piscando = false
        job = scope.launch {
            while (true) {
                luzAtiva = "vermelho"
                delay(3000)
                luzAtiva = "verde"
                delay(3000)
                luzAtiva = "amarelo"
                delay(2000)
            }
        }
    }

    fun iniciarPiscante() {
        job?.cancel()
        piscando = true
        job = scope.launch {
            while (true) {
                luzAtiva = "amarelo"
                delay(500)
                luzAtiva = "desligado"
                delay(500)
            }
        }
    }

    LaunchedEffect(Unit) {
        iniciarCicloNormal()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color.DarkGray, shape = MaterialTheme.shapes.medium)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            LuzSemaforo(
                cor = Color.Red,
                acesa = luzAtiva == "vermelho"
            )
            LuzSemaforo(
                cor = Color.Yellow,
                acesa = luzAtiva == "amarelo"
            )
            LuzSemaforo(
                cor = Color(0xFF00C853),
                acesa = luzAtiva == "verde"
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = { iniciarCicloNormal() },
                enabled = piscando
            ) {
                Text("Ciclo Normal")
            }

            Button(
                onClick = { iniciarPiscante() },
                enabled = !piscando
            ) {
                Text("Amarelo Piscante")
            }
        }
    }
}

@Composable
fun LuzSemaforo(cor: Color, acesa: Boolean) {
    val corExibida = if (acesa) cor else cor.copy(alpha = 0.15f)
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(corExibida)
    )
}
