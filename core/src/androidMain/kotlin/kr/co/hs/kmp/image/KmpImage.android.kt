package kr.co.hs.kmp.image

import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
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