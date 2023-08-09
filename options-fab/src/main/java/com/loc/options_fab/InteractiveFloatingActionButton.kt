package com.loc.options_fab

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class FloatingActionButtonOption(
    @DrawableRes val icon: Int,
    val text: String,
    val tintColor: Color,
    val containerColor: Color,
    val textColor: Color,
)

data class FloatingActionButtonSizes(
    val mainFloatingActionButton: Dp = 57.dp,
    val optionFloatingActionButton: Dp = 46.dp,
)

val blue = Color(0xFF1982DD)


@Composable
fun InteractiveFloatingActionButton(
    modifier: Modifier = Modifier,
    @DrawableRes mainIcon: Int,
    @DrawableRes mainIconAlternative: Int,
    mainIconColor: Color = Color.White,
    mainContainerColor: Color = blue,
    mainText: String,
    textStyle: TextStyle = TextStyle(color = Color.Black),
    options: List<FloatingActionButtonOption>,
    sizes: FloatingActionButtonSizes = FloatingActionButtonSizes(),
    showOptions: Boolean = false,
    onMainIconClick: () -> Unit,
    onOptionClick: (FloatingActionButtonOption) -> Unit
) {

    var mainIconRotationState by remember {
        mutableStateOf(false)
    }
    val mainIconRotationValue = animateFloatAsState(
        targetValue = if (mainIconRotationState) -90f else 0f,
        label = "Main Icon Animation",
        animationSpec = tween(durationMillis = 300)
    ).value

    var scaleState by remember {
        mutableStateOf(false)
    }
    val scaleValue = animateFloatAsState(
        targetValue = if (scaleState) 1f else 1.1f,
        label = "Click Size Animation",
        animationSpec = tween(durationMillis = 100),
        finishedListener = {
            scaleState = false
        }
    ).value

    LaunchedEffect(key1 = showOptions) {
        if (showOptions) { // Trigger the animation
            mainIconRotationState = !mainIconRotationState
            scaleState = !scaleState
        } else { // Restore Animation
            mainIconRotationState = !mainIconRotationState
            scaleState = !scaleState
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // Texts Column
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = End
        ) {
            if (showOptions) {
                options.forEach { option ->
                    Box(
                        modifier = Modifier
                            .height(sizes.optionFloatingActionButton),
                        contentAlignment = Center
                    ) {
                        Text(text = option.text, style = textStyle)
                    }
                }
                // Main Button Text
                Box(
                    modifier = Modifier.height(sizes.mainFloatingActionButton),
                    contentAlignment = Center
                ) {
                    Text(text = mainText, style = textStyle)
                }
            }
        }
        if (showOptions) {
            Spacer(modifier = Modifier.width(20.dp))
        }
        // Icons Column
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
                //Option Floating Action Button
                options.forEach { option ->
                    AnimatedFloatingActionButton(
                        isVisible = showOptions,
                        size = sizes.optionFloatingActionButton,
                        containerColor = option.containerColor,
                        tintColor = option.tintColor,
                        icon = option.icon,
                        onClick = { onOptionClick(option) }
                    )
                }
            //Main Floating Action Button
            FloatingActionButton(
                modifier = Modifier
                    .size(sizes.mainFloatingActionButton)
                    .rotate(mainIconRotationValue)
                    .scale(scaleValue),
                containerColor = mainContainerColor,
                contentColor = mainIconColor,
                shape = CircleShape,
                onClick = {
                    onMainIconClick()
                },
            ) {
                Icon(
                    painter = painterResource(id = if (showOptions) mainIconAlternative else mainIcon),
                    modifier = Modifier.scale(scaleValue),
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedFloatingActionButton(
    isVisible: Boolean,
    size: Dp,
    containerColor: Color,
    tintColor: Color,
    @DrawableRes icon: Int,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn(tween(200)),
        exit = scaleOut(tween(200))
    ) {
        FloatingActionButton(
            modifier = Modifier.size(size),
            shape = CircleShape,
            containerColor = containerColor,
            onClick = onClick
        ) {
            AnimatedVisibility(
                visible = isVisible,
                enter = scaleIn(tween(300)),
                exit = scaleOut(tween(300)),
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = tintColor
                )
            }
        }
    }

}