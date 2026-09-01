import java.util.Properties
val isIzzyOrFdroid = false


plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.compose.compiler)

    alias(libs.plugins.baselineprofile)
}


android {
    namespace = "com.rk.taskmanager.app"
    compileSdk = 37

    lint {
        disable += "MissingTranslation"
    }

    dependenciesInfo {
        includeInApk = isIzzyOrFdroid.not()
        includeInBundle = isIzzyOrFdroid.not()
    }

    signingConfigs {
        create("release") {
            // Test signing configuration for open source version
            storeFile = file("test.keystore")
            storePassword = "123456"
            keyAlias = "test"
            keyPassword = "123456"
        }
    }


    buildTypes {
        release{
            isMinifyEnabled = isIzzyOrFdroid.not()
            isCrunchPngs = isIzzyOrFdroid.not()
            isShrinkResources = isIzzyOrFdroid.not()

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug{
            versionNameSuffix = "-DEBUG"
        }
    }

    defaultConfig {
        applicationId = "com.rk.taskmanager"
        minSdk = 26
        targetSdk = 37

        //versioning
        versionCode = 53
        versionName = "1.5.3"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
        // isCoreLibraryDesugaringEnabled = true
    }
    packaging {
        jniLibs {
            useLegacyPackaging = true
        }
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

}

tasks.whenTaskAdded {
    if (isIzzyOrFdroid && name.contains("ArtProfile")) {
        println("Skipped Task $name")
        enabled = false
    }
}

dependencies {

    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))

    implementation(libs.androidx.room.ktx)

    implementation(project(":main"))
}
