package com.teamwiney.data.repository.wine

import com.teamwiney.core.common.base.ResponseWrapper
import com.teamwiney.data.network.adapter.ApiResult
import com.teamwiney.data.network.model.response.RecommendWineResponse
import com.teamwiney.data.network.model.response.WineTipsResponse
import kotlinx.coroutines.flow.Flow

interface WineRepository {

    fun getRecommendWines(): Flow<ApiResult<ResponseWrapper<List<RecommendWineResponse>>>>

    fun getWineTips(page: Int, size: Int): Flow<ApiResult<ResponseWrapper<WineTipsResponse>>>

}