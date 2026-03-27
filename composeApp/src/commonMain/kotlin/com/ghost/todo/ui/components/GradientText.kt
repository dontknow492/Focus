package com.ghost.todo.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun GradientText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    colors: List<Color> = listOf(Color.Blue, Color.Magenta),
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle = FontStyle.Normal
) {
    val brush = Brush.linearGradient(
        colors = colors,
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, 0f)
    )

    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            brush = brush,
            fontSize = if (fontSize != TextUnit.Unspecified) fontSize else style.fontSize,
            fontWeight = fontWeight ?: style.fontWeight,
            letterSpacing = if (letterSpacing != TextUnit.Unspecified) letterSpacing else style.letterSpacing,
        ),
        color = Color.Unspecified,
        lineHeight = if (lineHeight != TextUnit.Unspecified) lineHeight else style.lineHeight,
        fontStyle = fontStyle
    )
}
