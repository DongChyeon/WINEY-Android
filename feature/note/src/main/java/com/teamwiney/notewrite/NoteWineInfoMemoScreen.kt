package com.teamwiney.notewrite

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.navOptions
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.teamwiney.core.common.AmplitudeEvent
import com.teamwiney.core.common.AmplitudeProvider
import com.teamwiney.core.common.WineyAppState
import com.teamwiney.core.common.navigation.NoteDestinations
import com.teamwiney.core.design.R
import com.teamwiney.data.network.model.response.TastingNoteImage
import com.teamwiney.notedetail.component.NoteFeatureText
import com.teamwiney.ui.components.TopBar
import com.teamwiney.ui.components.WButton
import com.teamwiney.ui.components.WRoundTextField
import com.teamwiney.ui.components.imagepicker.ImagePickerContract
import com.teamwiney.ui.theme.WineyTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun NoteWineInfoMemoScreen(
    appState: WineyAppState,
    viewModel: NoteWriteViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val effectFlow = viewModel.effect

    val context = LocalContext.current

    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )
    val allPermissionsGranted = permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ImagePickerContract(),
        onResult = { uris ->
            uris?.let {
                viewModel.addNoteImages(
                    it.map { uri ->
                        TastingNoteImage(
                            imgId = "",
                            imgUrl = "",
                            contentUri = uri
                        )
                    }
                )
            }
        }
    )

    val launchMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMap ->
        val areGranted = permissionMap.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            imagePicker.launch(3 - uiState.wineNote.selectedImages.size)
        } else {
            appState.showSnackbar("미디어 권한과 카메라 권한을 허용해야 갤러리를 사용할 수 있습니다")

            // 허용하지 않았을 경우 설정창으로 이동
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", context.packageName, null)
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(context, intent, null)
            }, 500)
        }
    }

    LaunchedEffect(true) {
        effectFlow.collectLatest { effect ->
            when (effect) {
                is NoteWriteContract.Effect.NavigateTo -> {
                    appState.navigate(effect.destination, effect.navOptions)
                }

                is NoteWriteContract.Effect.ShowSnackBar -> {
                    appState.showSnackbar(effect.message)
                }

                is NoteWriteContract.Effect.NoteWriteSuccess -> {
                    appState.navigate(NoteDestinations.Write.COMPLETE)
                }
            }
        }
    }

    BackHandler {
        appState.navController.navigateUp()
        AmplitudeProvider.trackEvent(AmplitudeEvent.REVIEW_COMPLETE_BACK_CLICK)
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
            AmplitudeProvider.trackEvent(AmplitudeEvent.REVIEW_COMPLETE_BACK_CLICK)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Text(
                text = "마지막이에요!",
                style = WineyTheme.typography.bodyB1,
                color = WineyTheme.colors.main_2,
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = WineyTheme.colors.gray_50,
                            fontSize = 17.sp
                        )
                    ) {
                        append("와인에 대한 메모를 작성해주세요. ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = WineyTheme.colors.gray_600,
                            fontSize = 14.sp
                        )
                    ) {
                        append("(선택)")
                    }
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .horizontalScroll(rememberScrollState())
            ) {
                uiState.wineNote.selectedImages.map { image ->
                    Box {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(
                                    image.imgUrl.ifEmpty { image.contentUri }
                                )
                                .build(),
                            contentDescription = "IMG_URL",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(WineyTheme.colors.gray_900),
                            contentScale = ContentScale.Crop
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close_fill_18),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(7.dp)
                                .align(Alignment.TopEnd)
                                .clickable {
                                    viewModel.removeNoteImage(image)
                                }
                                .size(18.dp),
                            tint = Color.Unspecified
                        )
                    }
                }
            }

            Button(
                onClick = {
                    if (uiState.wineNote.selectedImages.size < 3) {
                        if (!allPermissionsGranted) {
                            launchMultiplePermissions.launch(permissions)
                        } else {
                            imagePicker.launch(3 - uiState.wineNote.selectedImages.size)
                        }
                    } else {
                        appState.showSnackbar("사진은 최대 3장까지 첨부 가능합니다")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(),
                border = BorderStroke(1.dp, WineyTheme.colors.main_2),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(5.dp),
                enabled = true
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "사진 첨부하기",
                        color = WineyTheme.colors.main_2,
                        style = WineyTheme.typography.bodyM2,
                        modifier = Modifier.padding(vertical = 8.5.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_camera_baseline_30),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }

            WRoundTextField(
                value = uiState.wineNote.memo,
                onValueChange = viewModel::updateMemo,
                placeholderText = "와인에 대한 생각을 작성해주세요 :)"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "와인은 어떠셨어요?",
                    style = WineyTheme.typography.bodyB1,
                    color = WineyTheme.colors.gray_50,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    repeat(5) {
                        Icon(
                            painter = painterResource(id = if (uiState.wineNote.rating > it) R.mipmap.ic_wine_filled else R.mipmap.ic_wine_unfilled),
                            contentDescription = null,
                            modifier = Modifier
                                .size(30.dp)
                                .clickable {
                                    viewModel.updateRating(it + 1)
                                },
                            tint = Color.Unspecified
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "재구매 의사",
                    style = WineyTheme.typography.bodyB1,
                    color = WineyTheme.colors.gray_50,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    NoteFeatureText(name = "있어요", enable = uiState.wineNote.buyAgain == true) {
                        viewModel.updateBuyAgain(true)
                    }
                    NoteFeatureText(name = "없어요", enable = uiState.wineNote.buyAgain == false) {
                        viewModel.updateBuyAgain(false)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "노트 공개 여부",
                    style = WineyTheme.typography.bodyB1,
                    color = WineyTheme.colors.gray_50,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    NoteFeatureText(name = "있어요", enable = uiState.wineNote.public == true) {
                        viewModel.updatePublic(true)
                    }
                    NoteFeatureText(name = "없어요", enable = uiState.wineNote.public == false) {
                        viewModel.updatePublic(false)
                    }
                }
            }
        }

        WButton(
            text = "작성완료",
            modifier = Modifier
                .padding(bottom = 40.dp)
                .padding(horizontal = 20.dp),
            enableBackgroundColor = WineyTheme.colors.main_2,
            disableBackgroundColor = WineyTheme.colors.gray_900,
            disableTextColor = WineyTheme.colors.gray_600,
            enableTextColor = WineyTheme.colors.gray_50,
            enabled = uiState.wineNote.rating != 0 && uiState.wineNote.buyAgain != null && uiState.wineNote.public != null,
            onClick = {
                if (uiState.mode == EditMode.ADD) viewModel.writeTastingNote() else viewModel.updateTastingNote()
                AmplitudeProvider.trackEvent(AmplitudeEvent.REVIEW_COMPLETE_CLICK)
            }
        )
    }
}