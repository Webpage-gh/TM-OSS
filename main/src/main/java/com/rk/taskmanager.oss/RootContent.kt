package com.rk.taskmanager.oss

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rk.taskmanager.oss.animations.NavigationAnimationTransitions
import com.rk.taskmanager.oss.screens.MainScreen
import com.rk.taskmanager.oss.screens.ProcessInfo
import com.rk.taskmanager.oss.screens.procByPid
import com.rk.taskmanager.oss.settings.About
import com.rk.taskmanager.oss.settings.DaemonSettings
import com.rk.taskmanager.oss.settings.GraphSettings
import com.rk.taskmanager.oss.settings.ProcSettings
import com.rk.taskmanager.oss.settings.SelectedWorkingMode
import com.rk.commons.settings.Settings
import com.rk.taskmanager.oss.settings.SettingsRoutes
import com.rk.taskmanager.oss.settings.SettingsScreen
import com.rk.taskmanager.oss.settings.Themes
import com.rk.taskmanager.oss.ui.theme.TaskManagerTheme
import java.lang.ref.WeakReference

var navControllerRef: WeakReference<NavController?> = WeakReference(null)
    private set
@Composable
fun MainActivity.RootContent(modifier: Modifier = Modifier) {
    TaskManagerTheme {
        Surface {
            val navController = rememberNavController()
            navControllerRef = WeakReference(navController)
            NavHost(
                navController = navController,
                startDestination = if (Settings.workingMode == -1) {
                    SettingsRoutes.SelectWorkingMode.route
                } else {
                    SettingsRoutes.Home.route
                },
                enterTransition = { NavigationAnimationTransitions.enterTransition },
                exitTransition = { NavigationAnimationTransitions.exitTransition },
                popEnterTransition = { NavigationAnimationTransitions.popEnterTransition },
                popExitTransition = { NavigationAnimationTransitions.popExitTransition },
            ) {
                composable(SettingsRoutes.Home.route) {
                    MainScreen(navController = navController, viewModel = viewModel, gpuViewModel = gpuViewModel)
                }

                composable(SettingsRoutes.SelectWorkingMode.route) {
                    SelectedWorkingMode(navController = navController)
                }

                composable(SettingsRoutes.Settings.route) {
                    SettingsScreen(navController = navController)
                }

                composable(SettingsRoutes.Daemon.route){
                    DaemonSettings()
                }

                composable(SettingsRoutes.Graphs.route){
                    GraphSettings()
                }

                composable(SettingsRoutes.Procs.route){
                    ProcSettings()
                }

                composable(SettingsRoutes.Themes.route){
                    Themes()
                }

                composable(SettingsRoutes.About.route){
                    About()
                }


                composable("proc/{pid}") {
                    val pid = it.arguments?.getString("pid")!!.toInt()
                    val proc = procByPid[pid]?.get()

                    if (proc != null) {
                        LaunchedEffect(Unit) {
                            if (proc.killed.value){
                                navController.popBackStack()
                                return@LaunchedEffect
                            }
                        }
                        ProcessInfo(proc = proc, navController = navController, viewModel = viewModel)
                    }else{
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}