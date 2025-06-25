// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.spotless) apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            ktlint("1.0.1").editorConfigOverride(
                mapOf(
                    "ktlint_standard_package-name" to "disabled",
                    "ktlint_standard_parameter-list-wrapping" to "disabled",
                    "ktlint_standard_multiline-if-else" to "disabled",
                    "ktlint_standard_discouraged-comment-location" to "disabled",
                )
            )
        }
    }
}