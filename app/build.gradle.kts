plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.kunal.onehealth"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.kunal.onehealth"
        minSdk = 25
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.7.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
   // implementation(libs.room.common.jvm)
    //implementation(libs.room.runtime.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Room components
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")


    // ✅ Lifecycle (ViewModel + LiveData)
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.8.4")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.8.4")

    // ✅ UI
    implementation ("androidx.appcompat:appcompat:1.7.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

// ✅ Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // ✅ Navigation Components
    implementation("androidx.navigation:navigation-fragment:2.8.2")
    implementation("androidx.navigation:navigation-ui:2.8.2")

    // ✅ Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.room:room-runtime:2.7.0-alpha01")
    annotationProcessor("androidx.room:room-compiler:2.7.0-alpha01")



// (Optional) Room Kotlin Extensions and Coroutines support
// implementation "androidx.room:room-ktx:2.6.1"


}