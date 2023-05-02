# Android Architecture

This is an Android starter project with MVVM architecture which you can use to bootstrap your application.
## Config

| SDK               | Version |
| ----------------- |---------|
| Min Sdk           | 23      |
| Compile Sdk       | 33      |
| Target Sdk        | 33      |

#### Kotlin & Gradle Versions

**1.** Gradle distributionUrl inside **gradle-wrapper.properties**
```
distributionUrl=https\://services.gradle.org/distributions/gradle-7.0.2-bin.zip
```

**2.** Android tools & kotlin version inside project level **build.gradle**
```
dependencies {
    classpath "com.android.tools.build:gradle:7.0.4"
    classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10"
}
```

## SDKs

**1. Dagger 2**
```
implementation 'com.google.dagger:dagger-android-support:2.41'
kapt 'com.google.dagger:dagger-android-processor:2.41'
kapt 'com.google.dagger:dagger-compiler:2.41'
```

**2. Retrofit & OkHttp**
```
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.9.3'
```