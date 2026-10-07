package com.example.packguide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.PrimaryBlack
import com.example.packguide.ui.theme.SecondarySlate
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun TechBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = PrimaryBlack,
    contentColor: Color = Color.White,
    borderColor: Color = SecondarySlate.copy(alpha = 0.4f),
    ledColor: Color? = null
) {
    Box(
        modifier = modifier
            .background(containerColor, shape = RoundedCornerShape(2.dp))
            .border(1.dp, borderColor, shape = RoundedCornerShape(2.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (ledColor != null) {
                LedIndicator(color = ledColor, size = 6.dp, isPulsing = false)
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                style = Typography.labelSmall.copy(fontSize = 11.sp),
                fontFamily = TechMonospaceFontFamily,
                color = contentColor
            )
        }
    }
}
