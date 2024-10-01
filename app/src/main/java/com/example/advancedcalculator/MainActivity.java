package com.example.advancedcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private EditText totaltxt;
    private String currentExpression = "";
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
        totaltxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    totaltxt.setSelection(totaltxt.getText().length()); // Move cursor to the end
                }
            }
        });

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

    private void handleInput(String input) {
        switch (input) {
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
                if (!currentExpression.isEmpty()) {
                    if (isOperatorPressed) {
                        // Replace the operator if an operator was already pressed
                        currentExpression = currentExpression.substring(0, currentExpression.length() - 1) + input;
                    } else {
                        currentExpression += input;
                    }
                    isOperatorPressed = true;
                }
                break;
            case "=":
                calculateResult();
                break;
            case ".":
                if (!currentExpression.endsWith(".")) {
                    currentExpression += ".";
                }
                break;
            default:
                currentExpression += input;
                totaltxt.setText(currentExpression);
                isOperatorPressed = false;
                break;
        }
        totaltxt.setText(currentExpression);
        totaltxt.setSelection(currentExpression.length());
    }

    private void calculateResult() {
        try {
            // Check if current expression ends with '%'
            if (currentExpression.endsWith("%")) {
                String numberPart = currentExpression.substring(0, currentExpression.length() - 1);
                double number = Double.parseDouble(numberPart);
                double result = number / 100;  // Convert to percentage
                totaltxt.setText(String.valueOf(result));
                currentExpression = String.valueOf(result);  // Update current expression for further calculations
                return;
            }

            if (currentExpression.length() > 0 && isOperator(currentExpression.charAt(currentExpression.length() - 1))) {
                currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
            }

            // If no operator is present, just print the single operand
            if (currentExpression.matches("^[0-9.]+$")) {
                totaltxt.setText(currentExpression);  // No operation, just display the number
                return;
            }

            // Evaluate the expression with operators
            double result = evaluateExpression(currentExpression.replace("X", "*"));
            String res = String.valueOf(result);
            if (res.endsWith(".0")) {
                res = res.replace(".0", "");
                totaltxt.setText(res);
                currentExpression = res;
            } else {
                totaltxt.setText(res);
                currentExpression = res;
            }
        } catch (Exception e) {
            totaltxt.setText("Error");
            currentExpression = "";
        }
    }


    // Simple method to evaluate the expression using stacks
    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If the character is a digit, parse the full number
            if (Character.isDigit(c)) {
                StringBuilder number = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i++));
                }
                i--; // step back to correct for the extra increment
                numbers.push(Double.parseDouble(number.toString()));
            }
            // If the character is an operator
            else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(c);
            }
        }

        // Apply remaining operators
        while (!operators.isEmpty()) {
            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    // Returns precedence of operators
    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            default:
                return -1;
        }
    }

    // Applies an operator to two numbers
    private double applyOperator(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            case '%':
                return (a / 100) * b;
            default:
                return 0;
        }
    }

    private void clearAll() {
        currentExpression = "";
        totaltxt.setText("");
    }

    private void backspace() {
        if (currentExpression.length() > 0) {
            currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
            totaltxt.setText(currentExpression);
        }
    }
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == 'X' || c == '/'|| c == '%';
    }
}
















