package com.teamwiney.data.repository.wine

import com.teamwiney.core.common.base.ResponseWrapper
import com.teamwiney.data.network.adapter.ApiResult
import com.teamwiney.data.network.model.response.PagingData
import com.teamwiney.data.network.model.response.RecommendWine
import com.teamwiney.data.network.model.response.Wine
import com.teamwiney.data.network.model.response.WineTip
import kotlinx.coroutines.flow.Flow

interface WineRepository {

    fun getRecommendWines(): Flow<ApiResult<ResponseWrapper<List<RecommendWine>>>>

    fun getWineDetail(wineId: Long): Flow<ApiResult<ResponseWrapper<Wine>>>

    fun getWineTips(page: Int, size: Int): Flow<ApiResult<ResponseWrapper<PagingData<List<WineTip>>>>>

}