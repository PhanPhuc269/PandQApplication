# PandQ Application - Project Guide

## 1. Project Overview
This project is an Android application built using **Jetpack Compose** and following **Clean Architecture** principles with **MVVM** pattern. It serves as a University Project demonstration.

## 2. Tech Stack
- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture
- **Dependency Injection**: Hilt
- **Network**: Retrofit + OkHttp + Gson
- **Async**: Coroutines + Flow
- **Image Loading**: Coil
- **Authentication**: Firebase Auth

## 3. Project Structure
The code is organized by **layer** in `com.group1.pandqapplication`:

### **`data` Layer**
Handles data operations, network calls, and repository implementations.
- `remote/`: API interfaces (e.g., `ApiService`).
- `repository/`: Repository interfaces and implementations (e.g., `AuthRepositoryImpl`, `MyRepositoryImpl`).

### **`di` Layer**
Dependency Injection modules.
- `AppModule.kt`: Global dependencies.
- `NetworkModule.kt`: Retrofit, OkHttp, ApiService providers.
- `RepositoryModule.kt`: Binds Repository interfaces to implementations.
- `AuthModule.kt`: Provides FirebaseAuth.

### **`ui` Layer**
Contains UI components and ViewModels.
- `login/`: Login feature (Screen, ViewModel, State).
- `home/`: Home feature (Screen).
- `navigation/`: Navigation graph (`PandQNavGraph`) and Routes (`Screen`).
- `theme/`: Theme definitions.

### **`util` Layer**
Helper classes and constants.
- `Result.kt`: Generic sealed class for Success/Error/Loading states.
- `Constants.kt`: App-wide constants.

## 4. Setup Instructions
### Prerequisites
- Android Studio Ladybug or newer.
- JDK 11 or 17.

### Firebase Configuration (CRITICAL)
This project uses Firebase Authentication. You **must** configure it manually:
1. Go to [Firebase Console](https://console.firebase.google.com/).
2. Create a new project or select an existing one.
3. Add an Android app with package name: `com.group1.pandqapplication`.
4. Download the `google-services.json` file.
5. Place the file in the **`app/`** directory of this project (`PandQApplication/app/google-services.json`).
6. Enable **Email/Password** provider in Firebase Authentication settings.

## 5. Build and Run
1. Open the project in Android Studio.
2. Sync Project with Gradle Files.
3. Select the `app` configuration.
4. Run on an Emulator or Physical Device.

## 6. Development Guide

### How to Add a New Screen
1.  **Create UI Components**:
    *   Create a new package in `ui/` (e.g., `ui/profile`).
    *   Create a ViewModel (e.g., `ProfileViewModel`) annotated with `@HiltViewModel`.
    *   Create a Composable (e.g., `ProfileScreen`).
2.  **Define Route**:
    *   Open `ui/navigation/Screen.kt`.
    *   Add a new object: `data object Profile : Screen("profile")`.
3.  **Update Navigation**:
    *   Open `ui/navigation/PandQNavGraph.kt`.
    *   Add a `composable(Screen.Profile.route) { ProfileScreen(...) }` block inside the `NavHost`.

### How to Add a New API Call
1.  **Define Endpoint**:
    *   Open `data/remote/ApiService.kt`.
    *   Add a new suspend function with Retrofit annotations (e.g., `@GET("products") suspend fun getProducts(): List<ProductDto>`).
2.  **Update Repository**:
    *   Add the function signature to your Repository interface (e.g., `MyRepository`).
    *   Implement the function in the implementation class (e.g., `MyRepositoryImpl`). Use `flow { ... }` to emit `Result.Loading`, `Result.Success`, or `Result.Error`.
3.  **Call from ViewModel**:
    *   Inject the Repository into your ViewModel.
    *   Launch a coroutine (`viewModelScope.launch`) and collect the flow to update your UI State.

## 7. Key Features
- **Login**: Users can sign in using Email/Password via Firebase.
- **Home**: A welcome screen accessible after login.
- **Navigation**: Seamless transition between screens using Jetpack Navigation Compose.
- **Error Handling**: Network and Auth errors are handled and displayed via Snackbars.
