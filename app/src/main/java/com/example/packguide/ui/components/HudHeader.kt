package com.example.packguide.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.packguide.ui.theme.AccentGreenLed
import com.example.packguide.ui.theme.BackgroundLight
import com.example.packguide.ui.theme.PrimaryBlack
import com.example.packguide.ui.theme.SecondarySlate
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun HudHeader(
    title: String,
    modifier: Modifier = Modifier,
    sysCode: String = "SYS // PACKING_OS",
    statusText: String = "[ ONLINE ]",
    ledColor: Color = AccentGreenLed
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sysCode,
                style = Typography.labelSmall,
                fontFamily = TechMonospaceFontFamily,
                color = SecondarySlate
            )
            Spacer(modifier = Modifier.weight(1f))
            LedIndicator(
                color = ledColor,
                size = 7.dp,
                label = statusText
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = Typography.headlineMedium,
            fontFamily = TechMonospaceFontFamily,
            fontWeight = FontWeight.Bold,
            color = PrimaryBlack
        )

        Spacer(modifier = Modifier.height(8.dp))

        DashedDivider()
    }
}
