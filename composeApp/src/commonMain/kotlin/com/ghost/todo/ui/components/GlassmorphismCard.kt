package com.ghost.todo.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
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

    // 🔥 Now theme-driven by default
    backgroundColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    shadowColor: Color = Color.Unspecified,

    borderWidth: Dp = 1.dp,
    blurRadius: Dp = 12.dp,
    shadowElevation: Dp = 12.dp,

    innerPadding: Dp = 20.dp,

    gradientColors: List<Color> = emptyList(),
    gradientAngle: Float = 40f,

    cardAlpha: Float = 1f,

    enableInnerGlow: Boolean = true,
    innerGlowColor: Color = Color.Unspecified,
    innerGlowAlpha: Float = 1f,
    innerGlowWidth: Dp = 40.dp,

    enableHoverEffect: Boolean = false,
    hoverElevation: Dp = 16.dp,

    onClick: (() -> Unit)? = null,

    content: @Composable () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val colorScheme = MaterialTheme.colorScheme

    // ✅ Resolve colors safely
    val resolvedBackground = if (backgroundColor == Color.Unspecified) {
        if (isDark) colorScheme.surface.copy(alpha = 0.3f)
        else colorScheme.surface.copy(alpha = 0.6f)
    } else backgroundColor

    val resolvedBorder = if (borderColor == Color.Unspecified) {
        if (isDark) Color.White.copy(alpha = 0.2f)
        else Color.Black.copy(alpha = 0.1f)
    } else borderColor

    val resolvedShadow = if (shadowColor == Color.Unspecified) {
        Color.Black.copy(alpha = if (isDark) 0.4f else 0.15f)
    } else shadowColor

    val resolvedGlow = if (innerGlowColor == Color.Unspecified) {
        if (isDark) Color.White.copy(alpha = 0.08f)
        else Color.White.copy(alpha = 0.15f)
    } else innerGlowColor

    // ✅ Gradient
    val colors = if (gradientColors.isNotEmpty()) {
        gradientColors
    } else {
        if (isDark) {
            listOf(
                resolvedBackground.copy(alpha = 0.4f),
                resolvedBackground.copy(alpha = 0.1f)
            )
        } else {
            listOf(
                resolvedBackground.copy(alpha = 0.3f),
                resolvedBackground.copy(alpha = 0.05f)
            )
        }
    }

    val density = LocalDensity.current
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    val currentElevation =
        if (enableHoverEffect && isHovered) hoverElevation else shadowElevation

    val currentBorderColor =
        if (enableHoverEffect && isHovered) {
            resolvedBorder.copy(alpha = resolvedBorder.alpha * 1.5f)
        } else resolvedBorder

    val radians = Math.toRadians(gradientAngle.toDouble()).toFloat()

    val gradientStartOffset = remember(gradientAngle) {
        Offset(cos(radians), sin(radians))
    }

    val gradientEndOffset = remember(gradientAngle) {
        Offset(-cos(radians), -sin(radians))
    }

    Box(
        modifier = modifier
            .shadow(
                elevation = currentElevation,
                shape = shape,
                spotColor = resolvedShadow.copy(alpha = resolvedShadow.alpha * cardAlpha),
                ambientColor = resolvedShadow.copy(alpha = resolvedShadow.alpha * cardAlpha)
            )
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = colors.map { it.copy(alpha = it.alpha * cardAlpha) },
                    start = gradientStartOffset,
                    end = gradientEndOffset
                )
            )
            .border(
                width = borderWidth,
                color = currentBorderColor.copy(alpha = currentBorderColor.alpha * cardAlpha),
                shape = shape
            )
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = ripple(
                            bounded = true,
                            color = resolvedBorder
                        )
                    ) { onClick() }
                } else Modifier
            )
            .drawBehind {
                if (enableInnerGlow) {
                    val glowWidthPx = innerGlowWidth.toPx()
                    val cornerRadiusPx = getCornerRadiusInPx(shape, size, density, 28.dp)

                    drawRoundRect(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                resolvedGlow.copy(
                                    alpha = resolvedGlow.alpha * innerGlowAlpha * cardAlpha
                                ),
                                resolvedGlow.copy(alpha = 0f)
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
            modifier = Modifier.padding(innerPadding)
        ) {
            content()
        }
    }
}

