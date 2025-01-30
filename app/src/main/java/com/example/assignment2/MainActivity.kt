package com.example.assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.assignment2.ui.theme.Assignment2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assignment2Theme {
                TapItGameScreen()
            }
        }
    }
}

@Composable
fun TapItGameScreen() {
    var level by remember { mutableStateOf(1) }
    val colors = listOf(Color.Blue, Color.Red, Color.Green, Color.Yellow)
    var currentColor by remember { mutableStateOf(colors.random()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Level Indicator
        Text(
            text = "Level $level",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Next in Sequence Color Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(8.dp)
                .background(currentColor, RoundedCornerShape(16.dp))
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tap Area (Four Colored Squares)
        Column(
            modifier = Modifier
                .size(400.dp)
                .padding(8.dp)

        ) {
            Row(Modifier.fillMaxWidth()) {
                ColorTile(color = Color.Blue, Modifier.weight(1f)) { /* Handle tap */ }
                ColorTile(color = Color.Red, Modifier.weight(1f)) { /* Handle tap */ }
            }
            Row(Modifier.fillMaxWidth()) {
                ColorTile(color = Color.Green, Modifier.weight(1f)) { /* Handle tap */ }
                ColorTile(color = Color.Yellow, Modifier.weight(1f)) { /* Handle tap */ }
            }
        }
    }
}

@Composable
fun ColorTile(color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .aspectRatio(1f) // Ensure square shape
            .clickable { onClick() }
            .padding(4.dp)
            .background(color, RoundedCornerShape(8.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun TapItGameScreenPreview() {
    Assignment2Theme {
        TapItGameScreen()
    }
}