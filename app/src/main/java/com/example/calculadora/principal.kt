package com.example.calculadora // nombre del paquete

import android.content.Intent // importacion de la clase Intent
import android.net.Uri  // importacion de la clase Uri para abrir el navegador
import android.os.Bundle // importacion de la clase Bundle para guardar el estado de la actividad
import android.view.View // importacion de la clase View para manejar los eventos de los botones
import android.widget.Button // importacion de la clase Button para crear botones
import android.widget.TextView // importacion de la clase TextView para mostrar el resultado
import androidx.appcompat.app.AppCompatActivity // importacion de la clase AppCompatActivity para crear la actividad

class Principal : AppCompatActivity() { // clase principal

    private lateinit var textoResultado: TextView // TextView para mostrar el resultado
    private var numeroActual: String = "" // Para almacenar el número actual
    private var primerNumero: Double? = null // Para almacenar el primer número
    private var operador: String? = null // Para almacenar el operador actual
    private var resultadoMostrado = false // Para limpiar pantalla al comenzar una nueva entrada

    override fun onCreate(savedInstanceState: Bundle?) { // metodo onCreate
        super.onCreate(savedInstanceState) // inicializa la actividad
        setContentView(R.layout.activity_main) // establece el layout

        // Inicializa el TextView para mostrar el resultado
        textoResultado = findViewById(R.id.tvResult)

        // Configura los botones
        configurarBotonesNumericos()
        configurarBotonesOperadores()
    }

    private fun configurarBotonesNumericos() { // configura los botones numericos
        val botonesNumericos = listOf( // lista de botones numericos
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, // botones numericos
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9 // botones numericos
        )

        for (id in botonesNumericos) { // bucle for para configurar los botones
            findViewById<Button>(id).setOnClickListener { boton -> // evento click para cada boton esto guarda el numero en la variable boton
                if (resultadoMostrado) { // Limpia la pantalla si se muestra un resultado previo
                    numeroActual = "" // reinicia el numero actual
                    resultadoMostrado = false // reinicia la variable de resultado
                }
                val numero = (boton as Button).text.toString() // obtiene el texto del boton
                numeroActual += numero // agrega el numero al numero actual
                textoResultado.text = numeroActual // muestra el numero actual en el TextView
            }
        }
    }

    private fun configurarBotonesOperadores() { // configura los botones de operadores
        val botonesOperadores = mapOf( // mapa de botones y operadores
            R.id.btnAdd to "+", // suma
            R.id.btnSubtract to "-", // resta
            R.id.btnMultiply to "*", // multiplicacion
            R.id.btnDivide to "/" // division
        )

        for ((id, op) in botonesOperadores) { // bucle for para configurar los botones de operadores
            findViewById<Button>(id).setOnClickListener { // evento click para cada boton de operador
                if (numeroActual.isNotEmpty() || primerNumero != null) { // condicional para verificar que se haya ingresado un numero
                    if (numeroActual.isNotEmpty()) { // condicional para verificar que se haya ingresado un numero
                        primerNumero = numeroActual.toDoubleOrNull() // convierte el numero a double
                        if (primerNumero == null) { // condicional para verificar que se haya ingresado un numero
                            mostrarError("Entrada inválida") // muestra un error si no se puede convertir a double
                            return@setOnClickListener // retorna si no se puede convertir a double
                        }
                        numeroActual = "" // reinicia el numero actual
                    }
                    operador = op // guarda el operador actual
                    textoResultado.text = "" // limpia el TextView
                } else { // si no se ha ingresado un numero
                    mostrarError("Falta el primer número") // muestra un error
                }
            }
        }

        findViewById<Button>(R.id.btnEqual).setOnClickListener { // evento click para el boton de igual
            if (numeroActual.isNotEmpty() && operador != null && primerNumero != null) { // condicional para verificar que se haya ingresado un numero
                val segundoNumero = numeroActual.toDoubleOrNull() // convierte el numero a double
                if (segundoNumero == null) { // condicional para verificar que se haya ingresado un numero
                    mostrarError("Entrada inválida")
                    return@setOnClickListener // retorna si no se puede convertir a double
                }
                if (operador == "/" && segundoNumero == 0.0) { // condicional para verificar que se haya ingresado un numero
                    mostrarError("No se puede dividir por cero") // muestra un error si se intenta dividir por cero
                    return@setOnClickListener // retorna si se intenta dividir por cero
                }
                val resultado = realizarOperacion(primerNumero!!, segundoNumero, operador!!) // realiza la operacion
                textoResultado.text = resultado.toString() // muestra el resultado en el TextView

                // Actualiza `primerNumero` con el resultado para permitir operaciones consecutivas
                primerNumero = resultado
                numeroActual = ""
                operador = null
                resultadoMostrado = true
            } else if (primerNumero != null && operador != null) { // condicional para verificar que se haya ingresado un numero
                mostrarError("Falta el segundo número")
            } else { // si no se ha ingresado un numero
                mostrarError("Entrada incompleta")
            }
        }

        findViewById<Button>(R.id.btnClear).setOnClickListener { // evento click para el boton de limpiar
            reiniciarCalculadora() // reinicia la calculadora
            textoResultado.text = "0" // muestra 0 en el TextView
        }
    }

    private fun realizarOperacion(num1: Double, num2: Double, operador: String): Double { // funcion para realizar la operacion
        return when (operador) { // bucle para realizar la operacion
            "+" -> num1 + num2
            "-" -> num1 - num2
            "*" -> num1 * num2
            "/" -> num1 / num2
            else -> 0.0
        }
    }

    private fun reiniciarCalculadora() { // funcion para reiniciar la calculadora
        numeroActual = ""
        primerNumero = null
        operador = null
        resultadoMostrado = false
    }

    private fun mostrarError(mensaje: String) { // funcion para mostrar un error
        textoResultado.text = mensaje
        resultadoMostrado = true
        reiniciarCalculadora()
    }


    fun github(view: View) {
        val url = "https://github.com/davikho" // mi github
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent)
    }
}
