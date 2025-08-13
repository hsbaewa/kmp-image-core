package kr.co.hs.kmp.image

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

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

actual fun ImageBitmap.crop(
    rect: Rect
): ImageBitmap = createBitmap(
    rect.width.roundToInt(),
    rect.height.roundToInt(),
    Bitmap.Config.ARGB_8888
)
    .apply {
        Canvas(this).drawBitmap(
            asAndroidBitmap(),
            rect.left,
            rect.top,
            null
        )
    }
    .asImageBitmap()