package com.teamwiney.auth.signup

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teamwiney.auth.signup.SignUpContract.Companion.PHONE_NUMBER_LENGTH
import com.teamwiney.auth.signup.component.bottomsheet.SendDisabledBottomSheet
import com.teamwiney.auth.signup.component.bottomsheet.SendMessageBottomSheet
import com.teamwiney.auth.signup.component.bottomsheet.SendMessageBottomSheetType
import com.teamwiney.auth.signup.component.bottomsheet.SendTimeExceededLimitBottomSheet
import com.teamwiney.core.common.AmplitudeEvent
import com.teamwiney.core.common.AmplitudeProvider
import com.teamwiney.core.common.WineyAppState
import com.teamwiney.core.common.WineyBottomSheetState
import com.teamwiney.core.common.navigation.AuthDestinations
import com.teamwiney.ui.components.HeightSpacer
import com.teamwiney.ui.components.PhoneNumberVisualTransformation
import com.teamwiney.ui.components.TopBar
import com.teamwiney.ui.components.WButton
import com.teamwiney.ui.components.WTextField
import com.teamwiney.ui.theme.WineyTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpPhoneScreen(
    appState: WineyAppState,
    bottomSheetState: WineyBottomSheetState,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val effectFlow = viewModel.effect

    BackHandler {
        appState.navController.navigateUp()
        AmplitudeProvider.trackEvent(AmplitudeEvent.SIGNUP_FLOW_BACK_CLICK)
    }

    DisposableEffect(true) {
        bottomSheetState.setOnHideBottomSheet {
            bottomSheetState.hideBottomSheet()
            appState.navigate("${AuthDestinations.SignUp.AUTHENTICATION}?phoneNumber=${uiState.phoneNumber}")
        }
        onDispose {
            bottomSheetState.setOnHideBottomSheet { }
        }
    }

    LaunchedEffect(true) {
        AmplitudeProvider.trackEvent(AmplitudeEvent.SIGNUP_FLOW_ENTER)

        effectFlow.collectLatest { effect ->
            when (effect) {
                is SignUpContract.Effect.ShowSnackBar -> {
                    appState.showSnackbar(effect.message)
                }

                is SignUpContract.Effect.ShowBottomSheet -> {
                    when (effect.bottomSheet) {
                        is SignUpContract.BottomSheet.SendMessage -> {
                            bottomSheetState.showBottomSheet {
                                SendMessageBottomSheet(
                                    text = "인증번호가 발송되었어요\n3분 안에 인증번호를 입력해주세요",
                                    sendMessageBottomSheetType = SendMessageBottomSheetType.SEND_MESSAGE
                                ) {
                                    bottomSheetState.hideBottomSheet()
                                    appState.navigate("${AuthDestinations.SignUp.AUTHENTICATION}?phoneNumber=${uiState.phoneNumber}")
                                }
                            }
                        }

                        is SignUpContract.BottomSheet.SendTimeExceededLimit -> {
                            bottomSheetState.showBottomSheet {
                                SendTimeExceededLimitBottomSheet {
                                    bottomSheetState.hideBottomSheet()
                                    appState.navigate(AuthDestinations.Login.ROUTE) {
                                        popUpTo(AuthDestinations.SignUp.ROUTE) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        }

                        is SignUpContract.BottomSheet.SendDisabled -> {
                            bottomSheetState.showBottomSheet {
                                SendDisabledBottomSheet {
                                    bottomSheetState.hideBottomSheet()
                                    appState.navigate(AuthDestinations.Login.ROUTE) {
                                        popUpTo(AuthDestinations.SignUp.ROUTE) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        }

                        is SignUpContract.BottomSheet.UserAlreadyExists -> {
                            bottomSheetState.showBottomSheet {
                                val message = buildAnnotatedString {
                                    append("${formatPhoneNumber(uiState.phoneNumber)}님은\n")
                                    withStyle(style = SpanStyle(WineyTheme.colors.main_3)) {
                                        append(formatLoginType(effect.bottomSheet.message))
                                    }
                                    append(" 회원으로\n")
                                    append("가입하신 기록이 있어요")
                                }

                                SendMessageBottomSheet(
                                    annotatedText = message,
                                    sendMessageBottomSheetType = SendMessageBottomSheetType.USER_ALREADY_EXIST
                                ) {
                                    bottomSheetState.hideBottomSheet()
                                    appState.navigate(AuthDestinations.Login.ROUTE) {
                                        popUpTo(AuthDestinations.SignUp.ROUTE) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        }

                        else -> {}
                    }
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(uiState.phoneNumber) {
        viewModel.updatePhoneNumberErrorState(
            !(uiState.phoneNumber.length == PHONE_NUMBER_LENGTH || uiState.phoneNumber.isEmpty())
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WineyTheme.colors.background_1)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        TopBar(
            leadingIconOnClick = {
                appState.navController.navigateUp()
                AmplitudeProvider.trackEvent(AmplitudeEvent.SIGNUP_FLOW_BACK_CLICK)
            }
        )
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Text(
                text = "휴대폰 번호를 입력해주세요",
                color = WineyTheme.colors.gray_50,
                style = WineyTheme.typography.title1
            )
            HeightSpacer(54.dp)
            Text(
                text = if (uiState.phoneNumberErrorState) "올바른 번호를 입력해주세요" else "전화번호",
                color = if (uiState.phoneNumberErrorState) WineyTheme.colors.error else WineyTheme.colors.gray_600,
                style = if (uiState.phoneNumberErrorState) WineyTheme.typography.bodyM2 else WineyTheme.typography.bodyB2
            )
            HeightSpacer(10.dp)
            WTextField(
                value = uiState.phoneNumber,
                onValueChanged = {
                    viewModel.updatePhoneNumber(it.filter { symbol ->
                        symbol.isDigit()
                    })
                },
                trailingIcon = {
                    if (uiState.phoneNumber.isNotBlank()) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    viewModel.updatePhoneNumber("")
                                },
                            painter = painterResource(id = com.teamwiney.core.design.R.drawable.ic_textfield_delete),
                            contentDescription = "IC_TEXT_DELETE",
                            tint = Color.Unspecified
                        )
                    }
                },
                placeholderText = "${PHONE_NUMBER_LENGTH}자리 입력",
                maxLength = PHONE_NUMBER_LENGTH,
                visualTransformation = PhoneNumberVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        if (!uiState.phoneNumberErrorState) viewModel.processEvent(SignUpContract.Event.SendAuthentication)
                    }
                ),
                onErrorState = uiState.phoneNumberErrorState
            )
            Spacer(modifier = Modifier.weight(1f))
            WButton(
                text = "확인",
                onClick = {
                    keyboardController?.hide()
                    if (!uiState.isLoading) viewModel.processEvent(SignUpContract.Event.SendAuthentication)
                },
                enabled = uiState.phoneNumber.length == PHONE_NUMBER_LENGTH,
                modifier = Modifier.padding(bottom = 30.dp)
            )
        }
    }
}

private fun formatPhoneNumber(input: String): String {
    val part1 = input.substring(0, 3)
    val part2 = input.substring(3, 7)
    val part3 = "****"

    return "$part1 - $part2 - $part3"
}

private fun formatLoginType(input: String): String {
    return when (input) {
        "KAKAO" -> "카카오 소셜"
        else -> "구글 소셜"
    }
}