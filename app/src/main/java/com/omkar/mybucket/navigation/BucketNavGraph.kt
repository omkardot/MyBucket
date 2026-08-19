package com.omkar.mybucket.navigation


import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toString
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.omkar.mybucket.core.database.BucketDatabase
import com.omkar.mybucket.feature.dashboard.presentation.DashboardScreen
import com.omkar.mybucket.feature.dashboard.presentation.DashboardViewModel
import com.omkar.mybucket.feature.dashboard.presentation.DashboardViewModelFactory
import com.omkar.mybucket.feature.insights.presentation.InsightsScreen
import com.omkar.mybucket.feature.insights.presentation.InsightsViewModel
import com.omkar.mybucket.feature.insights.presentation.InsightsViewModelFactory
import com.omkar.mybucket.feature.responsibility.data.ResponsibilityRepository
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
fun BucketNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoute.Dashboard.route,
        modifier = modifier
    ) {
        composable(ScreenRoute.Dashboard.route) {
            val database = BucketDatabase.getDatabase(navController.context)
            val repository = ResponsibilityRepository(database.responsibilityDao())
            val viewModel: DashboardViewModel = viewModel(
                factory = DashboardViewModelFactory(repository)
            )

            DashboardScreen(
                viewModel = viewModel,
                onItemClick = { responsibilityId ->
                    navController.navigate(ScreenRoute.Detail.createRoute(responsibilityId))
                },
                onAddClick = {
                    navController.navigate(ScreenRoute.AddTask.route)
                }
            )
        }
        composable(ScreenRoute.Responsibilities.route) {
            // Instantiate Repository & ViewModel (Use Hilt in production)
            val database = BucketDatabase.getDatabase(navController.context)
            val repository = ResponsibilityRepository(database.responsibilityDao())
            val viewModel: ResponsibilityListViewModel = viewModel(
                factory = ResponsibilityListViewModelFactory(repository)
            )

            ResponsibilityListScreen(
                viewModel = viewModel,
                onItemClick = { id ->
                    Log.d("Navcontroller",id.toString())
                    navController.navigate(ScreenRoute.Detail.createRoute(id))
                }
            )
        }
        composable(ScreenRoute.Timeline.route) {
            val database = BucketDatabase.getDatabase(navController.context)
            val repository = ResponsibilityRepository(database.responsibilityDao())
            val viewModel: TimelineViewModel = viewModel(
                factory = TimelineViewModelFactory(repository)
            )

            TimelineScreen(
                viewModel = viewModel,
                onItemClick = { responsibilityId ->
                    navController.navigate(ScreenRoute.Detail.createRoute(responsibilityId))
                }
            )
        }

        composable(ScreenRoute.Insights.route) {
            val database = BucketDatabase.getDatabase(navController.context)
            val repository = ResponsibilityRepository(database.responsibilityDao())
            val viewModel: InsightsViewModel = viewModel(
                factory = InsightsViewModelFactory(repository)
            )

            InsightsScreen(viewModel = viewModel)
        }
        composable(
            route = ScreenRoute.Detail.route,
            arguments = listOf(
                navArgument("responsibilityId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("responsibilityId") ?: return@composable


            val database = BucketDatabase.getDatabase(navController.context)
            val repository = remember { ResponsibilityRepository(database.responsibilityDao()) }
            val factory = remember(id) { ResponsibilityDetailViewModelFactory(id, repository) }
            val viewModel: ResponsibilityDetailViewModel = viewModel(factory = factory)

            ResponsibilityDetailScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

