package ru.itis.android.homework9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.android.homework9.ui.CircleChart
import ru.itis.android.homework9.ui.theme.Homework9Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Homework9Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ChartScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

private val Palette = listOf(
    Color(0xFFE57373),
    Color(0xFF64B5F6),
    Color(0xFFFFD54F),
    Color(0xFF81C784),
    Color(0xFFBA68C8),
    Color(0xFFFFB74D),
    Color(0xFF4DD0E1),
    Color(0xFFF06292),
    Color(0xFFAED581),
    Color(0xFF9575CD),
)

private const val MIN_SECTORS = 2
private val MAX_SECTORS = Palette.size

@Composable
private fun ChartScreen(modifier: Modifier = Modifier) {
    var sectorsCount by remember { mutableIntStateOf(6) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
    ) {
        CircleChart(sectorsCount = sectorsCount, colors = Palette)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = { if (sectorsCount > MIN_SECTORS) sectorsCount-- },
                enabled = sectorsCount > MIN_SECTORS,
            ) { Text("−", fontSize = 20.sp) }

            Text(
                text = "Секторов: $sectorsCount",
                modifier = Modifier.width(140.dp),
                textAlign = TextAlign.Center,
            )

            Button(
                onClick = { if (sectorsCount < MAX_SECTORS) sectorsCount++ },
                enabled = sectorsCount < MAX_SECTORS,
            ) { Text("+", fontSize = 20.sp) }
        }
    }
}
