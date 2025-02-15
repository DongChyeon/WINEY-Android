package com.teamwiney.notewrite

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamwiney.core.common.AmplitudeEvent
import com.teamwiney.core.common.AmplitudeProvider
import com.teamwiney.core.common.WineyAppState
import com.teamwiney.core.common.navigation.NoteDestinations
import com.teamwiney.core.common.navigation.NoteDestinations.Write.INFO_STANDARD_SMELL
import com.teamwiney.notedetail.component.NoteFeatureText
import com.teamwiney.ui.components.ColorSlider
import com.teamwiney.ui.components.HeightSpacer
import com.teamwiney.ui.components.TopBar
import com.teamwiney.ui.components.WButton
import com.teamwiney.ui.components.bottomBorder
import com.teamwiney.ui.theme.LocalColors
import com.teamwiney.ui.theme.WineyTheme

data class WineSmellKeyword(
    val title: String,
    val options: List<WineSmellOption>,
)

data class WineSmellOption(
    val name: String,
    val value: String
)


@Composable
fun NoteWineInfoColorAndSmellScreen(
    appState: WineyAppState,
    viewModel: NoteWriteViewModel,
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BackHandler {
        appState.navController.navigateUp()
        AmplitudeProvider.trackEvent(AmplitudeEvent.COLOR_SCENT_INPUT_BACK_CLICK)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WineyTheme.colors.background_1)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        TopBar(
            content = "와인 정보 입력",
        ) {
            appState.navController.navigateUp()
            AmplitudeProvider.trackEvent(AmplitudeEvent.COLOR_SCENT_INPUT_BACK_CLICK)
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
        ) {
            WineColorPicker(
                initialColor = uiState.initialColor,
                currentColor = uiState.wineNote.color,
                barColors = uiState.barColors
            ) {
                viewModel.updateColor(it)
            }
            HeightSpacer(35.dp)
            WineFlavorPicker(
                wineSmellKeywords = uiState.wineSmellKeywords,
                isWineSmellKeywordSelected = viewModel::isWineSmellSelected,
                updateWineSmell = { wineSmellOption ->
                    viewModel.updateWineSmell(wineSmellOption)
                },
                navigateToStandardSmell = {
                    appState.navigate(INFO_STANDARD_SMELL)
                    AmplitudeProvider.trackEvent(AmplitudeEvent.SCENT_HELP_CLICK)
                }
            )
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(top = 20.dp, bottom = 40.dp),
        ) {
            WButton(
                text = "다음",
                modifier = Modifier
                    .weight(3f),
                enableBackgroundColor = WineyTheme.colors.main_2,
                disableBackgroundColor = WineyTheme.colors.gray_900,
                disableTextColor = WineyTheme.colors.gray_600,
                enableTextColor = WineyTheme.colors.gray_50,
                onClick = {
                    appState.navController.navigate(NoteDestinations.Write.INFO_FLAVOR)
                    AmplitudeProvider.trackEvent(AmplitudeEvent.COLOR_SCENT_INPUT_NEXT_CLICK)
                }
            )
        }
    }
}

@Composable
private fun WineFlavorPicker(
    wineSmellKeywords: List<WineSmellKeyword>,
    isWineSmellKeywordSelected: (WineSmellOption) -> Boolean,
    updateWineSmell: (WineSmellOption) -> Unit = {},
    navigateToStandardSmell: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = WineyTheme.colors.gray_50
                        )
                    ) {
                        append("와인 향은요? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = WineyTheme.colors.gray_600,
                            fontSize = 14.sp
                        )
                    ) {
                        append("(선택)")
                    }
                },
                style = WineyTheme.typography.bodyB1
            )
            Text(
                text = "향표현이 어려워요!",
                style = WineyTheme.typography.captionM2,
                color = WineyTheme.colors.gray_500,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navigateToStandardSmell()
                }
            )
        }
        HeightSpacer(20.dp)
        Column(
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            wineSmellKeywords.forEach {
                WineSmellContainer(
                    wineSmellKeyword = it,
                    isWineSmellKeywordSelected = isWineSmellKeywordSelected,
                    updateWineSmell = updateWineSmell
                )
            }
        }
    }
}

