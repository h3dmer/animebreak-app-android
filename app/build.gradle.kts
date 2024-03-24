@file:Suppress("UnstableApiUsage")

plugins {
  id("anibreak.android.application")
  id("anibreak.android.application.compose")
  id("anibreak.android.hilt")
  id("kotlin-parcelize")
  id("dagger.hilt.android.plugin")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.hedmer.anibreak"
  compileSdk = 34

  defaultConfig {
    applicationId = "com.hedmer.anibreak"
    minSdk = 28
    targetSdk = 34
    versionCode = Configurations.versionCode
    versionName = Configurations.versionName
  }

  packaging {
    resources {
      excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
  }

  buildTypes {
    release {
      isShrinkResources = true
      isMinifyEnabled = true
    }
    debug {
      isMinifyEnabled = false
      isShrinkResources = false
      isDebuggable = true
    }
  }
}

dependencies {

  implementation(project(":core:designsystem"))
  implementation(project(":core:navigation"))
  implementation(project(":feature:home"))
  implementation(project(":feature:animedetails"))
  implementation(project(":sync"))

  // material
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.compose.material3)

  // compose
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.constraintlayout)

  // jetpack
  implementation(libs.androidx.startup)
  implementation(libs.hilt.android)
  implementation(libs.androidx.hilt.navigation.compose)
  ksp(libs.hilt.compiler)

  implementation(libs.timber.android)
}
