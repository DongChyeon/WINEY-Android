package com.teamwiney.auth.signup

import androidx.lifecycle.viewModelScope
import com.teamwiney.core.common.AuthDestinations
import com.teamwiney.core.common.base.BaseViewModel
import com.teamwiney.data.network.adapter.ApiResult
import com.teamwiney.data.network.model.request.PhoneNumberRequest
import com.teamwiney.data.repository.AuthRepository
import com.teamwiney.ui.signup.state.SignUpFavoriteCategoryiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel<SignUpContract.State, SignUpContract.Event, SignUpContract.Effect>(
    initialState = SignUpContract.State()
) {

    override fun reduceState(event: SignUpContract.Event) {
        when (event) {
            is SignUpContract.Event.SendAuthenticationButtonClicked -> {
                sendAuthenticationNumber()
            }

            is SignUpContract.Event.BackToLoginButtonClicked -> {
                postEffect(
                    SignUpContract.Effect.ShowBottomSheet(
                        SignUpContract.BottomSheet.ReturnToLogin
                    )
                )
            }

            is SignUpContract.Event.CancelTasteSelectionButtonClicked -> {
                postEffect(
                    SignUpContract.Effect.ShowBottomSheet(
                        SignUpContract.BottomSheet.CancelTasteSelection
                    )
                )
            }

            is SignUpContract.Event.TasteSelectionLastItemClicked -> {
                postEffect(
                    SignUpContract.Effect.NavigateTo(AuthDestinations.SignUp.COMPLETE)
                )
            }
        }
    }

    private fun sendAuthenticationNumber() = viewModelScope.launch {
        authRepository.sendAuthCodeMessage(PhoneNumberRequest(currentState.phoneNumber)).onStart {
            updateState(currentState.copy(isLoading = true))
        }.collectLatest {
            updateState(currentState.copy(isLoading = false))
            when (it) {
                is ApiResult.Success -> {
                    postEffect(
                        SignUpContract.Effect.ShowBottomSheet(
                            SignUpContract.BottomSheet.SendMessage
                        )
                    )
                }

                else -> {
                    postEffect(SignUpContract.Effect.ShowSnackBar("네트워크 에러가 발생했습니다."))
                }
            }
        }
    }


    fun updatePhoneNumber(phoneNumber: String) = viewModelScope.launch {
        updateState(currentState.copy(phoneNumber = phoneNumber))
    }

    fun updatePhoneNumberErrorState(isError: Boolean) = viewModelScope.launch {
        updateState(currentState.copy(phoneNumberErrorState = isError))
    }

    fun updateVerifyNumber(verifyNumber: String) = viewModelScope.launch {
        updateState(currentState.copy(verifyNumber = verifyNumber))
    }

    fun updateVerifyNumberErrorState(isError: Boolean) = viewModelScope.launch {
        updateState(currentState.copy(verifyNumberErrorState = isError))
    }

    fun updateRemainingTime(remainingTime: Int) = viewModelScope.launch {
        updateState(currentState.copy(remainingTime = remainingTime))
    }

    fun updateIsTimerRunning(isTimerRunning: Boolean) = viewModelScope.launch {
        updateState(currentState.copy(isTimerRunning = isTimerRunning))
    }

    fun resetTimer() = viewModelScope.launch {
        updateState(
            currentState.copy(
                remainingTime = SignUpContract.VERIFY_NUMBER_TIMER,
                isTimerRunning = true
            )
        )
    }

    fun updateSignUpFavoriteItem(signUpFavoriteCategoryiState: SignUpFavoriteCategoryiState) =
        viewModelScope.launch {
            updateState(
                currentState.copy(
                    favoriteTastes = currentState.favoriteTastes.map {
                        if (it.title == signUpFavoriteCategoryiState.title) {
                            signUpFavoriteCategoryiState
                        } else {
                            it
                        }
                    }
                )
            )
        }
}

