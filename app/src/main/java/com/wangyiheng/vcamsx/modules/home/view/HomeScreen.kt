import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.wangyiheng.vcamsx.components.LivePlayerDialog
import com.wangyiheng.vcamsx.components.SettingRow
import com.wangyiheng.vcamsx.components.VideoPlayerDialog
import com.wangyiheng.vcamsx.modules.home.controllers.HomeController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val homeController =  viewModel<HomeController>()
    LaunchedEffect(Unit){
        homeController.init()
    }

    val selectMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val mimeType = context.contentResolver.getType(it).orEmpty()
            val isSupported = mimeType.startsWith("video/") || mimeType.startsWith("image/")
            if (isSupported) {
                homeController.copyMediaToAppDir(context, it)
                Toast.makeText(context, "Đã chọn tệp phương tiện", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Chỉ hỗ trợ video hoặc ảnh", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted || Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                selectMediaLauncher.launch("*/*")
            } else {
                Toast.makeText(context, "Vui lòng cấp quyền đọc bộ nhớ trong phần Cài đặt", Toast.LENGTH_SHORT).show()
            }
        }
    )

    Card(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
        val buttonModifier = Modifier.fillMaxWidth()

        Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            TextField(
                value = homeController.liveURL.value,
                onValueChange = { homeController.liveURL.value = it },
                label = { Text("Liên kết RTMP:") }
            )

            Button(modifier = buttonModifier, onClick = { homeController.saveState() }) {
                Text("Lưu liên kết RTMP")
            }
            Button(
                modifier = buttonModifier,
                onClick = { requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE) }
            ) {
                Text("Chọn video / ảnh")
            }

            Button(modifier = buttonModifier, onClick = { homeController.isVideoDisplay.value = true }) {
                Text("Xem nguồn phát")
            }

            Button(modifier = buttonModifier, onClick = { homeController.isLiveStreamingDisplay.value = true }) {
                Text("Xem luồng trực tiếp")
            }

            SettingRow(
                label = "Bật nguồn phát",
                checkedState = homeController.isVideoEnabled,
                onCheckedChange = { homeController.saveState() },
                context = context
            )

            SettingRow(
                label = "Bật RTMP trực tiếp",
                checkedState = homeController.isLiveStreamingEnabled,
                onCheckedChange = { homeController.saveState() },
                context = context
            )

            SettingRow(
                label = "Bật âm thanh",
                checkedState = homeController.isVolumeEnabled,
                onCheckedChange = { homeController.saveState() },
                context = context
            )

            SettingRow(
                label = if (homeController.codecType.value) "Giải mã cứng" else "Giải mã mềm",
                checkedState = homeController.codecType,
                onCheckedChange = {
                    if(homeController.isH264HardwareDecoderSupport()){
                        homeController.saveState()
                    }else{
                        homeController.codecType.value = false
                        Toast.makeText(context, "Thiết bị không hỗ trợ giải mã cứng", Toast.LENGTH_SHORT).show()
                    }},
                context = context
            )
        }
        val releaseText = "Ứng dụng miễn phí, bấm để đến trang tải xuống"
        val annotatedString = AnnotatedString.Builder(releaseText).apply {
            addStringAnnotation(
                tag = "URL",
                annotation = "https://github.com/iiheng/VCAMSX/releases",
                start = 0,
                end = releaseText.length
            )
        }.toAnnotatedString()

        ClickableText(
            text = annotatedString,
            style = TextStyle(fontSize = 12.sp, textDecoration = TextDecoration.Underline)
        ) { offset ->
            annotatedString.getStringAnnotations("URL", offset, offset)
                .firstOrNull()?.let { annotation ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                    context.startActivity(intent)
                }
        }

        LivePlayerDialog(homeController)
        VideoPlayerDialog(homeController)
    }
}


@Preview
@Composable
fun PreviewMessageCard() {
    HomeScreen()
}
