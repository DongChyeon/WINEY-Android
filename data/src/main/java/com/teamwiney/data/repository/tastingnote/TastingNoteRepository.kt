package com.teamwiney.data.repository.tastingnote

import android.net.Uri
import com.teamwiney.core.common.base.ResponseWrapper
import com.teamwiney.core.common.`typealias`.BaseResponse
import com.teamwiney.data.network.adapter.ApiResult
import com.teamwiney.data.network.model.response.PagingResponse
import com.teamwiney.data.network.model.response.TasteAnalysis
import com.teamwiney.data.network.model.response.TastingNote
import com.teamwiney.data.network.model.response.TastingNoteDetail
import com.teamwiney.data.network.model.response.TastingNoteFilters
import com.teamwiney.data.network.model.response.TastingNoteIdRes
import kotlinx.coroutines.flow.Flow

interface TastingNoteRepository {

    fun getTasteAnalysis(): Flow<ApiResult<ResponseWrapper<TasteAnalysis>>>

    fun getTastingNotes(
        page: Int,
        size: Int,
        order: Int,
        countries: List<String>,
        wineTypes: List<String>,
        buyAgain: Int?
    ): Flow<ApiResult<ResponseWrapper<PagingResponse<List<TastingNote>>>>>

    fun getTastingNotesCount(
        order: Int,
        countries: List<String>,
        wineTypes: List<String>,
        buyAgain: Int?
    ): Flow<ApiResult<ResponseWrapper<PagingResponse<List<TastingNote>>>>>

    fun getTastingNoteFilters(): Flow<ApiResult<ResponseWrapper<TastingNoteFilters>>>

    fun getTastingNoteDetail(noteId: Int): Flow<ApiResult<ResponseWrapper<TastingNoteDetail>>>

    fun deleteTastingNote(noteId: Int): Flow<ApiResult<BaseResponse>>

    fun postTastingNote(
        wineId: Long,
        officialAlcohol: Double,
        alcohol: Int,
        color: String,
        sweetness: Int,
        acidity: Int,
        body: Int,
        tannin: Int,
        finish: Int,
        memo: String,
        rating: Int,
        vintage: String,
        price: String,
        buyAgain: Boolean?,
        smellKeywordList: List<String>,
        imgUris: List<Uri>
    ): Flow<ApiResult<ResponseWrapper<TastingNoteIdRes>>>
}