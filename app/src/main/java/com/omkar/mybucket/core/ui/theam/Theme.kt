package com.omkar.mybucket.core.ui.theam


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.omkar.mybucket.ui.theme.Pink40
import com.omkar.mybucket.ui.theme.Purple40
import com.omkar.mybucket.ui.theme.PurpleGrey40

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
    /* Override other color palette values here if needed */
)

@Composable
fun MyBucketTheme(
    darkTheme: Boolean = false, // Always defaults to false
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}