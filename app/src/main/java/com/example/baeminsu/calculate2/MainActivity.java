package com.example.baeminsu.calculate2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    Button delete;
    Button l_bracket;
    Button r_bracket;
    Button divide;
    Button multiply;
    Button minus;
    Button plus;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn1;
    Button btn2;
    Button btn3;
    Button btn0;
    Button dot;
    Button percent;
    TextView result;
    TextView equation;
    Button equal;
    final int INIT_STATE = 0;
    final int NUMBER_STATE = 1;
    final int OPERATOR_STATE = 2;
    final int LB_STATE = 3;
    final int RB_STATE = 4;
    final int DOT_STATE = 5;
    final int END_STATE = 6;
    int bracketCount = 0;
    int state;

    Stack<Character> stackPostFix = new Stack<>();
    Queue<String> queue = new LinkedList<>();
    Stack<Double> stackCalculate = new Stack<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidget();
    }


    @Override
    public void onClick(View view) {
        int endflag = 0;
        Button tmpButton = (Button) view;

        if (state == END_STATE) {
            state = INIT_STATE;
            equation.setText("");
            result.setText("");
            endflag = 1;
        }

        switch (view.getId()) {

            case R.id.delete:
                int length = equation.getText().toString().length();

                if (state != END_STATE && endflag == 0) {
                    if (length > 1) {
                        if (equation.getText().toString().charAt(length - 1) == '(') {
                            bracketCount--;

                        } else if (equation.getText().toString().charAt(length - 1) == ')') {
                            bracketCount++;
                        }
                        equation.setText(equation.getText().toString().substring(0, length - 1));
                        state = charToState(equation.getText().toString().charAt(length - 2));

                    } else if (length == 1) {
                        equation.setText("");
                        state = 0;
                        bracketCount = 0;
                    }

                } else {
                    bracketCount = 0;
                    endflag = 0;
                    return;
                }
                break;

        /* state 1 - NUMBER */
            case R.id.btn1:
            case R.id.btn2:
            case R.id.btn3:
            case R.id.btn4:
            case R.id.btn5:
            case R.id.btn6:
            case R.id.btn7:
            case R.id.btn8:
            case R.id.btn9:
            case R.id.btn0:
                if (state != RB_STATE) {
                    state = NUMBER_STATE;
                    equation.setText(equation.getText() + tmpButton.getText().toString());

                }
                break;

        /* state 2 - OPERATOR */
            case R.id.plus:
            case R.id.div:
            case R.id.mul:
                if (state == NUMBER_STATE || state == RB_STATE) {
                    state = OPERATOR_STATE;
                    equation.setText(equation.getText() + tmpButton.getText().toString());
                }
                break;
            case R.id.min:
                if (state == NUMBER_STATE || state == LB_STATE || state == RB_STATE || state == INIT_STATE) {
                    state = OPERATOR_STATE;
                    equation.setText(equation.getText() + tmpButton.getText().toString());
                }
                break;

        /* state 3 - LEFT BRACKET */
            case R.id.l_bracket:
                if (state == INIT_STATE || state == OPERATOR_STATE || state == LB_STATE) {
                    state = LB_STATE;
                    bracketCount++;
                    equation.setText(equation.getText() + tmpButton.getText().toString());
                }
                break;

        /* state 4 - RIGHT BRACKET */
            case R.id.r_bracket:
                if ((state == NUMBER_STATE || state == RB_STATE) && bracketCount > 0) {
                    state = RB_STATE;
                    bracketCount--;
                    equation.setText(equation.getText() + tmpButton.getText().toString());
                }
                break;

        /* state 5 - DOT */
            case R.id.dot:
                if (state == NUMBER_STATE) {
                    state = DOT_STATE;
                    equation.setText(equation.getText() + tmpButton.getText().toString());
                }
                break;
        /* state 6 - END */
            case R.id.equal:
                if ((state == RB_STATE || state == NUMBER_STATE) && bracketCount == 0) {
                    state = END_STATE;
                    changePostFix(equation.getText().toString() + " ");
                    Double tmp = null;
                    try {
                        tmp = calculate();
                    } catch (Exception e) {
                        equation.setText("Error 연산할 수 없는 수식입니다");
                    }
                    //result.setText("= " + tmp.toString());
                    equation.setText(equation.getText() + " = " + tmp.toString());
                    break;
                } else if (bracketCount != 0) {
                    equation.setText("Error 괄호를 올바르게 입력해 주세요");
                    state = END_STATE;
                } else {
                    equation.setText("Error 유효한 수식을 입력해주세요");
                    state = END_STATE;
                }


        }

    }

    void setWidget() {

        l_bracket = (Button) findViewById(R.id.l_bracket);
        equation = (TextView) findViewById(R.id.equation);
        r_bracket = (Button) findViewById(R.id.r_bracket);
        percent = (Button) findViewById(R.id.percent);
        result = (TextView) findViewById(R.id.result);
        delete = (Button) findViewById(R.id.delete);
        multiply = (Button) findViewById(R.id.mul);
        equal = (Button) findViewById(R.id.equal);
        divide = (Button) findViewById(R.id.div);
        minus = (Button) findViewById(R.id.min);
        plus = (Button) findViewById(R.id.plus);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        btn0 = (Button) findViewById(R.id.btn0);
        dot = (Button) findViewById(R.id.dot);

        l_bracket.setOnClickListener(this);
        r_bracket.setOnClickListener(this);
        percent.setOnClickListener(this);
        multiply.setOnClickListener(this);
        delete.setOnClickListener(this);
        divide.setOnClickListener(this);
        equal.setOnClickListener(this);
        minus.setOnClickListener(this);
        plus.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        dot.setOnClickListener(this);

        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                initState();
                return false;
            }
        });

    }

    int charToState(char a) {
        switch (a) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return NUMBER_STATE;
            case '+':
            case '-':
            case '/':
            case '*':
            case '%':
                return OPERATOR_STATE;
            case '(':
                return LB_STATE;
            case ')':
                return RB_STATE;
            case '.':
                return DOT_STATE;
            default:
                return 0;
        }
    }

    int operatorRank(char a) {
        switch (a) {
            case '(':
                return 0;
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
        }
        return -1;
    }

    void changePostFix(String a) {

        int tmpState;
        char tmpChar;
        String queueInputString = "";

        for (int i = 0; i < a.length(); i++) {
            tmpChar = a.charAt(i);
            tmpState = charToState(tmpChar);

            if (i != 0 && a.charAt(i - 1) == '(' && tmpChar == '-' && queueInputString.equals("")) {
                queueInputString = "-";


            } else if (i == 0 && a.charAt(i) == '-') {
                queueInputString = "-";

            } else if ((tmpState == NUMBER_STATE) || (tmpState == DOT_STATE)) {
                queueInputString = queueInputString + tmpChar;
                if (charToState(a.charAt(i + 1)) != NUMBER_STATE && charToState(a.charAt(i + 1)) != DOT_STATE) {
                    queue.offer(queueInputString);
                    queueInputString = "";
                }

            } else if (tmpState == OPERATOR_STATE) {
                while (true) {
                    if (stackPostFix.isEmpty()) {
                        break;
                    } else if (operatorRank(stackPostFix.peek()) >= operatorRank(a.charAt(i)) && stackPostFix.peek() != '(') {
                        queue.offer(stackPostFix.pop() + "");
                    } else {
                        break;
                    }
                }
                stackPostFix.push(a.charAt(i));
            } else if (tmpState == LB_STATE) {
                stackPostFix.push(a.charAt(i));
            } else if (tmpState == RB_STATE) {
                while (true) {
                    if (!stackPostFix.isEmpty() && stackPostFix.peek() != '(') {
                        queue.offer(stackPostFix.pop() + "");
                    } else if (!stackPostFix.isEmpty() && stackPostFix.peek() != ')') {
                        stackPostFix.pop();
                        break;
                    } else {
                        break;
                    }
                }

            }

        }
        while (!stackPostFix.isEmpty()) {
            if (stackPostFix.peek() != ')' && stackPostFix.peek() != '(') {
                queue.offer(stackPostFix.pop() + "");
            }
        }
        /* 후위표기법 수식 출력 코드
        String tmp = "";
        while (!queue.isEmpty()) {
            tmp = tmp + queue.poll() + " ";

        }
        Log.i("후위연산 변환 결과", tmp);
        */
    }

    double calculate() throws Exception {

        double tmp1;
        double tmp2;

        while (!queue.isEmpty()) {
            switch (queue.peek()) {
                case "+":
                    queue.poll();
                    tmp1 = stackCalculate.pop();
                    tmp2 = stackCalculate.pop();
                    stackCalculate.push(tmp2 + tmp1);
                    break;
                case "-":
                    queue.poll();
                    tmp1 = stackCalculate.pop();
                    tmp2 = stackCalculate.pop();
                    stackCalculate.push(tmp2 - tmp1);
                    break;
                case "*":
                    queue.poll();
                    tmp1 = stackCalculate.pop();
                    tmp2 = stackCalculate.pop();
                    stackCalculate.push(tmp2 * tmp1);
                    break;
                case "/":
                    queue.poll();
                    tmp1 = stackCalculate.pop();
                    tmp2 = stackCalculate.pop();
                    stackCalculate.push(tmp2 / tmp1);
                    break;
                case "%":
                    queue.poll();
                    tmp1 = stackCalculate.pop();
                    tmp2 = stackCalculate.pop();
                    stackCalculate.push(tmp2 % tmp1);
                    break;
                default:
                    stackCalculate.push(Double.parseDouble(queue.poll()));
                    break;
            }
        }

        return stackCalculate.pop();

    }

    void initState() {
        stackCalculate.clear();
        stackPostFix.clear();
        queue.clear();
        state = 0;
        bracketCount = 0;
        equation.setText("");
        result.setText("");
    }
}
