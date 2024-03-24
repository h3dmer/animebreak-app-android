plugins {
  id("anibreak.android.library")
  id("anibreak.android.hilt")
  id("com.apollographql.apollo3")
}

android {
  namespace = "com.hedmer.anibreak.core.network"
}

apollo {

  useVersion2Compat()
  schemaFile.set(file("src/main/graphql/schema.graphqls"))
}

dependencies {

  api(projects.core.common)

  implementation(libs.androidx.startup)
  implementation("com.localebro:okhttpprofiler:1.0.8")

  api(libs.apollo.api)
  api(libs.apollo.runtime)
  api(libs.okhttp.logging)
//  api(libs.retrofit.core)
}
