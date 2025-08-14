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
import androidx.compose.ui.geometry.Size
import org.jetbrains.compose.ui.tooling.preview.Preview

import kmpimagecore.composeapp.generated.resources.Res
import kmpimagecore.composeapp.generated.resources.compose_multiplatform
import kr.co.hs.kmp.image.KmpImage
import kr.co.hs.kmp.image.painterResourceToImageBitmap
import kr.co.hs.kmp.image.scale

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
                    Text("Compose: $greeting")
                }
            }
        }
    }
}