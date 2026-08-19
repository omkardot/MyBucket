package com.omkar.mybucket.feature.dashboard.components

import android.graphics.drawable.Icon
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omkar.mybucket.ui.theme.Hankengrotesk

@Composable
fun TaskDetailsCard(
    modifier: Modifier,
    stack:String,
    icontint: Color,
    textToShow:String,
    backgroundColor: Color = Color(0xFF004B8D),
    icon: ImageVector = Icons.Default.PlayArrow,
    title:String,textColor: Color,
    stacktextColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor // Card background tint
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                // Header Row (Icon Badge + Title)

                Text(
                    text = textToShow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Hankengrotesk,
                    color = stacktextColor
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Circular Icon Badge
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(Color.Transparent, shape = CircleShape),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = icontint,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    // Title
                    Text(
                        text = title.uppercase(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Hankengrotesk,
                        color = textColor
                    )
                }


            }
        }
    }
}

