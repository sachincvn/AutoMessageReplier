package com.chavan.automessagereplier

import androidx.compose.runtime.getValue
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContent {
            val navController = rememberNavController()
            SplashScreen(navController)
        }
    }
}

@Composable
fun SplashScreen(navController: NavController) {
    val splashMessageLottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_lottie))

    LaunchedEffect(key1 = true) {
//        splashMessageLottie?.duration?.let { delay(it.toLong() ) }
//        navController.navigate("main_activity_route")
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Red),
    ) {
        LottieAnimation(
            modifier = Modifier.align(Alignment.Center),
            composition = splashMessageLottie,
            iterations = LottieConstants.IterateForever,
        )
    }
}
