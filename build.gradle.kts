// 모든 하위 프로젝트/모듈에 공통적인 구성 옵션을 추가하는 최상위 빌드
plugins {
    with(Plugins) {
        id(ANDROID_APPLICATION) version Versions.AGP apply false
        id(ANDROID_LIBRARY) version Versions.AGP apply false
        id(JETBRAINS_KOTLIN_ANDROID) version Versions.KOTLIN apply false
    }
    `kotlin-dsl`
}