plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.marck.hordaaovivo"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.marck.hordaaovivo"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    // Assinatura fixa: as próximas versões instalam por cima
    signingConfigs {
        create("fixa") {
            storeFile = file("horda.keystore")
            storePassword = "horda12345"
            keyAlias = "horda"
            keyPassword = "horda12345"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("fixa")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
