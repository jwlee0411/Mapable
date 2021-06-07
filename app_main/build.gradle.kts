import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins{
    id ("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("android.extensions")

}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion (28)
        targetSdkVersion (30)
        versionCode = 1
        versionName  = "1.4"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation (project(path = ":app"))
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation (platform ("com.google.firebase:firebase-bom:27.1.0"))
    implementation ("com.google.firebase:firebase-database:20.0.0")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")

    androidTestImplementation("com.android.support.test.espresso:espresso-core:2.2.2"){
        exclude (group = "com.android.support", module = "support-annotations")
    }

    implementation ("org.jsoup:jsoup:1.13.1")
    implementation ("com.google.android.gms:play-services-maps:17.0.1")
    implementation ("com.google.android.material:material:1.4.0-rc01")
    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation ("com.android.support.constraint:constraint-layout:2.0.4")
    implementation ("com.android.support:support-annotations:28.0.0")
    implementation ("com.google.code.findbugs:jsr305:3.0.2")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("com.appyvet:materialrangebar:1.4.8")
    implementation ("com.github.PhilJay:MPAndroidChart:3.1.0")
    implementation ("com.scwang.wave:MultiWaveHeader:1.0.0")
    implementation ("com.airbnb.android:lottie:3.7.0")
    implementation ("com.github.vikramezhil:DroidSpeech:v2.0.3")
    testImplementation ("junit:junit:4.13.2")
    implementation ("androidx.core:core-ktx:1.5.0")
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

}

repositories {
    mavenCentral()

}
