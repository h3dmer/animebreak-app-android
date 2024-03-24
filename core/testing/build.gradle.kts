plugins {
  alias(libs.plugins.anibreak.android.library)
  alias(libs.plugins.anibreak.android.hilt)
}

android {
  namespace = "com.hedmer.anibreak.core.testing"
}

dependencies {

  implementation(libs.androidx.test.rules)
  implementation(libs.androidx.test.core)
  implementation(libs.hilt.android.testing)
  implementation(libs.kotlinx.coroutines.test)
  implementation(libs.truth)

}
