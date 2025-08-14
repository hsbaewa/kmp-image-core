package kr.co.hs.kmp.image.sample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmpimagecore.composeapp.generated.resources.Res
import kmpimagecore.composeapp.generated.resources.compose_multiplatform
import kmpimagecore.composeapp.generated.resources.frame_mainprofile_mobile_defaultarea_full
import kmpimagecore.composeapp.generated.resources.frame_mainprofile_pc
import kmpimagecore.composeapp.generated.resources.frame_mainprofile_pc_photoarea
import kr.co.hs.kmp.image.KmpImage
import kr.co.hs.kmp.image.draw
import kr.co.hs.kmp.image.mask
import kr.co.hs.kmp.image.painterResourceToImageBitmap
import kr.co.hs.kmp.image.scale
import org.jetbrains.compose.resources.imageResource

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(
                        bitmap = painterResourceToImageBitmap(Res.drawable.compose_multiplatform)
                            .scale(
                                size = Size(200f, 200f),
                                format = KmpImage.Format.PNG,
                                quality = 50
                            ),
                        null
                    )

                    val maskImage = imageResource(Res.drawable.frame_mainprofile_pc_photoarea)
                    val profileImage =
                        painterResourceToImageBitmap(Res.drawable.frame_mainprofile_mobile_defaultarea_full)
                            .mask(maskImage, format = KmpImage.Format.PNG)
                    val frameImage = imageResource(Res.drawable.frame_mainprofile_pc)
                    val rect = Rect(
                        offset = Offset(0f, 0f),
                        size = Size(
                            frameImage.width.toFloat(),
                            frameImage.height.toFloat()
                        )
                    )
                    val framedImage = profileImage.draw(
                        frameImage,
                        rect,
                        KmpImage.Format.PNG
                    )

                    Image(
                        bitmap = framedImage,
                        ""
                    )

                    Text("Compose: $greeting")
                }
            }
        }
    }
}