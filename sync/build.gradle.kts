plugins {
  alias(libs.plugins.anibreak.android.library)
  alias(libs.plugins.anibreak.android.hilt)
}

android {
  namespace = "com.hedmer.anibreak.sync"

}

dependencies {
  ksp(libs.hilt.ext.compiler)
  implementation(libs.androidx.worker)
  implementation(libs.hilt.worker)
  implementation(libs.timber.android)

  implementation(projects.core.domain)
  implementation(projects.core.common)
}