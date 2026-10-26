package com.example.packguide.ui.components

import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import com.example.packguide.ui.localization.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun AsciiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = true,
    isAccentRed: Boolean = false,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val containerColor = when {
        !enabled -> PackGuideColors.secondaryText.copy(alpha = 0.2f)
        isAccentRed -> AccentRedLed
        isPrimary -> PackGuideColors.primaryText
        else -> PackGuideColors.surface
    }

    val contentColor = when {
        !enabled -> PackGuideColors.secondaryText
        isAccentRed -> PackGuideColors.surface
        isPrimary -> PackGuideColors.surface
        else -> PackGuideColors.primaryText
    }

    val borderColor = when {
        !enabled -> PackGuideColors.secondaryText.copy(alpha = 0.3f)
        isAccentRed -> AccentRedLed
        isPrimary -> PackGuideColors.primaryText
        else -> PackGuideColors.primaryText
    }

    val shape = RoundedCornerShape(2.dp)

    Box(
        modifier = modifier
            .background(containerColor, shape = shape)
            .border(1.dp, borderColor, shape = shape)
            .clickable(enabled = enabled) { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = Typography.titleMedium,
                fontFamily = TechMonospaceFontFamily,
                color = contentColor
            )
        }
    }
}
