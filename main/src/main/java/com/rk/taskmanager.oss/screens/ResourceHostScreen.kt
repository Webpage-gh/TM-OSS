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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rk.taskmanager.oss.ProcessViewModel
import com.rk.taskmanager.oss.components.CollapsibleChartCard
import com.rk.taskmanager.oss.components.InfoItem
import com.rk.taskmanager.oss.screens.cpu.CPU
import com.rk.taskmanager.oss.screens.cpu.cpuUsage
import com.rk.taskmanager.oss.screens.gpu.GPU
import com.rk.taskmanager.oss.screens.gpu.GpuViewModel
import com.rk.taskmanager.oss.screens.gpu.gpuUsage
import com.rk.taskmanager.oss.screens.ram.RAM
import com.rk.taskmanager.oss.screens.ram.RamUsage
import com.rk.taskmanager.oss.screens.ram.usedRam
import com.rk.taskmanager.oss.screens.ram.totalRam
import com.rk.taskmanager.oss.screens.ram.usedSwap
import com.rk.taskmanager.oss.screens.ram.totalSwap
import com.rk.taskmanager.oss.screens.ram.SwapUsage
import com.rk.commons.strings
import com.rk.commons.getString
import com.rk.commons.utils.FormatUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResourceHostScreen(
    modifier: Modifier = Modifier,
    viewModel: ProcessViewModel,
    gpuViewModel: GpuViewModel
) {
    val cpuUsage by cpuUsage.collectAsState()
    val gpuInfo by gpuViewModel.gpuInfo.collectAsState()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // CPU 卡片
        CollapsibleChartCard(
            title = stringResource(strings.cpu),
            icon = Icons.Default.Memory,
            usageText = if (cpuUsage < 0) stringResource(strings.no_data) else "$cpuUsage%",
            chartContent = { CPU(chartOnly = true, viewModel = viewModel) },
            detailsContent = {
                Column {
                    // 注意：这里需要从CPU组件获取详细信息，但为了简化，暂时使用占位符
                    // 实际实现中，您可能需要将CPU组件的详细信息传递到此处
                    InfoItem(label = stringResource(strings.temperature), value = "N/A")
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.cores), value = "N/A")
                }
            }
        )
        
        // GPU 卡片
        CollapsibleChartCard(
            title = stringResource(strings.gpu),
            icon = Icons.Default.VideoSettings,
            usageText = if (gpuUsage < 0) stringResource(strings.no_data) else "$gpuUsage%",
            chartContent = { GPU(chartOnly = true, viewModel = gpuViewModel) },
            detailsContent = {
                Column {
                    InfoItem(label = stringResource(strings.vendor), value = gpuInfo?.vendor ?: stringResource(strings.no_data))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.gpu_model), value = gpuInfo?.renderer ?: stringResource(strings.no_data))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.vulkan), value = if (gpuInfo?.vulkanSupported == true) stringResource(strings.supported) else stringResource(strings.not_supported))
                }
            }
        )
        
        // 内存卡片
        CollapsibleChartCard(
            title = stringResource(strings.ram),
            icon = Icons.Default.Memory,
            usageText = "$RamUsage%",
            chartContent = { RAM(chartOnly = true, viewModel = viewModel) },
            detailsContent = {
                Column {
                    InfoItem(label = stringResource(strings.used), value = FormatUtils.formatBytes(usedRam))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.available), value = FormatUtils.formatBytes(totalRam - usedRam))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.total), value = FormatUtils.formatBytes(totalRam))
                }
            }
        )
        
        // 底部间距
        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}
