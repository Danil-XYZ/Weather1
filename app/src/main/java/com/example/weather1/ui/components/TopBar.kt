package com.example.weather1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weather1.R

@Composable
fun TopBarRow(
    modifier: Modifier = Modifier,
    title: String? = null,
    iconPainterFirst: Painter = painterResource(id = R.drawable.ic_baseline_add_24),
    iconPainterSecond: Painter? = painterResource(id = R.drawable.ic_baseline_more_vert_24),
    onClickFirst: () -> Unit,
    onClicSecond: (() -> Unit)? = null
) {
    TopAppBar(
        backgroundColor = Color.Transparent, contentColor = MaterialTheme.colors.onBackground,
        contentPadding = PaddingValues(horizontal = 16.dp),
        elevation = 0.dp
    ) {
        Row(modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
            ) {

            Icon(
                modifier = Modifier
                    .background(
                        color = Color.Transparent,
                        shape = RoundedCornerShape(100)
                    )
                    .clip(RoundedCornerShape(100))
                    .clickable { onClickFirst() },
                painter = iconPainterFirst,
                contentDescription = null,
                tint = Color.White
            )

            title?.let { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp,

                    )
            }

            iconPainterSecond?.let {
                Icon(
                    modifier = Modifier.clickable { onClicSecond?.invoke() },
                    painter = it,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}