package com.teamwiney.notecollection

import androidx.navigation.NavOptions
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.teamwiney.core.common.base.UiEffect
import com.teamwiney.core.common.base.UiEvent
import com.teamwiney.core.common.base.UiState
import com.teamwiney.data.network.model.response.TastingNote
import com.teamwiney.data.network.model.response.WineCountryResponse
import com.teamwiney.data.network.model.response.WineTypeResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class NoteContract {

    data class State(
        val isLoading: Boolean = false,
        val tastingNotes: Flow<PagingData<TastingNote>> = flowOf(
            PagingData.from(
                emptyList(),
                LoadStates(
                    prepend = LoadState.NotLoading(endOfPaginationReached = true),
                    append = LoadState.NotLoading(endOfPaginationReached = true),
                    refresh = LoadState.NotLoading(endOfPaginationReached = true),
                )
            )
        ),
        val tastingNotesCount: Int = 0,
        val sortItems: List<String> = listOf("최신순", "평점순"),
        val selectedSort: Int = 0,
        val buyAgainSelected: Boolean = false,
        val typeFilter: List<WineTypeResponse> = emptyList(),
        val selectedTypeFilter: List<WineTypeResponse> = emptyList(),
        val countryFilter: List<WineCountryResponse> = emptyList(),
        val selectedCountryFilter: List<WineCountryResponse> = emptyList(),
    ) : UiState

    sealed class Event : UiEvent {
        object ShowFilter : Event()
        object ApplyFilter : Event()
    }

    sealed class Effect : UiEffect {
        data class NavigateTo(
            val destination: String,
            val navOptions: NavOptions? = null
        ) : Effect()

        data class ShowSnackBar(val message: String) : Effect()
    }

}