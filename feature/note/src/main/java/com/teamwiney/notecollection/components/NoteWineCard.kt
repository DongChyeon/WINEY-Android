package com.teamwiney.notecollection.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.teamwiney.core.design.R
import com.teamwiney.ui.components.CardProperties
import com.teamwiney.ui.components.HeightSpacer
import com.teamwiney.ui.theme.Chaviera
import com.teamwiney.ui.theme.WineyTheme

@Composable
fun NoteWineCard(
    modifier: Modifier = Modifier,
    color: String,
    name: String,
    origin: String,
    starRating: Int
) {
    val (wineName, image, borderColor, gradientCircleColor, circleColor, cardColor) = when (color) {
        "RED" -> CardProperties(
            color,
            R.drawable.ic_red_wine,
            Color(0xFFA87575),
            listOf(Color(0xFFC06868), Color(0xFF7C2B2B)),
            Color(0xFF640D0D),
            Color(0xFF441010)
        )

        "WHITE" -> CardProperties(
            color,
            R.drawable.ic_white_wine,
            Color(0xFFC1BA9E),
            listOf(Color(0xFFAEAB99), Color(0xFF754A09)),
            Color(0xFF898472),
            Color(0xFF7A706D)
        )

        "ROSE" -> CardProperties(
            color,
            R.drawable.ic_rose_wine,
            Color(0xFFC9A4A1),
            listOf(Color(0xFFAD96A4), Color(0xFFCFA98D)),
            Color(0xFFBA7A71),
            Color(0xFF8F6C64)
        )

        "SPARKL" -> CardProperties(
            color,
            R.drawable.ic_sparkl_wine,
            Color(0xFFA78093),
            listOf(Color(0xFF827D6B), Color(0xFFBAC59C)),
            Color(0xFF777151),
            Color(0xFF4F5144)
        )

        "PORT" -> CardProperties(
            color,
            R.drawable.ic_port_wine,
            Color(0xFFB09A86),
            listOf(Color(0xFF4A2401), Color(0xFF77503A)),
            Color(0xFF4F3F28),
            Color(0xFF3A2F2F)
        )

        else -> CardProperties(
            color,
            R.drawable.ic_etc_wine,
            Color(0xFF768169),
            listOf(Color(0xFF3C3D12), Color(0xFF465C18)),
            Color(0xFF2D4328),
            Color(0xFF233124)
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(7.dp))
                .border(
                    BorderStroke(
                        1.dp, brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF9671FF),
                                Color(0x109671FF),
                            )
                        )
                    ),
                    RoundedCornerShape(7.dp)
                )
                .background(WineyTheme.colors.background_1)
        ) {
            NoteCardSurface(
                modifier = Modifier.fillMaxSize(),
                gradientCircleColor = gradientCircleColor,
                circleColor = circleColor
            )
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        text = wineName,
                        style = TextStyle(
                            fontFamily = Chaviera,
                            fontWeight = FontWeight.Normal,
                            fontSize = 25.sp,
                        ),
                        color = WineyTheme.colors.gray_50,
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_thismooth),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .size(13.dp)
                    )
                }
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "IMG_SPARKL_WINE",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 10.dp)
                        .offset(y = (-10).dp)
                )
            }

        }
        HeightSpacer(height = 10.dp)
        Text(
            text = name,
            color = WineyTheme.colors.gray_50,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth(),
            style = WineyTheme.typography.captionB1,
        )
        HeightSpacer(height = 2.dp)
        Text(
            text = "$origin / ${starRating}점",
            color = WineyTheme.colors.gray_700,
            style = WineyTheme.typography.captionM2
        )
    }
}

@Composable
private fun NoteCardSurface(
    modifier: Modifier = Modifier,
    gradientCircleColor: List<Color>,
    circleColor: Color
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .blur(30.dp)
            .alpha(0.4f)
            .background(
                color = Color(0x993F3F3F),
                shape = RoundedCornerShape(5.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        NoteWineCardCircle(
            gradientCircleColor = gradientCircleColor,
            circleColor = circleColor
        )
    }
}

@Composable
private fun NoteWineCardCircle(
    gradientCircleColor: List<Color>,
    circleColor: Color
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
    ) {
        drawCircle(
            brush = Brush.verticalGradient(gradientCircleColor),
            radius = 35.dp.toPx(),
            center = Offset(x = 40.dp.toPx(), y = 20.dp.toPx())
        )

        drawCircle(
            color = circleColor,
            radius = 65.dp.toPx(),
            center = Offset(x = (size.width / 1.5f), y = (size.height / 1.5f))
        )
    }
}