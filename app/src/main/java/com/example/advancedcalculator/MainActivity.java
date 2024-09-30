package com.example.advancedcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    //Tite ka villar
    private EditText totaltxt;
    private String currentInput = "";
    private double firstOperand = 0;
    private String operator = "";
    private boolean isOperatorPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        totaltxt = findViewById(R.id.totaltxt);

        Button buttonC = findViewById(R.id.buttonC);
        Button buttonLessEquals = findViewById(R.id.buttonLessEquals);
        Button buttonPercent = findViewById(R.id.buttonPercent);
        Button buttonDivide = findViewById(R.id.buttonDivide);
        Button buttonMultiply = findViewById(R.id.buttonMultiply);
        Button buttonMinus = findViewById(R.id.buttonMinus);
        Button buttonPlus = findViewById(R.id.buttonPlus);
        Button buttonEquals = findViewById(R.id.buttonEquals);
        Button buttonPoint = findViewById(R.id.buttonPoint);
        Button button0 = findViewById(R.id.button0);
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);
        Button button6 = findViewById(R.id.button6);
        Button button7 = findViewById(R.id.button7);
        Button button8 = findViewById(R.id.button8);
        Button button9 = findViewById(R.id.button9);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button b = (Button) view;
                handleInput(b.getText().toString());
            }
        };
        buttonC.setOnClickListener(listener);
        buttonLessEquals.setOnClickListener(listener);
        buttonPercent.setOnClickListener(listener);
        buttonDivide.setOnClickListener(listener);
        buttonMultiply.setOnClickListener(listener);
        buttonMinus.setOnClickListener(listener);
        buttonPlus.setOnClickListener(listener);
        buttonEquals.setOnClickListener(listener);
        buttonPoint.setOnClickListener(listener);
        button0.setOnClickListener(listener);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
    }
    private void handleInput(String input){

        switch (input){
            case "C":
                clearAll();
                break;
            case "<=":
                backspace();
                break;
            case "+":
            case "-":
            case "/":
            case "X":
            case "%":
                setOperator(input);
                break;
            case "=":
                calculateResult();
                break;
            case ".":
                if (!currentInput.contains(".")){
                    currentInput += ".";
                }
                break;
            default:
                if(isOperatorPressed) {
                    currentInput = "";
                    isOperatorPressed = false;
                }
                currentInput += input;
                totaltxt.setText(currentInput);
                break;
        }
    }

    private void setOperator(String op) {
        if (!currentInput.isEmpty()) {
            firstOperand = Double.parseDouble(currentInput);
            operator = op;
            isOperatorPressed = true;
        }
    }

    private void calculateResult() {
        if (!currentInput.isEmpty() && !operator.isEmpty()) {
            double secondOperand = Double.parseDouble(currentInput);
            double result = 0;
            switch (operator) {
                case "+":
                    result = firstOperand + secondOperand;
                    break;
                case "-":
                    result = firstOperand - secondOperand;
                    break;
                case "X":
                    result = firstOperand * secondOperand;
                    break;
                case "/":
                    if (secondOperand != 0) {
                        result = firstOperand / secondOperand;
                    } else {
                        totaltxt.setText("Error");
                        return;
                    }
                    break;
                case "%":
                    result = firstOperand % secondOperand;
                    break;
            }
            totaltxt.setText(String.valueOf(result));
            currentInput = String.valueOf(result);
            operator = "";
        }
    }

    private void clearAll() {
        currentInput = "";
        firstOperand = 0;
        operator = "";
        totaltxt.setText("");
    }

    private void backspace() {
        if (currentInput.length() > 0) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            totaltxt.setText(currentInput);
        }
    }
}