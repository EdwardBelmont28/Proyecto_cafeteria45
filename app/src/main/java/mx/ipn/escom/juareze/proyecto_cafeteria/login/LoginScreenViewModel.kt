package mx.ipn.escom.juareze.proyecto_cafeteria.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch


class LoginScreenViewModel: ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun sesionCorreoContraseña(email:String, password: String, home: ()->Unit)
    = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        Log.d("MenSesion","sesionCorreoContraseña")
                        home()
                    }
                    else{
                        Log.d("MenSesion", "sesionCorreoContraseña: ${task.result.toString()}")
                    }
                }

        }
        catch (ex:Exception){
            Log.d("MenSesion", "sesionCorreoContraseña: ${ex.message}")
        }
    }

    fun crearUsuario(
        email:String,
        password: String,
        home: () ->Unit
    ){
        if (_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val mostrarNombre=
                            task.result.user?.email?.split("@")?.get(0)
                        crearUsuario(mostrarNombre)
                        home()
                    }
                    else{
                        Log.d("Inicio Sesion","Usuario creado con: ${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun crearUsuario(mostarNombre: String?) {
        val userId = auth.currentUser?.uid
        val usuario = mutableMapOf<String, Any>()

        usuario["user_id"] = userId.toString()
        usuario["display_name"] = mostarNombre.toString()
        FirebaseFirestore.getInstance().collection("Usuario")
            .add(usuario)
            .addOnSuccessListener {
                Log.d("Inicio sesion", "Usuario ${it.id} creado")
            }.addOnFailureListener {
                Log.d("Login", "Ocurrio un error ${it}")
            }

    }
}

