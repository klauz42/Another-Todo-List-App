plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    
    id("com.google.gms.google-services")
    id("androidx.navigation.safeargs.kotlin")

    id("kotlin-kapt")

    id("androidx.room")
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

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "ru.claus42.anothertodolistapp.NoAuthTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
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
    room {
        schemaDirectory("$projectDir/schemas")
    }
    sourceSets {
        getByName("androidTest") {
            manifest.srcFile("src/androidTest/AndroidManifest.xml")
        }
    }
}

dependencies {

    // explicit determine version to prevent FirebaseUI Auth PendingIntent exception
    // throwing when Target SDK is 34+
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.datastore:datastore-preferences-core:1.0.0")

    implementation(Dependencies.Android.core)

    testImplementation(Dependencies.Testing.junit)
    androidTestImplementation(Dependencies.Testing.AndroidXTest.junit)
    androidTestImplementation(Dependencies.Testing.AndroidXTest.junitKtx)
    androidTestImplementation(Dependencies.Testing.AndroidXTest.rules)
    androidTestImplementation(Dependencies.Testing.espressoCore)
    androidTestImplementation(Dependencies.Testing.espressoContrib)
    androidTestImplementation(Dependencies.Testing.idlingResource)
    androidTestImplementation(Dependencies.Navigation.testitng)
    debugImplementation(Dependencies.Testing.fragment)
    testImplementation(Dependencies.Testing.coroutinesTest)
    testImplementation(Dependencies.Testing.Mockito.core)
    testImplementation(Dependencies.Testing.Mockito.kotlin)
    androidTestImplementation(Dependencies.Testing.Mockito.core)
    androidTestImplementation(Dependencies.Testing.Mockito.kotlin)
    androidTestImplementation(Dependencies.Testing.Mockito.android)
    testImplementation(Dependencies.Testing.robolectric)

    implementation(Dependencies.Lifecycle.runtime)
    implementation(Dependencies.Lifecycle.livedata)

    implementation(Dependencies.constraintLayout)

    implementation(Dependencies.Navigation.fragmentKtx)
    implementation(Dependencies.Navigation.uiKtx)
    androidTestImplementation(Dependencies.Navigation.testitng)

    implementation(platform(Dependencies.Firebase.bom))
    implementation(Dependencies.Firebase.auth)
    implementation(Dependencies.Firebase.firestore)
    implementation(Dependencies.Firebase.UI.auth)
    implementation(Dependencies.Firebase.UI.firestore)

    implementation(Dependencies.Dagger.dagger)
    kapt(Dependencies.Dagger.compiler)
    androidTestImplementation(Dependencies.Dagger.dagger)
    kaptAndroidTest(Dependencies.Dagger.compiler)

    implementation(Dependencies.Room.room)
    testImplementation(Dependencies.Room.testing)
    implementation(Dependencies.Room.paging)
    kapt(Dependencies.Room.compiler)

    implementation(Dependencies.material)
}