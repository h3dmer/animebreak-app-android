plugins {
  id("anibreak.android.library")
  id("anibreak.android.library.compose")
  id("anibreak.android.hilt")
}

android {
  namespace = "com.hedmer.anibreak.core.navigation"
}

dependencies {

  // compose
  implementation(libs.androidx.compose.ui.tooling)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.compose.runtime)
  implementation(libs.androidx.navigation.compose)

  // jetpack
  implementation(libs.androidx.hilt.navigation.compose)
  implementation(libs.hilt.android)
}