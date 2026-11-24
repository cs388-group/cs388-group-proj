import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.legisly"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.legisly"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        val legiscanKey = (localProperties.getProperty("LEGISCAN_API_KEY") ?: localProperties.getProperty("NY_API_KEY") ?: "").trim().removeSurrounding("\"")
        val nyKey = (localProperties.getProperty("NY_API_KEY") ?: "").trim().removeSurrounding("\"")

        buildConfigField(
            "String",
            "NY_API_KEY",
            "\"$nyKey\""
        )
        buildConfigField(
            "String",
            "LEGISCAN_API_KEY",
            "\"$legiscanKey\""
        )
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.fragment:fragment-ktx:1.8.9")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.5")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}
