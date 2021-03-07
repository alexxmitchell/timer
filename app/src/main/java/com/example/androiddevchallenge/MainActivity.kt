/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun MyApp() {
    val viewModel = MainViewModel()
    val time by viewModel.time.observeAsState()

    var timerStarted by remember { mutableStateOf(false) }
    Surface(color = MaterialTheme.colors.background) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround

        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                AnimatedVisibility(!timerStarted) {
                    Button(
                        onClick = {
                            timerStarted = true
                            viewModel.timer.start()
                        },
                        modifier = Modifier.width(180.dp).padding(14.dp)
                    ) {
                        Text(text = "Start")
                    }
                }
                AnimatedVisibility(timerStarted && time != 0) {
                    Button(
                        onClick = {
                            timerStarted = false
                            viewModel.onPause()
                        },
                        modifier = Modifier.width(180.dp).padding(14.dp)
                    ) {
                        Text(text = "Stop")
                    }
                }
                Spacer(modifier = Modifier.padding(end = 20.dp))

                Button(
                    onClick = {
                        viewModel.resetTimer()
                        timerStarted = false
                    },
                    enabled = timerStarted,
                    modifier = Modifier.width(180.dp).padding(14.dp)
                ) {
                    Text(text = "Reset")
                }
            }
            if (time != 0) {
                Clock(elapsedTime = time)
                Text(text = "Time remaining: $time seconds")
            } else {
                timerStarted = false
                Text(
                    text = "Time's up!",
                    fontSize = 30.sp
                )
            }
        }
    }
}
@Preview
@Composable
fun previewClock() {
    Clock(elapsedTime = 15)
}

@Composable
fun Clock(elapsedTime: Int?) {

    val elapsed: Float by animateFloatAsState(
        targetValue = if (elapsedTime != null) {
            (360 * (elapsedTime.div(10)).toFloat())
        } else {
            0f
        },
        animationSpec = tween(
            durationMillis = 10000,
            easing = LinearEasing
        )
    )
    Canvas(
        modifier = Modifier.size(300.dp),
        onDraw = {
            if (elapsedTime != null) {
                drawArc(
                    color = Color.DarkGray,
                    startAngle = -90f,
                    sweepAngle = elapsed,
                    useCenter = true,
                    size = Size(600f, 600f),
                    topLeft = Offset(225f, 50f)
                )
            }
        }
    )
}

@ExperimentalAnimationApi
@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}
//
// @Preview("Dark Theme", widthDp = 360, heightDp = 640)
// @Composable
// fun DarkPreview() {
//    MyTheme(darkTheme = true) {
//        MyApp()
//    }
// }
