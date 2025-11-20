package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var displayTextView: TextView
    private lateinit var operationTextView: TextView

    private var currentNumber = ""
    private var previousNumber = ""
    private var operation = ""
    private var shouldResetDisplay = false

    private val decimalFormat = DecimalFormat("#.##########")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayTextView = findViewById(R.id.displayTextView)
        operationTextView = findViewById(R.id.operationTextView)
    }

    fun onDigitClick(view: View) {
        val digit = (view as Button).text.toString()

        if (shouldResetDisplay) {
            currentNumber = ""
            shouldResetDisplay = false
        }

        if (digit == "." && currentNumber.contains(".")) {
            return
        }

        if (digit == "0" && currentNumber == "0") {
            return
        }

        if (currentNumber == "0" && digit != ".") {
            currentNumber = digit
        } else {
            currentNumber += digit
        }

        updateDisplay()
    }

    fun onOperationClick(view: View) {
        val op = (view as Button).text.toString()

        if (currentNumber.isNotEmpty()) {
            if (previousNumber.isNotEmpty() && operation.isNotEmpty()) {
                calculate()
            } else {
                previousNumber = currentNumber
            }

            operation = op
            operationTextView.text = "$previousNumber $operation"
            currentNumber = ""
            shouldResetDisplay = false
        } else if (previousNumber.isNotEmpty()) {
            operation = op
            operationTextView.text = "$previousNumber $operation"
        }
    }

    fun onEqualsClick() {
        if (currentNumber.isNotEmpty() && previousNumber.isNotEmpty() && operation.isNotEmpty()) {
            calculate()
            operationTextView.text = ""
            operation = ""
            previousNumber = ""
            shouldResetDisplay = true
        }
    }

    fun onClearClick() {
        currentNumber = ""
        previousNumber = ""
        operation = ""
        shouldResetDisplay = false
        displayTextView.text = "0"
        operationTextView.text = ""
    }

    fun onBackspaceClick(view: View) {
        if (currentNumber.isNotEmpty() && !shouldResetDisplay) {
            currentNumber = currentNumber.dropLast(1)
            updateDisplay()
        }
    }

    fun onPercentClick(view: View) {
        if (currentNumber.isNotEmpty()) {
            val number = currentNumber.toDoubleOrNull() ?: return
            currentNumber = decimalFormat.format(number / 100)
            updateDisplay()
        }
    }

    fun onPlusMinusClick(view: View) {
        if (currentNumber.isNotEmpty()) {
            currentNumber = if (currentNumber.startsWith("-")) {
                currentNumber.substring(1)
            } else {
                "-$currentNumber"
            }
            updateDisplay()
        }
    }

    private fun calculate() {
        val num1 = previousNumber.toDoubleOrNull() ?: return
        val num2 = currentNumber.toDoubleOrNull() ?: return

        val result = when (operation) {
            "+" -> num1 + num2
            "−" -> num1 - num2
            "×" -> num1 * num2
            "÷" -> {
                if (num2 == 0.0) {
                    displayTextView.text = "Error"
                    currentNumber = ""
                    previousNumber = ""
                    operation = ""
                    return
                }
                num1 / num2
            }
            else -> return
        }

        currentNumber = decimalFormat.format(result)
        previousNumber = currentNumber
        updateDisplay()
    }

    private fun updateDisplay() {
        displayTextView.text = if (currentNumber.isEmpty()) "0" else currentNumber
    }
}