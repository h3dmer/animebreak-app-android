buildscript {
  repositories {
    google()
    mavenCentral()
    maven("https://plugins.gradle.org/m2/")
  }
}

plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.library) apply false
  alias(libs.plugins.apollo.graphql) apply false
  alias(libs.plugins.gms.googleServices) apply false
  alias(libs.plugins.hilt) apply false
  alias(libs.plugins.kotlin.jvm) apply false
  alias(libs.plugins.kotlinAndroid) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.room) apply false
  alias(libs.plugins.spotless) apply false
}
