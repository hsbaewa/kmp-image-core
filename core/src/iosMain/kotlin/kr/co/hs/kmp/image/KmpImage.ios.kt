package kr.co.hs.kmp.image

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Data
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageGetBytesPerRow
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.posix.memcpy

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

fun UIImage.toByteArray(
    format: KmpImage.Format = KmpImage.Format.JPEG,
    quality: Int = 100
): ByteArray? = asImageBitmap()
    ?.toByteArray(format, quality)

fun ImageBitmap.asUIImage(
    format: KmpImage.Format = KmpImage.Format.JPEG,
    quality: Int = 100
): UIImage? = toByteArray(format, quality)
    ?.toNSData()
    ?.toUIImage()

@OptIn(ExperimentalForeignApi::class) // Required for accessing CoreGraphics functions
fun UIImage.asImageBitmap(): ImageBitmap? {
    val imageRef = this.CGImage ?: return null

    val width = CGImageGetWidth(imageRef).toInt()
    val height = CGImageGetHeight(imageRef).toInt()
    val bytesPerRow = CGImageGetBytesPerRow(imageRef)
    val data = CGDataProviderCopyData(CGImageGetDataProvider(imageRef))
    val bytePointer = CFDataGetBytePtr(data)
    val length = CFDataGetLength(data)

    val byteArray = ByteArray(length.toInt())
    byteArray.usePinned { pinned ->
        memcpy(pinned.addressOf(0), bytePointer, length.toULong())
    }

    // Create a Skia Image from the raw pixel data
    val skiaImage = Image.makeRaster(
        imageInfo = ImageInfo(
            width,
            height,
            ColorType.BGRA_8888, // Adjust ColorType based on your UIImage's format
            ColorAlphaType.PREMUL,
            ColorSpace.sRGB
        ),
        data = Data.makeFromBytes(byteArray),
        rowBytes = bytesPerRow.toInt()
    )

    // Convert Skia Image to Compose ImageBitmap
    return skiaImage.toComposeImageBitmap()
}

@OptIn(ExperimentalForeignApi::class)
fun ByteArray.toNSData(): NSData = usePinned { pinned ->
    NSData.dataWithBytes(
        bytes = pinned.addressOf(0),
        length = size.toULong()
    )
}

fun NSData.toUIImage(): UIImage? = UIImage.imageWithData(this)