// Helper function to get corner radius in pixels
private fun getCornerRadiusInPx(shape: Shape, size: Size, density: Density, defaultRadius: Dp): Float {
    return when (shape) {
        is RoundedCornerShape -> {
            val topStart = shape.topStart.toPx(size, density)
            val topEnd = shape.topEnd.toPx(size, density)
            val bottomEnd = shape.bottomEnd.toPx(size, density)
            val bottomStart = shape.bottomStart.toPx(size, density)
            listOf(topStart, topEnd, bottomEnd, bottomStart).maxOrNull()
                ?: with(density) { defaultRadius.toPx() }
        }

        else -> with(density) { defaultRadius.toPx() }
    }
}


@Preview(showBackground = false)
@Composable
fun GlassmorphismCardPreview() {
    // Red glassmorphism card
    MaterialTheme {
        Column {
            AdvancedGlassmorphismCard(
                backgroundColor = Color.Red.copy(alpha = 0.15f),
                borderColor = Color.Red.copy(alpha = 0.3f),
                gradientColors = listOf(
                    Color.Red.copy(alpha = 0.2f),
                    Color.Red.copy(alpha = 0.05f)
                ),
                innerGlowColor = Color.Red.copy(alpha = 0.15f),
                shadowColor = Color.Black.copy(alpha = 0.2f)
            ) {
                Text("Red Card", color = Color.White)
            }

// Blue glassmorphism card
            AdvancedGlassmorphismCard(
                backgroundColor = Color.Blue.copy(alpha = 0.15f),
                borderColor = Color.Cyan.copy(alpha = 0.3f),
                gradientColors = listOf(
                    Color.Blue.copy(alpha = 0.2f),
                    Color(0xFF1A237E).copy(alpha = 0.1f)
                ),
                innerGlowColor = Color.Cyan.copy(alpha = 0.2f)
            ) {
                Text("Blue Card", color = Color.White)
            }

// Purple gradient card
            AdvancedGlassmorphismCard(
                gradientColors = listOf(
                    Color(0xFF9C27B0).copy(alpha = 0.3f),
                    Color(0xFFE040FB).copy(alpha = 0.1f),
                    Color(0xFF7B1FA2).copy(alpha = 0.05f)
                ),
                borderColor = Color(0xFFE040FB).copy(alpha = 0.4f),
                innerGlowColor = Color(0xFFE040FB).copy(alpha = 0.2f),
                gradientAngle = 135f
            ) {
                Text("Purple Gradient Card", color = Color.White)
            }

// Dark theme custom card
            AdvancedGlassmorphismCard(
                backgroundColor = Color(0xFF1A1A1A).copy(alpha = 0.8f),
                borderColor = Color(0xFF4CAF50).copy(alpha = 0.5f),
                gradientColors = listOf(
                    Color(0xFF2E7D32).copy(alpha = 0.3f),
                    Color(0xFF1B5E20).copy(alpha = 0.1f)
                ),
                innerGlowColor = Color(0xFF4CAF50).copy(alpha = 0.15f),
                shadowColor = Color.Magenta.copy(alpha = 0.1f)

            ) {
                Text("Custom Dark Green Card", color = Color.White, modifier = Modifier.background(Color.Transparent))
            }


        }
    }

}


@Preview(showBackground = true)
@Composable
fun GlassmorphismCardPreview2() {
    // Red glassmorphism card
    Column(
        modifier = Modifier.background(Color.Blue)
    ) {
        MaterialTheme(
            colorScheme = lightColorScheme(),
        ) {
            AdvancedGlassmorphismCard {
                Text("Light Card")
            }
        }
        MaterialTheme(
            colorScheme = darkColorScheme(),
        ) {
            AdvancedGlassmorphismCard {
                Text("Dark Card")
            }
        }


    }

}