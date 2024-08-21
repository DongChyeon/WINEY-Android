package com.teamwiney.core.common

enum class AmplitudeEvent {
    SIGNUP_FLOW_ENTER,
    SIGNUP_COMPLETE_HOME,
    SIGNUP_FLOW_BACK_CLICK,

    HOME_ENTER,
    WINE_DETAIL_CLICK,
    ANALYZE_BUTTON_CLICK,
    TIP_POST_CLICK,

    NOTE_CREATE_BUTTON_CLICK,
    WINE_SELECT_BUTTON_CLICK,
    ALCOHOL_INPUT_SKIP_CLICK,
    ALCOHOL_INPUT_NEXT_CLICK,
    VINTAGE_PRICE_INPUT_SKIP_CLICK,
    VINTAGE_PRICE_INPUT_NEXT_CLICK,
    COLOR_SCENT_INPUT_SKIP_CLICK,
    COLOR_SCENT_INPUT_NEXT_CLICK,
    SCENT_HELP_CLICK,
    TASTE_INPUT_NEXT_CLICK,
    TASTE_INPUT_BACK_CLICK,
    TASTE_HELP_CLICK,
    REVIEW_COMPLETE_CLICK,
    REVIEW_COMPLETE_BACK_CLICK,

    MYPAGE_ENTER,
    WINEY_BADGE_CLICK,
    YOUNG_BADGE_CLICK,

    MAP_ENTER;

    val eventName: String
        get() = name
}