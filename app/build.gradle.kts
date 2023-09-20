import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    
    id("com.google.gms.google-services")

    id("kotlin-kapt")
}

android {
    namespace = "ru.claus42.anothertodolistapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.claus42.anothertodolistapp"
        minSdk = 26
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
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(Dependencies.constraintLayout)

    implementation(Dependencies.Navigation.fragmentKtx)
    implementation(Dependencies.Navigation.uiKtx)
    androidTestImplementation(Dependencies.Navigation.testitng)

    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.auth)
    implementation(Dependencies.Firebase.firestore)

    implementation(Dependencies.Dagger.dagger)
    kapt(Dependencies.Dagger.compiler)

    implementation(Dependencies.Room.room)
    testImplementation(Dependencies.Room.testing)
    implementation(Dependencies.Room.paging)
    kapt(Dependencies.Room.compiler)

    implementation(Dependencies.material)
}