plugins {
  id("anibreak.android.library")
  id("anibreak.android.library.compose")
  id("anibreak.android.hilt")
}

android {
  namespace = "com.hedmer.anibreak.core.common.ui"
}

dependencies {
  api(libs.androidx.compose.runtime)
  api(libs.androidx.compose.ui)
  api(libs.androidx.compose.ui.tooling)
  api(libs.androidx.compose.ui.tooling.preview)
  api(libs.androidx.compose.material)
  api(libs.androidx.compose.material3)
  api(libs.androidx.compose.foundation)
  api(libs.androidx.compose.foundation.layout)
  api(libs.androidx.compose.constraintlayout)
  implementation(libs.lottie.compose)
}
