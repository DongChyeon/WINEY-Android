@file:OptIn(ExperimentalMaterialApi::class)

package com.teamwiney.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teamwiney.ui.components.HeightSpacer
import com.teamwiney.ui.components.PhoneNumberVisualTransformation
import com.teamwiney.ui.signup.SignUpBottomSheet
import com.teamwiney.ui.signup.SignUpTopBar
import com.teamwiney.ui.components.WButton
import com.teamwiney.ui.components.WTextField
import com.teamwiney.ui.theme.WineyTheme
import kotlinx.coroutines.launch

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFF1F2126
)
@Composable
fun SignUpPhoneScreen(
    onBack: () -> Unit = { },
    onConfirm: () -> Unit = { }
) {

    var phoneNumber by remember {
        mutableStateOf("")
    }
    var errorState by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(phoneNumber) {
        errorState = !(phoneNumber.length == 11 || phoneNumber.isEmpty())
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val bottomSheetState =
        rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden,
            confirmValueChange = {
                if (it == ModalBottomSheetValue.Hidden) {
                    // 핸드폰 번호 입력했을 때는 올라갔다가 내려가면 다음화면으로 넘어가게 설정
                    onConfirm()
                }
                true
            })

    SignUpBottomSheet(
        bottomSheetState = bottomSheetState,
        bottomsheetContent = {
            Text(
                text = "인증번호가 발송되었어요\n3분 안에 인증번호를 입력해주세요",
                style = WineyTheme.typography.bodyB1,
                color = WineyTheme.colors.gray_200,
                textAlign = TextAlign.Center
            )
            HeightSpacer(height = 14.dp)
            Text(
                text = "*인증번호 요청 3회 초과 시 5분 제한",
                style = WineyTheme.typography.captionM2,
                color = WineyTheme.colors.gray_600
            )
            HeightSpacer(height = 26.dp)
            WButton(
                modifier = Modifier.padding(bottom = 20.dp),
                text = "확인",
                onClick = {
                    scope.launch {
                        bottomSheetState.hide()
                        onConfirm()
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WineyTheme.colors.background_1)
                .systemBarsPadding()
                .navigationBarsPadding()
                .imePadding()
        ) {
            SignUpTopBar {
                onBack()
            }
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "휴대폰 번호를 입력해주세요",
                    color = WineyTheme.colors.gray_50,
                    style = WineyTheme.typography.title1
                )
                HeightSpacer(54.dp)
                Text(
                    text = if (errorState) "올바른 번호를 입력해주세요" else "전화번호",
                    color = if (errorState) WineyTheme.colors.error else WineyTheme.colors.gray_600,
                    style = WineyTheme.typography.bodyB2
                )
                HeightSpacer(10.dp)
                WTextField(
                    value = phoneNumber,
                    onValueChanged = { phoneNumber = it },
                    placeholderText = "11자리 입력",
                    maxLength = 11,
                    visualTransformation = PhoneNumberVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            scope.launch {
                                keyboardController?.hide()
                                bottomSheetState.show()
                            }
                        }
                    ),
                    onErrorState = errorState
                )
                Spacer(modifier = Modifier.weight(1f))
                WButton(
                    text = "확인",
                    onClick = {
                        scope.launch {
                            keyboardController?.hide()
                            bottomSheetState.show()
                        }
                    },
                    enabled = phoneNumber.length == 11,
                    modifier = Modifier.padding(bottom = 20.dp)
                )
            }
        }
    }
}
