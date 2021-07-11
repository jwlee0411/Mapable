import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins{
    id ("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlin-android")

}

android {
    compileSdkVersion(30)
    defaultConfig {
        minSdkVersion (28)
        targetSdkVersion (30)
        versionCode = 1
        versionName  = "1.7"
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

    buildFeatures{
        viewBinding = true
    }
}



dependencies {
    androidTestImplementation("com.android.support.test.espresso:espresso-core:2.2.2"){
        exclude (group = "com.android.support", module = "support-annotations")
    }
    testImplementation ("junit:junit:4.13.2")
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation (project(path = ":app"))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

    //androidx
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("androidx.navigation:navigation-runtime-ktx:2.3.5")
    implementation ("androidx.navigation:navigation-fragment-ktx:2.3.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.3.5")
    implementation ("androidx.core:core-ktx:1.6.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //android_support
    implementation ("com.android.support.constraint:constraint-layout:2.0.4")
    implementation ("com.android.support:support-annotations:28.0.0")

    //firebase
    implementation (platform ("com.google.firebase:firebase-bom:27.1.0"))
    implementation ("com.google.firebase:firebase-auth:21.0.1")
    implementation ("com.google.firebase:firebase-auth-ktx:21.0.1")
    implementation ("com.google.firebase:firebase-firestore:23.0.1")
    implementation ("com.google.firebase:firebase-firestore-ktx:23.0.1")
    implementation ("com.google.firebase:firebase-storage")

    //android
    implementation ("com.google.android.gms:play-services-maps:17.0.1")
    implementation ("com.google.android.gms:play-services-places:17.0.0")
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("com.google.android.libraries.places:places:2.4.0")

    implementation ("com.google.android.gms:play-services-location:18.0.0")
    implementation ("com.google.android.gms:play-services-auth:19.0.0")
    implementation ("com.google.code.findbugs:jsr305:3.0.2")

    //open-source
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    implementation ("org.jsoup:jsoup:1.13.1")
    implementation ("com.scwang.wave:MultiWaveHeader:1.0.0")
    implementation ("com.airbnb.android:lottie:3.7.0")


    //사용 안함(제거하면 에러뜸)


}

repositories {
    mavenCentral()

}
