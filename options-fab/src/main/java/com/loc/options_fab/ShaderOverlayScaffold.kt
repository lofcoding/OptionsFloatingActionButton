package com.loc.options_fab

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
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
            ShaderOverlay(
                enableShaderOverlay = enableShaderOverlay,
                shaderOverlayColor = shaderOverlayColor,
                onClick = {
                    onShaderDismissRequest()
                },
                content = topBar
            )
        },
        bottomBar = {
//                    bottomBar()
            ShaderOverlay(
                enableShaderOverlay = enableShaderOverlay,
                shaderOverlayColor = shaderOverlayColor,
                onClick = {
                    onShaderDismissRequest()
                },
                content = bottomBar
            )
        },
        snackbarHost,
        floatingActionButton,
        floatingActionButtonPosition,
        containerColor,
        contentColor,
        contentWindowInsets,
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
private fun ShaderOverlay(
    enableShaderOverlay: Boolean,
    shaderOverlayColor: Color,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    DimensionSubcomposeLayout(
        mainContent = {
            content()
        },
        dependentContent = { size ->
            val maxHeight = LocalConfiguration.current.screenHeightDp.dp
            if (enableShaderOverlay) {
                Box(
                    modifier = Modifier
                        .height(maxHeight)
                        .fillMaxWidth()
                        .background(shaderOverlayColor)
                        .clickable(
                            enabled = true,
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            onClick()
                        }
                )
            }
        }
    )
}

/**
 * SubcomposeLayout that [SubcomposeMeasureScope.subcompose]s [mainContent]
 * and gets total size of [mainContent] and passes this size to [dependentContent].
 * This layout passes exact size of content unlike
 * BoxWithConstraints which returns [Constraints] that doesn't match Composable dimensions under
 * some circumstances
 *
 * @param placeMainContent when set to true places main content. Set this flag to false
 * when dimensions of content is required for inside [mainContent]. Just measure it then pass
 * its dimensions to any child composable
 *
 * @param mainContent Composable is used for calculating size and pass it
 * to Composables that depend on it
 *
 * @param dependentContent Composable requires dimensions of [mainContent] to set its size.
 * One example for this is overlay over Composable that should match [mainContent] size.
 *
 */
@Composable
private fun DimensionSubcomposeLayout(
    modifier: Modifier = Modifier,
    placeMainContent: Boolean = true,
    mainContent: @Composable () -> Unit,
    dependentContent: @Composable (Size) -> Unit
) {
    SubcomposeLayout(
        modifier = modifier
    ) { constraints: Constraints ->

        // Subcompose(compose only a section) main content and get Placeable
        val mainPlaceables: List<Placeable> = subcompose(SlotsEnum.Main, mainContent)
            .map {
                it.measure(constraints.copy(minWidth = 0, minHeight = 0))
            }

        // Get max width and height of main component
        var maxWidth = 0
        var maxHeight = 0

        mainPlaceables.forEach { placeable: Placeable ->
            maxWidth += placeable.width
            maxHeight = placeable.height
        }

        val dependentPlaceables: List<Placeable> = subcompose(SlotsEnum.Dependent) {
            dependentContent(Size(maxWidth.toFloat(), maxHeight.toFloat()))
        }
            .map { measurable: Measurable ->
                measurable.measure(constraints)
            }


        layout(maxWidth, maxHeight) {

            if (placeMainContent) {
                mainPlaceables.forEach { placeable: Placeable ->
                    placeable.placeRelative(0, 0)
                }
            }

            dependentPlaceables.forEach { placeable: Placeable ->
                placeable.placeRelative(0, 0)
            }
        }
    }
}

enum class SlotsEnum { Main, Dependent }
