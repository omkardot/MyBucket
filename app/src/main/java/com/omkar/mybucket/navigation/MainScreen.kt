package com.omkar.mybucket.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.omkar.mybucket.core.database.BucketDatabase
import com.omkar.mybucket.feature.dashboard.presentation.DashboardScreen
import com.omkar.mybucket.feature.dashboard.presentation.DashboardViewModel
import com.omkar.mybucket.feature.dashboard.presentation.DashboardViewModelFactory
import com.omkar.mybucket.feature.insights.presentation.InsightsScreen
import com.omkar.mybucket.feature.insights.presentation.InsightsViewModel
import com.omkar.mybucket.feature.insights.presentation.InsightsViewModelFactory
import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository
import com.omkar.mybucket.feature.responsibility.presentation.add_edit.AddEditResponsibilityViewModel
import com.omkar.mybucket.feature.responsibility.presentation.add_edit.AddTaskScreen
import com.omkar.mybucket.feature.responsibility.presentation.add_edit.AddTaskViewModelFacttory
import com.omkar.mybucket.feature.responsibility.presentation.detail.ResponsibilityDetailScreen
import com.omkar.mybucket.feature.responsibility.presentation.detail.ResponsibilityDetailViewModel
import com.omkar.mybucket.feature.responsibility.presentation.detail.ResponsibilityDetailViewModelFactory
import com.omkar.mybucket.feature.responsibility.presentation.list.ResponsibilityListScreen
import com.omkar.mybucket.feature.responsibility.presentation.list.ResponsibilityListViewModel
import com.omkar.mybucket.feature.responsibility.presentation.list.ResponsibilityListViewModelFactory
import com.omkar.mybucket.feature.timeline.presentation.TimelineScreen
import com.omkar.mybucket.feature.timeline.presentation.TimelineViewModel
import com.omkar.mybucket.feature.timeline.presentation.TimelineViewModelFactory

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Show Bottom Bar only on primary destinations
    val showBottomBar = currentRoute in BottomNavItem.items.map { it.route }

    val context = LocalContext.current
    val database = BucketDatabase.getDatabase(context)
    val repository = ResponsibilityRepository(database.responsibilityDao())

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Dashboard Tab
            composable(BottomNavItem.Dashboard.route) {
                val viewModel: DashboardViewModel = viewModel(
                    factory = DashboardViewModelFactory(repository)
                )
                DashboardScreen(
                    viewModel = viewModel,
                    onItemClick = { id -> navController.navigate(ScreenRoute.Detail.createRoute(id)) },
                    onAddClick = { navController.navigate(ScreenRoute.AddTask.createRoute()) }
                )
            }

            // 2. Responsibilities List Tab
            composable(BottomNavItem.Responsibilities.route) {
                val viewModel: ResponsibilityListViewModel = viewModel(
                    factory = ResponsibilityListViewModelFactory(repository)
                )
                ResponsibilityListScreen(
                    viewModel = viewModel,
                    onItemClick = { id -> navController.navigate(ScreenRoute.Detail.createRoute(id)) },

                )
            }

            // 3. Global Timeline Tab
            composable(BottomNavItem.Timeline.route) {
                val viewModel: TimelineViewModel = viewModel(
                    factory = TimelineViewModelFactory(repository)
                )
                TimelineScreen(
                    viewModel = viewModel,
                    onItemClick = { id -> navController.navigate(ScreenRoute.Detail.createRoute(id)) }
                )
            }

            // 4. Insights Tab
            composable(BottomNavItem.Insights.route) {
                val viewModel: InsightsViewModel = viewModel(
                    factory = InsightsViewModelFactory(repository)
                )
                InsightsScreen(viewModel = viewModel)
            }

            // 5. Add / Edit Responsibility Sub-screen
            composable(
                route = ScreenRoute.AddTask.route,
                arguments = listOf(
                    navArgument("responsibilityId") {
                        type = NavType.LongType
                        defaultValue = 0L
                    }
                )
            ) { backStackEntry ->
                val viewModel: AddEditResponsibilityViewModel = viewModel(
                    viewModelStoreOwner = backStackEntry,
                    factory = AddTaskViewModelFacttory(repository)
                )

                AddTaskScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            // 6. Responsibility Detail Sub-screen
            composable(
                route = ScreenRoute.Detail.route,
                arguments = listOf(navArgument("responsibilityId") { type = NavType.LongType; defaultValue = 0L })
            ) { backStack ->
                val id = backStack.arguments?.getLong("responsibilityId") ?: 0L
                val viewModel: ResponsibilityDetailViewModel = viewModel(
                    factory = ResponsibilityDetailViewModelFactory(id, repository)
                )
                ResponsibilityDetailScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}