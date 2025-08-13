package kr.co.hs.kmp.image.sample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform