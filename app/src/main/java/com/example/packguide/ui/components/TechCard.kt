package com.example.packguide.ui.components

import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.example.packguide.ui.localization.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun TechCard(
    modifier: Modifier = Modifier,
    headerTag: String? = null,
    subTag: String? = null,
    borderColor: Color = Color.Unspecified,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(4.dp)
    val resolvedBorderColor = if (borderColor == Color.Unspecified) {
        PackGuideColors.primaryText.copy(alpha = 0.15f)
    } else borderColor

    val cardModifier = modifier
        .fillMaxWidth()
        .shadow(elevation = 2.dp, shape = shape, spotColor = PackGuideColors.primaryText.copy(alpha = 0.1f))
        .background(PackGuideColors.surface, shape = shape)
        .border(1.dp, resolvedBorderColor, shape = shape)
        .let {
            if (onClick != null) it.clickable { onClick() } else it
        }

    Box(modifier = cardModifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            if (headerTag != null || subTag != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (headerTag != null) {
                        Text(
                            text = headerTag,
                            style = Typography.labelSmall,
                            fontFamily = TechMonospaceFontFamily,
                            color = PackGuideColors.primaryText
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (subTag != null) {
                        Text(
                            text = subTag,
                            style = Typography.labelSmall,
                            fontFamily = TechMonospaceFontFamily,
                            color = PackGuideColors.secondaryText
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                DashedDivider(color = PackGuideColors.border)
                Spacer(modifier = Modifier.height(10.dp))
            }

            content()
        }
    }
}
