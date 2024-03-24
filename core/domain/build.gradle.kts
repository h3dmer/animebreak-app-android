plugins {
  id("anibreak.android.library")
  id("anibreak.android.hilt")
}

android {
  namespace = "com.hedmer.anibreak.core.domain"
}

dependencies {

  api(project(":core:model"))
  implementation(project(":core:common"))

  implementation(libs.androidx.paging)

}
