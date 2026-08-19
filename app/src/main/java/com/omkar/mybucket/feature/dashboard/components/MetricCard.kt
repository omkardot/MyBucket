package com.omkar.mybucket.feature.dashboard.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MetricCard(
    title: String,
    count: Int,
    unit: String = "tasks",
    icon: ImageVector = Icons.Default.PlayArrow,
    badgeColor: Color = Color(0xFF004B8D),
    batchBackgroundColor  : Color= Color(0xFFF4F4F6),
    fontFamily: FontFamily? = null,
    modifier: Modifier = Modifier,
    decorativeCircleColor : Color = Color(0xFFE4E5EA)
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = batchBackgroundColor // Card background tint
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        ) {
            // Decorative background circle in top-right corner
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 25.dp, y = (-25).dp)
                    .background(
                        decorativeCircleColor,
                        shape = CircleShape
                    )
            )

            // Content Layout
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                // Header Row (Icon Badge + Title)
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular Icon Badge
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(badgeColor, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Title
                    Text(
                        text = title.uppercase(),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        color = badgeColor,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Value & Unit Row
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = count.toString(),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = fontFamily,
                        color = Color(0xFF1B1C1C),
                        lineHeight = 36.sp
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = unit,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = fontFamily,
                        color = Color(0xFF5E6066),
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }
        }
    }
}