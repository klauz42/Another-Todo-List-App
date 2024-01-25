object Versions {
    const val androidCore       = "1.12.0"
    const val dagger            = "2.48.1"
    const val navigation        = "2.7.6"
    const val room              = "2.6.1"
    const val material          = "1.11.0"
    const val firebase          = "32.7.0"
    const val firebase_ui       = "8.0.2"
    const val constraintLayout  = "2.1.4"
    const val lifecycle         = "2.6.2"
    const val junit             = "4.13.2"
    const val androidXTest      = "1.1.5"
    const val espresso          = "3.5.1"
    const val fragmentTesting   = "1.6.2"
    const val kotlinx           = "1.7.3"
    const val mockito           = "5.9.0"
    const val mockitoKotlin     = "3.2.0"
    const val robolectric       = "4.11.1"
}

object Dependencies {
    object Android {
        const val core = "androidx.core:core-ktx:${Versions.androidCore}"
    }

    object Navigation {
        const val fragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
        const val uiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
        const val testitng = "androidx.navigation:navigation-testing:${Versions.navigation}"
    }
    
    object Dagger {
        val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }
    
    object Room {
        const val room = "androidx.room:room-ktx:${Versions.room}"
        const val testing = "androidx.room:room-testing:${Versions.room}"
        const val paging = "androidx.room:room-paging:${Versions.room}"
        const val compiler = "androidx.room:room-compiler:${Versions.room}"
        const val coroutines = "androidx.room:room-coroutines:${Versions.room}"
    }


    const val material = "com.google.android.material:material:${Versions.material}"

    object Firebase {
        const val bom = "com.google.firebase:firebase-bom:${Versions.firebase}"
        const val auth = "com.google.firebase:firebase-auth-ktx"
        const val firestore = "com.google.firebase:firebase-firestore-ktx"
        const val storage = "com.google.firebase:firebase-storage-ktx"
        object UI {
            const val firestore = "com.firebaseui:firebase-ui-firestore:${Versions.firebase_ui}"
            const val auth = "com.firebaseui:firebase-ui-auth:${Versions.firebase_ui}"
        }

    }

    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    object Lifecycle {
        const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
        const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    }

    object Testing {
        const val junit = "junit:junit:${Versions.junit}"
        object AndroidXTest {
            const val junit = "androidx.test.ext:junit:${Versions.androidXTest}"
            const val junitKtx = "androidx.test.ext:junit-ktx:${Versions.androidXTest}"
        }
        const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso}"
        const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.espresso}"
        const val idlingResource = "androidx.test.espresso:espresso-idling-resource:${Versions.espresso}"
        const val fragment = "androidx.fragment:fragment-testing:${Versions.fragmentTesting}"
        const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinx}"
        object Mockito {
            const val core = "org.mockito:mockito-core:${Versions.mockito}"
            const val kotlin = "org.mockito.kotlin:mockito-kotlin:${Versions.mockitoKotlin}"
        }
        const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"
    }
}

object ClassPaths {

}