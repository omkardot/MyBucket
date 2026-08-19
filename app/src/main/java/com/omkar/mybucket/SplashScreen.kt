package com.omkar.mybucket

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    fun String.toComposeColor(): Color =
        Color(this.toColorInt())
    LaunchedEffect(key1 = true) {
        // Animate scale and alpha simultaneously
        scale.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 800)
        )
        alpha.animateTo(
            targetValue = 1.0f,
            animationSpec = tween(durationMillis = 800)
        )

        // Hold splash screen for 1.5 seconds
        delay(1500)
        onSplashFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC)), // Light off-white background matching image
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Bucket Icon
        Image(
            painter = painterResource(id = R.drawable.ic_my_bucket),
            contentDescription = "My Bucket Logo",
            modifier = Modifier
                .size(160.dp)
                .scale(scale.value)
                .alpha(alpha.value)
                .shadow(
                    elevation = 30.dp,
                    shape = RoundedCornerShape(24.dp),
                    clip = false
                )

        )

        Spacer(modifier = Modifier.height(24.dp))

        // App Title
        Text(
            text = "My Bucket",
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF000000),
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        )
        Text(
            text = "Organize your life, one drop at a time.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = "#424752".toComposeColor(),
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}