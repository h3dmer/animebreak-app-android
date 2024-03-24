plugins {
  alias(libs.plugins.anibreak.android.library)
  alias(libs.plugins.anibreak.android.hilt)
  alias(libs.plugins.anibreak.android.room)
  id("kotlinx-serialization")
}

android {
  namespace = "com.hedmer.anibreak.core.database"
}

dependencies {

  implementation(project(":core:model"))

  implementation(libs.kotlinx.datetime)
  implementation(libs.kotlinx.serialization.json)
}