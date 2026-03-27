package com.ghost.todo.ui.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun GlassmorphismCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color.White.copy(alpha = 0.1f),
    borderColor: Color = Color.White.copy(alpha = 0.2f),
    borderWidth: Dp = 1.5.dp,
    blurRadius: Dp = 8.dp,
    shadowElevation: Dp = 8.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.1f),
    padding: Dp = 16.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        backgroundColor,
                        backgroundColor.copy(alpha = backgroundColor.alpha * 0.8f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 100f)
                )
            )
            .border(
                width = borderWidth,
                color = borderColor,
                shape = shape
            )
            .shadow(
                elevation = shadowElevation,
                shape = shape,
                ambientColor = shadowColor,
                spotColor = shadowColor
            )
    ) {
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}




@Composable
fun AdvancedGlassmorphismCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp),
    backgroundColor: Color = Color.White.copy(alpha = 0.15f),
    borderColor: Color = Color.White.copy(alpha = 0.25f),
    borderWidth: Dp = 1.dp,
    blurRadius: Dp = 12.dp,
    shadowElevation: Dp = 12.dp,
    shadowColor: Color = Color.Black.copy(alpha = 0.15f),
    innerPadding: Dp = 20.dp,
    gradientColors: List<Color> = listOf(
        Color.White.copy(alpha = 0.2f),
        Color.White.copy(alpha = 0.05f)
    ),
    gradientAngle: Float = 40f,
    cardAlpha: Float = 1f, // NEW: Control whole card transparency
    enableInnerGlow: Boolean = true,
    innerGlowColor: Color = Color.White.copy(alpha = 0.1f),
    innerGlowAlpha: Float = 1f,
    innerGlowWidth: Dp = 40.dp,
    enableHoverEffect: Boolean = false,
    hoverElevation: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val currentElevation = if (enableHoverEffect && isHovered) hoverElevation else shadowElevation
    val currentBorderColor = if (enableHoverEffect && isHovered) {
        borderColor.copy(alpha = borderColor.alpha * 1.5f)
    } else borderColor

    val gradientStartOffset = remember(gradientAngle) {
        val radians = Math.toRadians(gradientAngle.toDouble()).toFloat()
        Offset(
            x = (cos(radians) + 1) / 2,
            y = (sin(radians) + 1) / 2
        )
    }

    val gradientEndOffset = remember(gradientAngle) {
        val radians = Math.toRadians((gradientAngle + 180).toDouble()).toFloat()
        Offset(
            x = (cos(radians) + 1) / 2,
            y = (sin(radians) + 1) / 2
        )
    }

    Box(
        modifier = modifier
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = rememberRippleEffect(
                            bounded = true,
                            radius = 40.dp,
                            color = borderColor
                        )
                    ) { onClick() }
                } else Modifier
            )
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors.map { it.copy(alpha = it.alpha * cardAlpha) },
                    start = gradientStartOffset,
                    end = gradientEndOffset
                )
            )
            .border(
                width = borderWidth,
                color = currentBorderColor.copy(alpha = currentBorderColor.alpha * cardAlpha),
                shape = shape
            )
            .shadow(
                elevation = currentElevation,
                shape = shape,
                spotColor = shadowColor.copy(alpha = shadowColor.alpha * cardAlpha),
                ambientColor = shadowColor.copy(alpha = shadowColor.alpha * cardAlpha),
                clip = false
            )
            .drawBehind {
                if (enableInnerGlow) {
                    val cornerRadiusPx = shape.toPx(cornerRadius = 28.dp, size = size, density = density)
                    val glowWidthPx = with(density) { innerGlowWidth.toPx() }

                    // Draw smooth gradient blend with adjustable alpha
                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                innerGlowColor.copy(alpha = innerGlowColor.alpha * innerGlowAlpha * cardAlpha),
                                innerGlowColor.copy(alpha = 0f)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(glowWidthPx, glowWidthPx)
                        ),
                        cornerRadius = CornerRadius(cornerRadiusPx),
                        style = Stroke(width = glowWidthPx)
                    )
                }
            }
    ) {
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .then(
                    if (blurRadius > 0.dp) {
                        Modifier.drawWithContent {
                            drawContent()
                            drawRoundRect(
                                color = Color.White.copy(alpha = 0.05f * cardAlpha),
                                cornerRadius = CornerRadius(shape.toPx(cornerRadius = 28.dp, size = size, density = density)),
                                blendMode = BlendMode.Screen
                            )
                        }
                    } else Modifier
                )
        ) {
            content()
        }
    }
}




// Extension function to convert Shape to corner radius in pixels
private fun Shape.toPx(cornerRadius: Dp, size: Size, density: Density): Float {
    return when (this) {
        is RoundedCornerShape -> {
            val topStart = topStart.toPx(size, density)
            val topEnd = topEnd.toPx(size, density)
            val bottomEnd = bottomEnd.toPx(size, density)
            val bottomStart = bottomStart.toPx(size, density)
            listOf(topStart, topEnd, bottomEnd, bottomStart).maxOrNull()
                ?: with(density) { cornerRadius.toPx() }
        }
        else -> with(density) { cornerRadius.toPx() }
    }
}



// Enhanced ripple effect with better customization
@Composable
private fun rememberRippleEffect(
    bounded: Boolean = true,
    radius: Dp = 40.dp,
    color: Color = Color.White.copy(alpha = 0.3f)
): Indication {
    return ripple(
        bounded = bounded,
        radius = radius,
        color = color
    )
}
