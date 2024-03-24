plugins {
  id("anibreak.android.library")
  id("anibreak.android.hilt")
  id("anibreak.spotless")
}

android {
  namespace = "com.hedmer.anibreak.core.data"

}

dependencies {
  api(projects.core.network)
  api(projects.core.common)
  api(projects.core.domain)
  implementation(projects.core.common)
  implementation(projects.core.database)

  implementation(libs.timber.android)
  implementation(libs.androidx.paging)
  implementation(libs.kotlinx.datetime)

  testImplementation(libs.androidx.test.rules)
  testImplementation(libs.androidx.test.core)
  testImplementation(libs.hilt.android.testing)
  testImplementation(libs.kotlinx.coroutines.test)
  testImplementation(libs.truth)
  testImplementation(libs.mockk.test)
  testImplementation("app.cash.turbine:turbine:1.1.0")
}