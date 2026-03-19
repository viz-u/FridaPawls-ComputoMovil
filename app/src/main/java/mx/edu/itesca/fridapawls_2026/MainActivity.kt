package mx.edu.itesca.fridapawls_2026

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mx.edu.itesca.fridapawls_2026.navigation.NavGraph
import mx.edu.itesca.fridapawls_2026.ui.theme.FridaPawls2026Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            FridaPawls2026Theme {
                NavGraph()
            }
        }
    }
}