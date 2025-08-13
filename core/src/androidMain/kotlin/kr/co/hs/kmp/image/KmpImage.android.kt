package kr.co.hs.kmp.image

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream

actual fun ImageBitmap.toByteArray(
    format: KmpImage.Format,
    quality: Int
): ByteArray? {
    val stream = ByteArrayOutputStream()

    asAndroidBitmap()
        .compress(
            when (format) {
                KmpImage.Format.PNG -> Bitmap.CompressFormat.PNG
                KmpImage.Format.JPEG -> Bitmap.CompressFormat.JPEG
                KmpImage.Format.WEBP -> Bitmap.CompressFormat.WEBP
            },
            quality,
            stream
        )

    return stream.toByteArray()
}

fun Bitmap.toByteArray(
    format: KmpImage.Format = KmpImage.Format.JPEG,
    quality: Int = 100
): ByteArray? = asImageBitmap()
    .toByteArray(format, quality)