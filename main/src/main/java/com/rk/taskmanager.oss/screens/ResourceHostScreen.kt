package com.rk.taskmanager.oss.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.rk.commons.ui.SectionHeader
import com.rk.taskmanager.oss.screens.cpu.CPU
import com.rk.taskmanager.oss.screens.cpu.cpuUsage
import com.rk.taskmanager.oss.screens.cpu.cpuTemperature
import com.rk.taskmanager.oss.screens.cpu.cpuInfo
import com.rk.taskmanager.oss.screens.cpu.cpuUptime
import com.rk.taskmanager.oss.screens.cpu.ClusterCard
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
    val cpuTemperature by cpuTemperature.collectAsState()
    val cpuInfo by cpuInfo.collectAsState()
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoItem(label = stringResource(strings.soc), value = cpuInfo?.soc ?: stringResource(strings.no_data))
                    HorizontalDivider()
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            InfoItem(label = stringResource(strings.architecture), value = cpuInfo?.arch ?: stringResource(strings.no_data))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            InfoItem(label = stringResource(strings.abi), value = cpuInfo?.abi ?: stringResource(strings.no_data))
                        }
                    }
                    HorizontalDivider()
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            InfoItem(label = stringResource(strings.cores), value = cpuInfo?.cores?.toString() ?: stringResource(strings.no_data))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            InfoItem(label = stringResource(strings.governor), value = cpuInfo?.governor ?: stringResource(strings.no_data))
                        }
                    }
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.temperature), value = cpuTemperature)
                    
                    // System Stats
                    SectionHeader(stringResource(strings.system_stats))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Column(modifier = Modifier.weight(1f)) {
                            InfoItem(label = stringResource(strings.procs), value = viewModel.procCount.collectAsState().value.toString())
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            InfoItem(label = stringResource(strings.threads), value = viewModel.threadCount.collectAsState().value.toString())
                        }
                    }
                    HorizontalDivider()
                    val cpuUptimeValue by cpuUptime.collectAsState()
                    InfoItem(label = stringResource(strings.uptime), value = cpuUptimeValue)
                    
                    // Clusters
                    if (cpuInfo?.clusters?.isNotEmpty() == true) {
                        SectionHeader(stringResource(strings.clusters))
                        cpuInfo?.clusters?.forEach { cluster ->
                            ClusterCard(cluster)
                        }
                    }
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoItem(label = stringResource(strings.vendor), value = gpuInfo?.vendor ?: stringResource(strings.no_data))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.gpu_model), value = gpuInfo?.renderer ?: stringResource(strings.no_data))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.opengl), value = gpuInfo?.openGlVersion ?: stringResource(strings.no_data))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.vulkan), value = if (gpuInfo?.vulkanSupported == true) stringResource(strings.supported) else stringResource(strings.not_supported))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.vulkan_api), value = gpuInfo?.vulkanApiVersion ?: stringResource(strings.no_data))
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoItem(label = stringResource(strings.used), value = FormatUtils.formatBytes(usedRam))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.total), value = FormatUtils.formatBytes(totalRam))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.available), value = FormatUtils.formatBytes(totalRam - usedRam))
                    
                    // Swap Info
                    SectionHeader(stringResource(strings.swap))
                    InfoItem(label = stringResource(strings.used), value = FormatUtils.formatBytes(usedSwap))
                    HorizontalDivider()
                    InfoItem(label = stringResource(strings.total), value = FormatUtils.formatBytes(totalSwap))
                }
            }
        )
        
        // 底部间距
        Spacer(
            modifier = Modifier.height(16.dp)
        )
    }
}
