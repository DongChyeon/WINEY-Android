package com.teamwiney.winey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.teamwiney.core_design_system.theme.Background_1
import com.teamwiney.core_design_system.theme.WineyTheme
import com.teamwiney.winey.ui.login.LoginScreen
import com.teamwiney.winey.ui.splash.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WineyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Background_1
                ) {
                    LoginScreen()
                }
            }
        }
    }
}