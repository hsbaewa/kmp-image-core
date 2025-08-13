import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.androidLint)

    // Maven publish
    alias(libs.plugins.maven.publish)
    id("signing") // GPG 서명을 위한 플러그인 추가

    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {

    // Target declarations - add or remove as needed below. These define
    // which platforms this KMP module supports.
    // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
    androidLibrary {
        namespace = "kr.co.hs.kmp.image"
        compileSdk = 36
        minSdk = 24
    }

    // For iOS targets, this is also where you should
    // configure native binary output. For more information, see:
    // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

    // A step-by-step guide on how to include this library in an XCode
    // project can be found here:
    // https://developer.android.com/kotlin/multiplatform/migrate
    val xcfName = "coreKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    // Source set declarations.
    // Declaring a target automatically creates a source set with the same name. By default, the
    // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
    // common to share sources between related targets.
    // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
    sourceSets {
        commonMain {
            dependencies {
                // Add KMP dependencies here
                implementation(compose.ui)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                // Add Android-specific dependencies here. Note that this source set depends on
                // commonMain by default and will correctly pull the Android artifacts of any KMP
                // dependencies declared in commonMain.
            }
        }

        iosMain {
            dependencies {
                // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
                // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
                // part of KMP’s default source set hierarchy. Note that this source set depends
                // on common by default and will correctly pull the iOS artifacts of any
                // KMP dependencies declared in commonMain.
            }
        }
    }

}

val mavenCentralGroupId = "io.github.hsbaewa"
val mavenCentralArtifactId = "kmp-image-core"
val mavenCentralVersion = "0.0.2"

// Maven 그룹 및 버전 설정
group = mavenCentralGroupId
version = mavenCentralVersion

tasks.withType(Javadoc::class) {
    options {
        encoding = "UTF-8"
    }
}

//region Fix Gradle warning about signing tasks using publishing task outputs without explicit dependencies
// <https://youtrack.jetbrains.com/issue/KT-46466>
tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}
//endregion

signing {
    sign(publishing.publications)
    useGpgCmd() // 이거 있으면 signAllPublications() 필요 없음.
}

mavenPublishing {
//    signAllPublications() // Gpg 서명을 위한 설정
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL) // 포탈로 등록 할거기 때문에 타입 추가

    coordinates(
        mavenCentralGroupId,
        mavenCentralArtifactId,
        mavenCentralVersion
    ) // 네임 스페이스, 라이브러리 이름, 버전 순서로 작성

    // POM 설정
    pom {
        /**
        name = '[라이브러리 이름]'
        description = '[라이브러리 설명]'
        url = '[오픈소스 Repository Url]'
         */
        name = mavenCentralArtifactId
        description = "KMP Image Library"
        url = "https://github.com/hsbaewa/$mavenCentralArtifactId"
        inceptionYear = "2025"

        // 라이선스 정보
        licenses {
            license {
                name = "Apache License"
                url = "https://github.com/hsbaewa/$mavenCentralArtifactId/blob/main/LICENSE"
            }
        }

        // 개발자 정보
        developers {
            developer {
                id = "hsbaewa"
                name = "Development guy"
                email = "hsbaewa@gmail.com"
            }
            // 다른 개발자 정보 추가 가능...
        }

        /**
        connection = 'scm:git:github.com/[Github 사용자명]/[오픈소스 Repository 이름].git'
        developerConnection = 'scm:git:ssh://github.com/[Github 사용자명]/[오픈소스 Repository 이름].git'
        url = '<https://github.com/>[Github 사용자명]/[오픈소스 Repository 이름]/tree/[배포 브랜치명]'
         */
        scm {
            connection = "scm:git:github.com/hsbaewa/$mavenCentralArtifactId"
            developerConnection = "scm:git:ssh://github.com:hsbaewa/$mavenCentralArtifactId.git"
            url = "https://github.com/hsbaewa/$mavenCentralArtifactId/tree/main"
        }
    }
}
