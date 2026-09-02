package com.rk.taskmanager.oss.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.VideoSettings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rk.taskmanager.oss.ProcessViewModel
import com.rk.taskmanager.oss.R
import com.rk.taskmanager.oss.components.CollapsibleChartCard
import com.rk.taskmanager.oss.components.InfoItem
import com.rk.taskmanager.oss.screens.cpu.CPU
import com.rk.taskmanager.oss.screens.gpu.GPU
import com.rk.taskmanager.oss.screens.gpu.GpuViewModel
import com.rk.taskmanager.oss.screens.ram.RAM
import com.rk.commons.strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceHostScreen(
    modifier: Modifier = Modifier,
    viewModel: ProcessViewModel,
    gpuViewModel: GpuViewModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // CPU 卡片
        CollapsibleChartCard(
            title = stringResource(strings.cpu),
            icon = Icons.Default.Memory,
            usageText = "50%", // 实际使用率
            chartContent = { CPU(chartOnly = true, viewModel = viewModel) },
            detailsContent = {
                Column {
                    InfoItem(label = "温度", value = "45°C")
                    HorizontalDivider()
                    InfoItem(label = "频率", value = "2.4 GHz")
                    HorizontalDivider()
                    InfoItem(label = "核心数", value = "8")
                }
            }
        )
        
        // GPU 卡片
        CollapsibleChartCard(
            title = stringResource(strings.gpu),
            icon = Icons.Default.VideoSettings,
            usageText = "30%", // 实际使用率
            chartContent = { GPU(chartOnly = true, viewModel = gpuViewModel) },
            detailsContent = {
                Column {
                    InfoItem(label = "厂商", value = "Qualcomm")
                    HorizontalDivider()
                    InfoItem(label = "型号", value = "Adreno 730")
                    HorizontalDivider()
                    InfoItem(label = "Vulkan", value = "支持")
                }
            }
        )
        
        // 内存卡片
        CollapsibleChartCard(
            title = stringResource(strings.ram),
            icon = painterResource(id = R.drawable.memory_alt_24px),
            usageText = "60%", // 实际使用率
            chartContent = { RAM(chartOnly = true, viewModel = viewModel) },
            detailsContent = {
                Column {
                    InfoItem(label = "已用", value = "4.8 GB")
                    HorizontalDivider()
                    InfoItem(label = "可用", value = "3.2 GB")
                    HorizontalDivider()
                    InfoItem(label = "总计", value = "8.0 GB")
                }
            }
        )
        
        // 底部间距
        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}
