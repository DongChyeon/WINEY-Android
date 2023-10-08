package com.teamwiney.data.datasource

import com.teamwiney.core.common.base.ResponseWrapper
import com.teamwiney.data.di.DispatcherModule
import com.teamwiney.data.network.adapter.ApiResult
import com.teamwiney.data.network.model.response.TasteAnalysisResponse
import com.teamwiney.data.network.model.response.TastingNoteFiltersResponse
import com.teamwiney.data.network.service.TastingNoteService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TastingNoteDataSourceImpl @Inject constructor(
    private val tastingNoteService: TastingNoteService,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TastingNoteDataSource {

    override fun getTasteAnalysis(): Flow<ApiResult<ResponseWrapper<TasteAnalysisResponse>>> =
        flow {
            emit(tastingNoteService.getTasteAnalysis())
        }.flowOn(ioDispatcher)

    override fun getTastingNoteFilters(): Flow<ApiResult<ResponseWrapper<TastingNoteFiltersResponse>>> =
        flow {
            emit(tastingNoteService.getTastingNoteFilters())
        }.flowOn(ioDispatcher)

}