// Top-level build file where you can add configuration options common to all sub-projects/modules.
//
//plugins {
//    id("com.android.application") version "8.11.1" apply false
//    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
//    id("com.google.dagger.hilt.android") version "2.46" apply false
//}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
}