@file:Suppress("UnstableApiUsage")

pluginManagement {
  includeBuild("build-logic")
  repositories {
    gradlePluginPortal()
    google()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    maven(url = "https://jitpack.io")
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    maven(url = "https://jitpack.io")
  }
}
rootProject.name = "anibreak"
include(":app")
include(":core:common")
include(":core:common-ui")
include(":core:designsystem")
include(":core:navigation")
include(":core:network")
include(":core:data")
include(":core:database")
include(":core:domain")
include(":core:model")
include(":core:testing")
include(":feature:home")
include(":feature:animedetails")
include(":sync")