@Composable
private fun WineSmellContainer(
    wineSmellKeyword: WineSmellKeyword,
    isWineSmellKeywordSelected: (WineSmellOption) -> Boolean,
    updateWineSmell: (WineSmellOption) -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = wineSmellKeyword.title,
            modifier = Modifier.padding(start = 24.dp),
            style = WineyTheme.typography.bodyB2,
            color = WineyTheme.colors.gray_500
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(wineSmellKeyword.options) {
                NoteFeatureText(
                    name = it.name,
                    enable = isWineSmellKeywordSelected(it),
                ) {
                    updateWineSmell(it)
                }
            }
        }
    }
}

@Composable
private fun WineColorPicker(
    initialColor: Color,
    currentColor: Color,
    barColors: List<Color>,
    updateCurrentColor: (Color) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 20.dp)
    ) {
        Text(
            text = "와인 컬러는요?",
            style = WineyTheme.typography.bodyB1,
            color = WineyTheme.colors.gray_50,
        )
        HeightSpacer(height = 10.dp)
        Text(
            text = "드신 와인 색감에 맞게 핀을 설정해주세요!",
            style = WineyTheme.typography.bodyB2,
            color = WineyTheme.colors.gray_800,
        )
        HeightSpacer(height = 30.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WineyTheme.colors.background_1),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Spacer(
                        modifier = Modifier
                            .background(
                                brush = Brush.radialGradient(
                                    listOf(
                                        currentColor,
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            )
                            .size(48.dp)
                    )

                    ColorSlider(
                        initialColor = initialColor,
                        onValueChange = updateCurrentColor,
                        barColors = barColors,
                        trackHeight = 10.dp,
                        thumbSize = 22.dp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WineInfoTextField(
    value: String = "asdasd",
    onValueChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholderText: String = "",
    fontSize: TextUnit = 16.sp,
    focusRequest: FocusRequester? = null,
    keyboardOptions: KeyboardOptions? = null,
    keyboardActions: KeyboardActions? = null,
    onFocusedChange: (Boolean) -> Unit = {},
    onErrorState: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLength: Int = 25,
    placeholderTextAlign: TextAlign = TextAlign.Center,
) {
    val localColors = LocalColors.current
    val bottomLineColor = remember {
        mutableStateOf(localColors.gray_800)
    }

    Column(modifier = modifier) {
        BasicTextField(
            modifier = Modifier
                .onFocusChanged {
                    onFocusedChange(it.isFocused)
                    if (it.isFocused) {
                        bottomLineColor.value = localColors.gray_50
                    } else {
                        bottomLineColor.value = localColors.gray_800
                    }
                }
                .bottomBorder(1.dp, if (onErrorState) localColors.error else bottomLineColor.value)
                .focusRequester(focusRequest ?: FocusRequester()),
            value = value,
            onValueChange = {
                if (it.length <= maxLength) onValueChanged(it)
            },
            singleLine = true,
            cursorBrush = SolidColor(Color.White),
            textStyle = WineyTheme.typography.bodyM1.copy(
                color = localColors.gray_50,
                textAlign = TextAlign.Center
            ),
            keyboardOptions = keyboardOptions ?: KeyboardOptions(),
            keyboardActions = keyboardActions ?: KeyboardActions(),
            decorationBox = { innerTextField ->
                Box(
                    Modifier
                        .padding(0.dp, 9.dp)
                        .fillMaxWidth()
                ) {
                    if (value.isEmpty()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = placeholderText,
                            style = LocalTextStyle.current.copy(
                                color = localColors.gray_800,
                                fontSize = fontSize,
                            ),
                            textAlign = placeholderTextAlign,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        innerTextField()
                    }
                    if (trailingIcon != null) Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                        trailingIcon()
                    }
                }
            },
            visualTransformation = visualTransformation,
        )
    }
}