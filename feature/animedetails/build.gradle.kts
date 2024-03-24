plugins {
  id("anibreak.android.library")
  id("anibreak.android.library.compose")
  id("anibreak.android.feature")
  id("anibreak.android.hilt")
//  id("anibreak.spotless")
}

android {
  namespace = "com.hedmer.anibreak.feature.animedetails"
}


dependencies {

  api(projects.core.data)
  api(projects.core.domain)
  api(projects.core.navigation)
  api(projects.core.commonUi)

  implementation(libs.androidx.lifecycle.runtimeCompose)
  implementation(libs.androidx.lifecycle.viewModelCompose)
  implementation(libs.androidx.startup)
  implementation(libs.timber.android)
  implementation(libs.coil.kt)
  implementation(libs.coil.kt.compose)

  implementation(libs.androidx.worker)
//  implementation(libs.viewmodel.lifecycle)
  implementation(libs.hilt.worker)

}