plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
}

android {
    namespace = "com.chavan.automessagereplier"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.chavan.automessagereplier"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("io.coil-kt:coil-compose:2.1.0")

    implementation("com.airbnb.android:lottie-compose:6.0.1")

    // Compose dependencies
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.compose.material:material-icons-extended:1.5.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("androidx.compose.runtime:runtime-livedata")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("androidx.hilt:hilt-compiler:1.1.0-beta01")
    kapt("com.google.dagger:hilt-android-compiler:2.47")

    //Room
    implementation("androidx.room:room-ktx:2.5.2")
    implementation("androidx.room:room-runtime:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")

    // Kotlin extensions and Room coroutine support
    implementation("androidx.room:room-ktx:2.5.2")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    //GSON
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    // Compose Nav Destinations
    implementation("io.github.raamcosta.compose-destinations:core:1.1.2-beta")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.1.2-beta")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
}