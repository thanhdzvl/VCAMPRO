package com.wangyiheng.vcamsx.components

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.wangyiheng.vcamsx.MainActivity

@Composable
fun DisclaimerDialog() {
    var showDialog by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val disclaimer = "Tuyên bố miễn trừ trách nhiệm\n" +
            "Về ứng dụng\n" +
            "   Ứng dụng này cung cấp tính năng thay thế dữ liệu camera. Người dùng có thể thay đổi và điều chỉnh dữ liệu/hình ảnh thu được từ camera.\n\n" +
            "Điều kiện sử dụng\n" +
            "   Người dùng chịu hoàn toàn trách nhiệm với dữ liệu/hình ảnh đã tải lên, chỉnh sửa hoặc chia sẻ qua ứng dụng.\n\n" +
            "Mục đích hợp pháp\n" +
            "   Chỉ sử dụng ứng dụng cho mục đích hợp pháp, không dùng cho hoạt động trái phép hoặc chưa được ủy quyền.\n\n" +
            "Bản quyền và sở hữu trí tuệ\n" +
            "   Người dùng cam kết có quyền hợp pháp đối với dữ liệu/hình ảnh được xử lý qua ứng dụng.\n\n" +
            "Bảo vệ quyền riêng tư\n" +
            "   Tôn trọng quyền riêng tư của người khác; không thu thập/xử lý/phân phối thông tin cá nhân khi chưa được cho phép rõ ràng.\n\n" +
            "Giới hạn trách nhiệm\n" +
            "   Nhà phát triển không chịu trách nhiệm cho mọi hậu quả trực tiếp hoặc gián tiếp phát sinh từ việc sử dụng ứng dụng.\n\n" +
            "Nhà phát triển không chịu trách nhiệm đối với:\n" +
            "   1. Thiệt hại do người dùng sử dụng sai mục đích.\n" +
            "   2. Việc bên thứ ba sử dụng hoặc phụ thuộc vào ứng dụng.\n" +
            "   3. Thiệt hại gián tiếp, ngẫu nhiên, đặc biệt hoặc hệ quả khi sử dụng/không thể sử dụng ứng dụng.\n" +
            "   4. Mất dữ liệu do truy cập/sử dụng trái phép.\n\n" +
            "Mọi thay đổi đối với tuyên bố này sẽ được cập nhật trong ứng dụng hoặc trên trang chính thức và có hiệu lực ngay khi công bố.\n\n" +
            "Pháp luật áp dụng\n" +
            "   Việc giải thích và áp dụng tuyên bố này tuân theo pháp luật hiện hành."

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "Tuyên bố miễn trừ") },
            text = {
                Box(modifier = Modifier.heightIn(max = 300.dp)) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        disclaimer.split("\n").forEach { line ->
                            Text(text = line)
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Tôi đồng ý")
                }
            },
            dismissButton = {
                Button(onClick = { closeApp(context) }) {
                    Text("Không đồng ý")
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        )
    }
}

private fun closeApp(context: Context) {
    if (context is MainActivity) {
        context.finishAffinity()
    }
}
