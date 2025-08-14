package kr.co.hs.kmp.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.ImageBitmapConfig
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.roundToInt

object KmpImage {
    enum class Format { PNG, JPEG, WEBP }
}

expect fun ImageBitmap.toByteArray(
    format: KmpImage.Format = KmpImage.Format.JPEG,
    quality: Int = 100
): ByteArray?

@Composable
fun painterResourceToImageBitmap(
    resource: DrawableResource,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    config: ImageBitmapConfig = ImageBitmapConfig.Argb8888,
): ImageBitmap = painterResource(resource = resource)
    .run {
        toImageBitmap(
            layoutDirection = layoutDirection,
            size = intrinsicSize,
            config = config
        )
    }

@Composable
fun Painter.toImageBitmap(
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    size: Size = intrinsicSize,
    config: ImageBitmapConfig = ImageBitmapConfig.Argb8888,
): ImageBitmap = with(LocalDensity.current) {
    toImageBitmap(
        density = this,
        layoutDirection = layoutDirection,
        size = size,
        config = config
    )
}

fun Painter.toImageBitmap(
    density: Density,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    size: Size = intrinsicSize,
    config: ImageBitmapConfig = ImageBitmapConfig.Argb8888,
): ImageBitmap {
    val image = ImageBitmap(
        width = size.width.roundToInt(),
        height = size.height.roundToInt(),
        config = config
    )
    val canvas = Canvas(image)
    CanvasDrawScope().draw(
        density = density,
        layoutDirection = layoutDirection,
        canvas = canvas,
        size = size
    ) {
        draw(size = this.size)
    }
    return image
}

expect fun ImageBitmap.crop(
    rect: Rect
): ImageBitmap

expect fun ImageBitmap.scale(
    size: Size,
    format: KmpImage.Format,
    quality: Int = 100
): ImageBitmap

expect fun ImageBitmap.mask(
    mask: ImageBitmap,
    format: KmpImage.Format,
    quality: Int = 100
): ImageBitmap

expect fun ImageBitmap.draw(
    image: ImageBitmap,
    rect: Rect,
    format: KmpImage.Format,
    quality: Int = 100
): ImageBitmap