package com.example.optionsfloatingactionbutton

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShaderOverlayScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    shaderOverlayColor: Color = Color.Black.copy(alpha = 0.8f),
    enableShaderOverlay: Boolean = false,
    onShaderDismissRequest: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {

    Scaffold(
        modifier
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {
                    if (enableShaderOverlay) {
                        onShaderDismissRequest()
                    }
                }),
        topBar = {
            ShaderOverlay(enableShaderOverlay, shaderOverlayColor, topBar)
        },
        bottomBar = {
            ShaderOverlay(enableShaderOverlay, shaderOverlayColor, bottomBar)
        },
        snackbarHost,
        floatingActionButton,
        floatingActionButtonPosition,
        containerColor,
        contentColor,
        contentWindowInsets,
    ) { paddingValues ->
        ShaderOverlay(
            enableShaderOverlay = enableShaderOverlay,
            shaderOverlayColor = shaderOverlayColor
        ) {
            content(paddingValues)
        }
    }
}

@Composable
private fun ShaderOverlay(
    enableShaderOverlay: Boolean,
    shaderOverlayColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .drawWithContent {
                drawContent()
                if (enableShaderOverlay) {
                    drawRect(
                        color = shaderOverlayColor,
                        size = size,
                        topLeft = Offset.Zero
                    )
                }
            }
    ) {
        content()
    }

}