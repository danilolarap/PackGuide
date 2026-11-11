package com.example.packguide

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.packguide.ui.navigation.NavTab
import com.example.packguide.ui.screens.PackingState

/** Survives rotation and multi-window, but never persists authentication to disk. */
class PackGuideSessionViewModel : ViewModel() {
    var isAuthenticated by mutableStateOf(false)
    var packing by mutableStateOf(PackingState())
        private set
    var currentTab by mutableStateOf<NavTab>(NavTab.Measurements)
        private set
    private val history = mutableStateListOf<NavTab>()
    val canGoBack: Boolean get() = packing.allDimensionsConfirmed && history.isNotEmpty()

    fun navigateTo(tab: NavTab) {
        if (tab != NavTab.Measurements && !packing.allDimensionsConfirmed) return
        if (tab == currentTab) return
        // Four tabs need only a bounded history; revisiting a tab unwinds to it.
        val index = history.indexOf(tab)
        if (index >= 0) {
            while (history.size > index) history.removeAt(history.lastIndex)
        } else {
            history.add(currentTab)
        }
        currentTab = tab
    }

    fun goBack() {
        if (!packing.allDimensionsConfirmed) return
        if (history.isNotEmpty()) currentTab = history.removeAt(history.lastIndex)
    }

    fun logout() {
        isAuthenticated = false
        packing = PackingState()
        currentTab = NavTab.Measurements
        history.clear()
    }
}
