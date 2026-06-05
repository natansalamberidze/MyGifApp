# Gif App

A native Android application written in Kotlin using Jetpack Compose to search and browse GIF animations using the Giphy API.

## Tech Stack

* **Language:** Kotlin
* **Architecture:** MVVM (Domain, Data, and UI layers split)
* **UI Framework:** Jetpack Compose (Material 3)
* **Dependency Injection:** Dagger Hilt
* **Networking:** Retrofit 2 + Gson
* **Asynchronous Programming:** Kotlin Coroutines & Flow
* **Pagination:** Jetpack Paging 3
* **Image Loading:** Glide Compose
* **Testing:** JUnit 4 + MockK + Turbine

---

## Getting Started

To build and run the project, you will need **Android Studio** and **Java 17**

### Step 1. Clone the Repository
Open your terminal and run the following command:

  ```bash
  git clone https://github.com/natansalamberidze/MyGifApp.git
  ```

### Step 2. Configure the API Key
The project uses an external API, and the access token is hidden for security reasons. To enable data loading in the app:
1. Create a file named `local.properties` in the root directory of the project (if it does not exist yet).
2. Add your personal API key in the following format (make sure to include the double quotes):

  ```text
  API_KEY="YOUR_ACTUAL_API_KEY_HERE"
  ```

*The build system will automatically inject this key into the app's source code via `buildConfigField` during compilation.*

### Step 3. Open and Run
1. Open Android Studio and select **File -> Open...**, then choose the project root folder.
2. Wait for the Gradle synchronization to complete successfully (`BUILD SUCCESSFUL`).
3. Connect an Android device (with USB Debugging enabled) or launch an emulator. 
   OR Wi-Fi Connection (Android 11+):
     * Enable **Wireless Debugging** in Developer Options.
     * Connect your computer and device to the same Wi-Fi network.
4. Click the **Run** button (green triangle) on the top toolbar.
