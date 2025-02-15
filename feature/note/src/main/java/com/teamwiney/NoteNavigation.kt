package com.teamwiney

import NoteListScreen
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.teamwiney.core.common.WineyAppState
import com.teamwiney.core.common.WineyBottomSheetState
import com.teamwiney.core.common.navigation.NoteDestinations
import com.teamwiney.notecollection.NoteFilterScreen
import com.teamwiney.notecollection.NoteScreen
import com.teamwiney.notedetail.NoteDetailScreen
import com.teamwiney.notedetail.OtherNoteDetailScreen
import com.teamwiney.notewrite.noteWriteGraph

fun NavGraphBuilder.noteGraph(
    appState: WineyAppState,
    bottomSheetState: WineyBottomSheetState,
    kakaoLinkScheme: String
) {
    navigation(
        route = NoteDestinations.ROUTE,
        startDestination = NoteDestinations.NOTE
    ) {
        composable(route = NoteDestinations.NOTE) {
            val backStackEntry = rememberNavControllerBackStackEntry(
                entry = it,
                navController = appState.navController,
                graph = NoteDestinations.ROUTE
            )
            NoteScreen(
                appState = appState,
                bottomSheetState = bottomSheetState,
                viewModel = hiltViewModel(backStackEntry)
            )
        }

        composable(route = NoteDestinations.FILTER) {
            val backStackEntry = rememberNavControllerBackStackEntry(
                entry = it,
                navController = appState.navController,
                graph = NoteDestinations.ROUTE
            )
            NoteFilterScreen(
                appState = appState,
                viewModel = hiltViewModel(backStackEntry)
            )
        }

        composable(
            route = "${NoteDestinations.NOTE_DETAIL}?id={noteId}&isShared={isShared}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("isShared") {
                    defaultValue = true
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "$kakaoLinkScheme?id={noteId}"
                    action = Intent.ACTION_VIEW
                }
            ),
        ) { entry ->
            val isShared = entry.arguments?.getBoolean("isShared") ?: true

            Log.d("NoteDetailScreen", "isShared : $isShared")

            if (isShared) {
                OtherNoteDetailScreen(
                    appState = appState,
                    viewModel = hiltViewModel(),
                    bottomSheetState = bottomSheetState
                )
            } else {
                NoteDetailScreen(
                    appState = appState,
                    viewModel = hiltViewModel(),
                    bottomSheetState = bottomSheetState,
                )
            }
        }

        composable(
            route = "${NoteDestinations.NOTE_LIST}?id={wineId}",
            arguments = listOf(
                navArgument("wineId") {
                    type = NavType.LongType
                    defaultValue = 0
                }
            )
        ) {
            NoteListScreen(
                appState = appState,
                viewModel = hiltViewModel()
            )
        }

        noteWriteGraph(
            appState = appState,
            bottomSheetState = bottomSheetState
        )
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