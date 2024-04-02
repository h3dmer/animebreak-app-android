import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id("anibreak.android.library")
  id("anibreak.android.library.compose")
  id("anibreak.android.feature")
  id("anibreak.android.hilt")
}

android {
  namespace = "com.hedmer.anibreak.feature.home"
}


dependencies {

  implementation(projects.core.data)
  implementation(projects.core.common)
  implementation(projects.core.commonUi)
  implementation(projects.core.domain)
  implementation(projects.core.model)
  implementation(projects.core.navigation)

  implementation(libs.androidx.lifecycle.runtimeCompose)
  implementation(libs.androidx.lifecycle.viewModelCompose)
  implementation(libs.androidx.startup)
  implementation(libs.timber.android)
  implementation(libs.coil.kt)
  implementation(libs.coil.kt.compose)
  implementation(libs.androidx.compose.ui.util)
  implementation(libs.kotlinx.collections.immutable)

  implementation(libs.androidx.paging)
  implementation(libs.androidx.paging.compose)
  implementation(libs.lottie.compose)

  implementation(projects.core.testing)
  testImplementation(libs.androidx.test.rules)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.hilt.android.testing)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.truth)
  testImplementation(libs.mockk.test)
  testImplementation("app.cash.turbine:turbine:1.1.0")

}