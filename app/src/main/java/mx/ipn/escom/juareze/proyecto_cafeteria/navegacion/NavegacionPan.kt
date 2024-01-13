package mx.ipn.escom.juareze.proyecto_cafeteria.navegacion

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.ipn.escom.juareze.proyecto_cafeteria.login.LoginSesion
import mx.ipn.escom.juareze.proyecto_cafeteria.pantallas.Bienvenida
import mx.ipn.escom.juareze.proyecto_cafeteria.pantallas.home.Home

@Composable
fun NavegacionPan(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = pantallasCaf.LoginScreen.name) {
        composable(pantallasCaf.LoginScreen.name) {
            LoginSesion(navController = navController)
        }
        composable(pantallasCaf.HomeScreen.name) {
            Home(navController = navController)
        }
        composable(pantallasCaf.BienvenidaScreen.name) {
            Bienvenida(navController = navController)
        }
    }
}
