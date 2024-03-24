plugins {
  id("anibreak.android.library")
  id("kotlinx-serialization")
}

android {
  namespace = "com.hedmer.anibreak.core.model"
}

dependencies {
  implementation(libs.kotlinx.serialization.json)
}
