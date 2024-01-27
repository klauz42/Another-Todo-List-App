<h1>Another Todo List App</h1>

<p>
  <a href="https://opensource.org/licenses/MIT"><img alt="License" src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
  <a href="https://www.android.com/"><img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"/></a>
  <a href="https://kotlinlang.org/"><img src="https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white"/></a>
</p>

<p >  
<b>Another Todo List App</b> demonstrates modern Android development practices on Kotlin, utilizing <b>Clean Architecture</b> and the <b>MVVM</b> pattern.
  <br>
  It includes frameworks and features such as: <b>Android Jetpack</b> (Navigation, ViewModel, Room, DataStore) <b>Dagger2</b>, Kotlin <b>Coroutines</b> and <b>Flow</b>, <b>LiveData</b>, <b>Firebase</b> (Authentication, Firestore),  along with <b>JUnit 4</b>, <b>Espresso</b> and <b>Mockito</b> for testing.
</p>

# Screenshots:
<h3>Light Theme</h3>
<p float="left">
  <img src="/screenshots/light_list.png" height="350px"/>
  <img src="/screenshots/light_detail.png" height="350px"/>
  <img src="/screenshots/light_search_grid.png" height="350px"/>
  <img src="/screenshots/light_search_linear.png" height="350px"/>
  <img src="/screenshots/light_account_info.png" height="350px"/>
</p>

<h3>Dark Theme</h3>
<p float="left">
  <img src="/screenshots/dark_list.png" height="350px"/>
  <img src="/screenshots/dark_detail.png" height="350px"/>
  <img src="/screenshots/dark_search_grid.png" height="350px"/>
  <img src="/screenshots/dark_search_linear.png" height="350px"/>
  <img src="/screenshots/dark_account_info.png" height="350px"/>
</p>

## Tech stack
- [Kotlin](https://kotlinlang.org/)
  - [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle): lifecycle-aware components, which can adjust their behavior based on the current lifecycle state of the activity or fragment
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): manages UI's data in a lifecycle-aware fashion. Allows data to survive configuration changes such as screen rotations or light/dark theme switching
  - [Material Design](https://m2.material.io/): provides design components that follow Google's Material Design guidelines such as [Bottom Navigation](https://m2.material.io/components/bottom-navigation) and [TextInputLayout](https://m2.material.io/components/text-fields/android)
  - [Databinding](https://developer.android.com/jetpack/androidx/releases/databinding): binds UI components in your layouts to data sources in a declarative style
  - [Coordinatorlayout](https://developer.android.com/jetpack/androidx/releases/coordinatorlayout): enables placement of top-level application widgets, such as AppBarLayout and FloatingActionButton, within your layout
  - [Recyclerview](https://developer.android.com/jetpack/androidx/releases/recyclerview): displays large sets of data in app's UI while minimizing memory usage, also allows you to use swipe and drag & drop actions by using [ItemTouchHelper](https://developer.android.com/reference/kotlin/androidx/recyclerview/widget/ItemTouchHelper)
  - [Room](https://developer.android.com/jetpack/androidx/releases/room): provides an abstraction layer over SQLite to allow fluent database access
  - [Preferences DataStore](https://developer.android.com/topic/libraries/architecture/datastore): a data storage solution that allows you to store key-value pairs
  - [Navigation Component](https://developer.android.com/jetpack/androidx/releases/navigation): provides support for fragment-based navigation, making a navigation in an app more clear
  - [Dagger](https://dagger.dev/): fully static, compile-time dependency injection framework
  - [Coroutines](https://github.com/Kotlin/kotlinx.coroutines): a library for performing asynchronous tasks, managing background tasks in a more efficient and simpler way than traditional methods like threads
  - [Firebase Firestore](https://firebase.google.com/docs/firestore): SDK with Kotlin extensions for real-time, cloud-hosted NoSQL database
  - [Firebase Authentication](https://firebase.google.com/docs/auth):  SDK with Kotlin extensions for implementing user authentication
  - [FirebaseUI for Auth](https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md): an open-source library that offers simple, customizable UI bindings on top of the core Firebase Auth SDK.
- Testing
  - [JUnit 4](https://junit.org/junit4/): a fundamental unit testing framework for Java applications
  - [AndroidX Test](https://developer.android.com/training/testing/instrumented-tests/androidx-test-libraries/rules): rules for Android testing
  - [Espresso](https://developer.android.com/training/testing/espresso): a framework for UI testing in Android
  - [Mockito](https://site.mockito.org/): a framework for mocking objects in tests, crucial for testing components in isolation
  - [Robolectric](https://robolectric.org/): a framework for unit testing Android code with a simulated Android environment
- Architecture
  - Clean Architecture (Presentation - Domain - Data)
  - MVVM Pattern (View - DataBinding - ViewModel - Model)
  - Repository Pattern

# Application Preview:

<p align="center">
  <img src="/gifs/coordinatorlayout_behavior.gif" width="300" />
  <br>
  <em>A collapsing toolbar layout with custom CoordinatorLayout.Behavior</em>
</p>
<br>
<p align="center">
  <img src="/gifs/itemtouchhelper_swipe.gif" width="300" />
  <br>
  <em>Swipe items aside to delete or change done status (custom ItemTouchHelper.Callback)</em>
</p>
<br>
<p align="center">
  <img src="/gifs/itemtouchhelper_drag.gif" width="300" />
  <br>
  <em>Drag and drop items to move them in the to-do list (custom ItemTouchHelper.Callback)</em>
</p>
<br>
<p align="center">
  <img src="/gifs/add_and_details.gif" width="300" />
  <br>
  <em>Adding a new to-do and editing details<br>(Jetpack Navigation Component with Safe Args Gradle plug-in)</em>
</p>
<br>
<p align="center">
  <img src="/gifs/add_and_details_no_save.gif" width="300" />
  <br>
  <em>Adding a new to-do but not saving (DialogFragment)</em>
</p>
<br>
<p align="center">
  <img src="/gifs/search_screen+layoutmanager.gif" width="300" />
  <br>
  <em>To-do search screen<br>(Bottom Navigation and dynamic layout manager switching)</em>
</p>
<br>
<p align="center">
  <img src="/gifs/sortoption+datastorepreferences.gif" width="300" />
  <br>
  <em>Search sorting options screen (Preferences DataStore)</em>
</p>
<br>
<p align="center">
  <img src="/gifs/filter_options+navigation.gif" width="300" />
  <br>
  <em>Search filter options screen (Preferences DataStore)</em>
</p>
<br>
<p align="center">
  <img src="/gifs/coroutine+livedata.gif" width="300" />
  <br>
  <em>Lifecycle-aware asynchronous data update<br>(Lifecycle, LiveData, Flow and coroutins)</em>
</p>
<br>
<p align="center">
  <img src="/gifs/sign_out+firabase_auth.gif" width="300" />
  <br>
  <em>Account info screen with option to sign out<br>(Firebase Authentication and FirebaseUI)</em>
</p>
