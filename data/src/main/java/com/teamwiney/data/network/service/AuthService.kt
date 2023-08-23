package com.teamwiney.data.network.service

import com.google.gson.annotations.SerializedName
import com.teamwiney.core.common.base.ResponseWrapper
import com.teamwiney.core.common.`typealias`.BaseResponse
import com.teamwiney.data.network.adapter.ApiResult
import com.teamwiney.data.network.model.request.SocialLoginRequest
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

enum class SocialType {
    @SerializedName("KAKAO")
    KAKAO,

    @SerializedName("GOOGLE")
    GOOGLE,

    @SerializedName("normal")
    normal
}

interface AuthService {

    @POST("/login/{social}")
    suspend fun socialLogin(
        @Path("social") social: SocialType,
        @Body socialLoginRequest: SocialLoginRequest
    ): ApiResult<BaseResponse>

    @POST("/auth/kakao")
    suspend fun kakaoLogin(
        @Query("code") code: String
    ): ApiResult<BaseResponse>

}