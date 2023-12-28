package com.teamwiney.mypage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.teamwiney.core.common.WineyAppState
import com.teamwiney.core.common.WineyBottomSheetState
import com.teamwiney.core.common.navigation.MyPageDestinations

fun NavGraphBuilder.myPageGraph(
    appState: WineyAppState,
    bottomSheetState: WineyBottomSheetState
) {
    navigation(
        route = MyPageDestinations.ROUTE,
        startDestination = MyPageDestinations.MY_PAGE
    ) {
        composable(route = MyPageDestinations.MY_PAGE) {
            val backStackEntry = rememberNavControllerBackStackEntry(
                entry = it,
                navController = appState.navController,
                graph = MyPageDestinations.ROUTE
            )
            MyPageScreen(
                appState = appState,
                viewModel = hiltViewModel(backStackEntry)
            )
        }

        composable(route = MyPageDestinations.BADGE) {
            MyPageBadgeScreen(
                appState = appState
            )
        }

        composable(route = MyPageDestinations.ACCOUNT) {
            val backStackEntry = rememberNavControllerBackStackEntry(
                entry = it,
                navController = appState.navController,
                graph = MyPageDestinations.ROUTE
            )
            MyPageAccountScreen(
                appState = appState,
                viewModel = hiltViewModel(backStackEntry),
                bottomSheetState = bottomSheetState
            )
        }

        composable(route = MyPageDestinations.WITHDRAWAL_REASON_SELECT) {
            val backStackEntry = rememberNavControllerBackStackEntry(
                entry = it,
                navController = appState.navController,
                graph = MyPageDestinations.ROUTE
            )
            MyPageWithdrawalReasonSelectScreen(
                appState = appState,
                viewModel = hiltViewModel(backStackEntry),
                bottomSheetState = bottomSheetState
            )
        }

        composable(route = MyPageDestinations.WITHDRAWAL_CONFIRM) {
            val backStackEntry = rememberNavControllerBackStackEntry(
                entry = it,
                navController = appState.navController,
                graph = MyPageDestinations.ROUTE
            )
            MyPageWithdrawalConfirmScreen(
                appState = appState,
                viewModel = hiltViewModel(backStackEntry),
                bottomSheetState = bottomSheetState
            )
        }

        // TODO : 프로필


        // TODO : 버전 정보

        // TODO : 고객센터
    }
}

@Composable
fun rememberNavControllerBackStackEntry(
    entry: NavBackStackEntry,
    navController: NavController,
    graph: String,
) = remember(entry) {
    navController.getBackStackEntry(graph)
}