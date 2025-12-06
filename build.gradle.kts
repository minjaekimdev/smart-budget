// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}

buildscript {
    dependencies {
        // ... 기존 코드들 ...

        // 이 줄을 추가하세요! (버전은 다를 수 있지만 보통 2.5.0 이상 사용)
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
    }
}