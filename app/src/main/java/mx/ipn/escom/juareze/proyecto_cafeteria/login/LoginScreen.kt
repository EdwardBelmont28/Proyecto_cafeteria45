package mx.ipn.escom.juareze.proyecto_cafeteria.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import mx.ipn.escom.juareze.proyecto_cafeteria.navegacion.pantallasCaf

@Composable
fun LoginSesion(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val mostrarFormulario = rememberSaveable {
        mutableStateOf(true)
    }
    Surface(modifier = Modifier
        .fillMaxSize()
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            if(mostrarFormulario.value){
                Text(text = "Inicio de sesion")
                UserForm(
                    isCreateAccount = false
                ){
                    email, password ->
                    Log.d("Usuario correcto", "Logueado con $email y $password")
                    viewModel.sesionCorreoContraseña(email,password){
                        navController.navigate(pantallasCaf.HomeScreen.name)
                    }
                }
            }
            else{
                Text(text = "Crea una cuenta")
                UserForm(
                    isCreateAccount = true
                )
                {
                        email, password ->
                    Log.d("Usuario correcto", "Creado con $email y $password")
                    viewModel.crearUsuario(email, password){
                        navController.navigate(pantallasCaf.HomeScreen.name)
                    }

                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 =
                    if (mostrarFormulario.value) "¿No tienes cuenta? "
                    else "¿Ya tienes cuenta? "
                val text2 =
                    if (mostrarFormulario.value) " Registrate"
                    else " Inicia sesión"
                Text(text = text1)
                Text(text = text2,
                    modifier = Modifier
                        .clickable { mostrarFormulario.value = !mostrarFormulario.value }
                        .padding(end = 5.dp),
                    color = MaterialTheme.colorScheme.secondary)
            }
        }

    }
}

@Preview
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun UserForm(
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = {email, pwd ->}
){
    val email = rememberSaveable {
        mutableStateOf("")
    }
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val valido = remember(email.value, password.value){
        email.value.trim().isNotEmpty()&&
                password.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        EmailImput(
            emailState = email
        )
        passwordInput(
            passwordState = password,
            labelId = "Contraseña",
            passwordVisible = passwordVisible
        )
        SubmitButton(
            textId = if (isCreateAccount) "Crear cuenta" else "Iniciar sesion",
            inputValido  = valido
        ){
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    Onclic: ()->Unit
) {
    Button(onClick = Onclic,
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth(),
        shape = CircleShape,
        enabled = inputValido
    ) {
        Text(text = textId,
            modifier = Modifier
                .padding(5.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun passwordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value, 
        onValueChange = {passwordState.value = it},
        label = { Text(text = labelId)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password
        ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if(passwordState.value.isNotBlank()){
                PasswordVisibleIcon(passwordVisible)
            }
            else null
        }
        )
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image =
        if(passwordVisible.value)
            Icons.Default.VisibilityOff
        else
            Icons.Default.Visibility
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }) {
        Icon(
            imageVector = image,
            contentDescription = "")
    }
}

@Composable
fun EmailImput(
    emailState: MutableState<String>,
    labelId : String = "Email"
) {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyBoardType = KeyboardType.Email

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    isSingLine: Boolean = true,
    keyBoardType: KeyboardType
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = {valueState.value = it},
        label = {Text(text = labelId)},
        singleLine = isSingLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyBoardType
        )
        )
}
