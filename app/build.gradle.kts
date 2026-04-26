plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
}

// Load local.properties for secrets
val localProperties = java.util.Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

android {
    namespace = "com.bousmah.realmadridstore_zayd"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.bousmah.realmadridstore_zayd"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Build config fields - read from local.properties or use defaults
        buildConfigField("String", "OLLAMA_BASE_URL", "\"${localProperties.getProperty("OLLAMA_BASE_URL", "http://10.0.2.2:11434/")}\"")
        buildConfigField("String", "BUILD_TYPE", "\"${project.findProperty("android.buildTypes.release.name") ?: "debug"}\"")
    }

    buildTypes {
        debug {
            isDebuggable = true
            buildConfigField("String", "OLLAMA_BASE_URL", "\"http://10.0.2.2:11434/\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Release MUST use HTTPS
            buildConfigField("String", "OLLAMA_BASE_URL", "\"${localProperties.getProperty("OLLAMA_BASE_URL") ?: "https://localhost:11434/"}\"")
            
            // Enable R8 full mode
            (this as com.android.build.api.dsl.ApplicationBuildType).isCrunchPngs = true
        }
    }
    
    buildFeatures {
        buildConfig = true
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.maps.compose)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.coil.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.play.services.location)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}