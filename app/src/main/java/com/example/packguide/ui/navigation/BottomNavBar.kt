package com.example.packguide.ui.navigation

import com.example.packguide.ui.theme.PackGuideColors

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Iconsimport androidx.compose.material.icons.outlined.Settingsimport androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import com.example.packguide.ui.localization.Text
import com.example.packguide.ui.localization.LocalAppLanguageimport com.example.packguide.ui.localization.localizedTextimport androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.packguide.ui.components.DashedDivider
import com.example.packguide.ui.components.LedIndicator
import com.example.packguide.ui.theme.AccentRedLed
import com.example.packguide.ui.theme.TechMonospaceFontFamily
import com.example.packguide.ui.theme.Typography

@Composable
fun TechBottomNavBar(
    selectedTab: NavTab?,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier,
    tabs: List<NavTab?> = NavTab.tabs,
    currentDestination: NavTab? = selectedTab,    onSettingsClick: () -> Unit = {}) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = PackGuideColors.border, shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)),        color = PackGuideColors.surface,        tonalElevation = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            DashedDivider(color = AccentRedLed.copy(alpha = 0.6f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.filterNotNull().forEach { tab ->
                    val route = tab?.route ?: return@forEach
                    val currentRoute = currentDestination?.route ?: ""
                    val isSelected = currentRoute == route

                    val iconTint by animateColorAsState(
                        targetValue = if (isSelected) Color.White else PackGuideColors.primaryText,                        animationSpec = tween(durationMillis = 200),
                        label = "TabIconTint"
                    )

                    val textColor by animateColorAsState(
                        targetValue = if (isSelected) Color.White else PackGuideColors.primaryText,                        animationSpec = tween(durationMillis = 200),
                        label = "TabTextColor"
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                if (isSelected) AccentRedLed else Color.Transparent                            )
                            .border(
                                width = if (isSelected) 1.dp else 0.dp,
                                color = if (isSelected) AccentRedLed else Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onTabSelected(tab)
                            }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (isSelected) {
                                LedIndicator(
                                    color = AccentRedLed,
                                    size = 5.dp,
                                    isPulsing = true
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                            }

                            Icon(
                                imageVector = tab.icon,
                                contentDescription = localizedText(tab.title, LocalAppLanguage.current),                                tint = iconTint,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = if (isSelected) tab.title else tab.title.replace("[", "").replace("]", ""),
                                style = Typography.labelSmall.copy(fontSize = 10.sp),
                                fontFamily = TechMonospaceFontFamily,
                                color = textColor
                            )
                        }
                    }
                }
                Box(                    modifier = Modifier.weight(0.8f).clip(RoundedCornerShape(4.dp))                        .clickable(                            interactionSource = remember { MutableInteractionSource() },                            indication = null,                            onClick = onSettingsClick                        )                        .padding(vertical = 8.dp),                    contentAlignment = Alignment.Center                ) {                    Column(horizontalAlignment = Alignment.CenterHorizontally) {                        Icon(                            imageVector = Icons.Outlined.Settings,                            contentDescription = localizedText("Configuración", LocalAppLanguage.current),                            tint = PackGuideColors.primaryText,                            modifier = Modifier.size(20.dp)                        )                        Spacer(Modifier.height(4.dp))                        Text(                            text = "[AJUSTES]",                            style = Typography.labelSmall.copy(fontSize = 9.sp),                            color = PackGuideColors.primaryText                        )                    }                }            }
        }
    }
}
