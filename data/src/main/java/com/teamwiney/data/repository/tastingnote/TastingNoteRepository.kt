package com.teamwiney.data.repository.tastingnote

import com.teamwiney.core.common.base.ResponseWrapper
import com.teamwiney.core.common.`typealias`.BaseResponse
import com.teamwiney.data.network.adapter.ApiResult
import com.teamwiney.data.network.model.response.PagingResponse
import com.teamwiney.data.network.model.response.TasteAnalysis
import com.teamwiney.data.network.model.response.TastingNote
import com.teamwiney.data.network.model.response.TastingNoteDetail
import com.teamwiney.data.network.model.response.TastingNoteFilters
import kotlinx.coroutines.flow.Flow

interface TastingNoteRepository {

    fun getTasteAnalysis(): Flow<ApiResult<ResponseWrapper<TasteAnalysis>>>

    fun getTastingNotes(
        page: Int,
        size: Int,
        order: Int,
        countries: List<String>,
        wineTypes: List<String>,
        buyAgain: Int
    ): Flow<ApiResult<ResponseWrapper<PagingResponse<List<TastingNote>>>>>

    fun getTastingNoteFilters(): Flow<ApiResult<ResponseWrapper<TastingNoteFilters>>>

    fun getTastingNoteDetail(noteId: Int): Flow<ApiResult<ResponseWrapper<TastingNoteDetail>>>

    fun deleteTastingNote(noteId: Int): Flow<ApiResult<BaseResponse>>
}