package kr.co.hs.kmp.image

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image

actual fun ImageBitmap.toByteArray(
    format: KmpImage.Format,
    quality: Int
): ByteArray? = Image
    .makeFromBitmap(asSkiaBitmap())
    .encodeToData(
        format = when (format) {
            KmpImage.Format.PNG -> EncodedImageFormat.PNG
            KmpImage.Format.JPEG -> EncodedImageFormat.JPEG
            KmpImage.Format.WEBP -> EncodedImageFormat.WEBP
        },
        quality = quality
    )
    ?.bytes