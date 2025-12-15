import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.androidx.navigation.safeargs)
    id("org.jetbrains.kotlin.kapt")
    //firebase
    id("com.google.gms.google-services")
}

// local.properties에서 비밀키 로드
val localProps = Properties().apply {
    val file = project.rootProject.file("local.properties")
    if (file.exists()) load(file.inputStream())
}
val openaiApiKey: String = localProps.getProperty("openaiApiKey") ?: ""

android {
    namespace = "com.example.logtalk"
    compileSdk = 34 // 36은 아직 미지원 가능성이 높아 34로 안정화

    defaultConfig {
        applicationId = "com.example.logtalk"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 모든 빌드 타입에서 접근 가능한 BuildConfig 필드로 노출
        buildConfigField("String", "OPENAI_API_KEY", "\"${openaiApiKey}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // debug 전용 추가 설정이 필요하면 여기에 배치
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    // 기본 compose, android 의존성 패키지
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.material.icons.extended)
    implementation("com.google.android.material:material:1.11.0")

    // Navigation Components (Fragment용)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Fragment KTX (viewModels delegate)
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //코루틴 및 라이프사이클 (비동기 처리)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // 로컬 데이터베이스 (Room)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx) // Room 코루틴/Kotlin 확장 기능
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    ksp(libs.androidx.room.compiler)

    // 네트워크 (Retrofit + JSON)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp3.logging.interceptor)


    // 테스트 의존성
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config.ktx)
    implementation(libs.firebase.analytics.ktx)

    //openai
    implementation(libs.openai.client)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.auth)
}