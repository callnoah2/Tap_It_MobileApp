package com.example.assignment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.assignment2.ui.theme.Assignment2Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

class TapItGameViewModel {
    private val colors = listOf(Color.Blue, Color.Red, Color.Green, Color.Yellow)

    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level.asStateFlow()

    private val _sequence = MutableStateFlow<List<Color>>(emptyList())
    val sequence: StateFlow<List<Color>> = _sequence.asStateFlow()

    private val _currentColor = MutableStateFlow(colors.random())
    val currentColor: StateFlow<Color> = _currentColor.asStateFlow()

    private val _gameOver = MutableStateFlow(false)
    val gameOver: StateFlow<Boolean> = _gameOver.asStateFlow()

    private var userInputIndex = 0

    init {
        startNewLevel()
    }

    fun handleColorTap(color: Color) {
        if (_gameOver.value) return

        val currentSequence = _sequence.value
        if (userInputIndex < currentSequence.size && color == currentSequence[userInputIndex]) {
            userInputIndex++
            if (userInputIndex == currentSequence.size) {
                // Player entered the sequence
                _level.value += 1
                startNewLevel()
            }
        } else {
            // Wrong = Game over
            _gameOver.value = true
        }
    }

    fun restartGame() {
        _level.value = 1
        _gameOver.value = false
        _sequence.value = emptyList()
        userInputIndex = 0
        startNewLevel()
    }

    private fun startNewLevel() {
        val newColor = colors.random()
        _sequence.value += newColor
        _currentColor.value = newColor
        userInputIndex = 0
    }
}

@Composable
fun TapItGameScreen() {
    val viewModel = remember { TapItGameViewModel() }

    val level by remember { viewModel.level }.collectAsState()
    val currentColor by remember { viewModel.currentColor }.collectAsState()
    val gameOver by remember { viewModel.gameOver }.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (gameOver) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Game Over! You reached Level $level",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.restartGame() }) {
                    Text(text = "Restart Game")
                }
            }
        } else {
            // Level Indicator
            Text(
                text = "Level $level",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Next Color to Tap
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(8.dp)
                    .background(currentColor, RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tap Area
            Column(
                modifier = Modifier
                    .size(400.dp)
                    .padding(8.dp)
            ) {
                Row(Modifier.fillMaxWidth()) {
                    ColorTile(Color.Blue, Modifier.weight(1f)) { viewModel.handleColorTap(Color.Blue) }
                    ColorTile(Color.Red, Modifier.weight(1f)) { viewModel.handleColorTap(Color.Red) }
                }
                Row(Modifier.fillMaxWidth()) {
                    ColorTile(Color.Green, Modifier.weight(1f)) { viewModel.handleColorTap(Color.Green) }
                    ColorTile(Color.Yellow, Modifier.weight(1f)) { viewModel.handleColorTap(Color.Yellow) }
                }
            }
        }
    }
}

@Composable
fun ColorTile(color: Color, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .aspectRatio(1f) // Ensures square shape
            .clickable { onClick() }
            .padding(4.dp)
            .background(color, RoundedCornerShape(8.dp))
    )
}