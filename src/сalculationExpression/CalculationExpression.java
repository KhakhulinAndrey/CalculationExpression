package сalculationExpression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;
import java.util.StringTokenizer;


/**
 * Задание:
 * Есть формула с цифрами, операциями +-*(/) и скобками ( ). Нужно написать
 * программу, которая ее вычисляет.  Формула может быть большой.
 * Дополнительная часть: предусмотреть (архитектурно) возможность доработки –
 * возможность вводить функции, вводить параметры для расчета формулы.
 *
 * Решил задачу, преобразуя выражение из инфиксной нотации в обратную польскую нотацию (ОПН) по алгоритму "Сортировочная
 * станция" Эдскера Дейкстры. Отличительной особенностью обратной польской нотации является то, что все
 * аргументы (или операнды) расположены перед операцией. Это позволяет избавиться от необходимости использования
 * скобок. Например, выражение, записаное в инфиксной нотации как 3 * (4 + 7), будет выглядеть как 3 4 7 + *
 * в ОПН.
 */

public class CalculationExpression {

    private final String[] FUNCTIONS = {"abs", "acos", "arg", "asin", "atan", "conj", "cos", "cosh", "exp", "log", "neg", "pow", "real", "sin", "sinh", "sqrt", "tan", "tanh"};
    private final String OPERATORS = "+-*/";
    private final String SEPARATOR = ",";
    private final String VARIABLE = "var";
    // временный стек , который содержит операторы, функции и скобки
    private Stack<String> stackOperations = new Stack<String>();
    // стек для хранения выражениея, преобразованного в обратную польскую запись
    private Stack<String> stackRPN = new Stack<String>();


    private boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
        } catch (Exception e) {
            if (token.equals(VARIABLE)) {
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean isFunction(String token) {
        for (String item : FUNCTIONS) {
            if (item.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSeparator(String token) {
        return token.equals(SEPARATOR);
    }

    private boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    private boolean isCloseBracket(String token) {
        return token.equals(")");
    }

    private boolean isOperator(String token) {
        return OPERATORS.contains(token);
    }

    //получить приоритет операции
    private byte getPrecedence(String token) {
        if (token.equals("+") || token.equals("-")) {//у + и - приоритет = 1, у остальных (* и /) приоритет = 2
            return 1;
        }
        return 2;
    }

    //преобразование входного выражения к выражению вида обратной польской записи алгоритмом "Сортировочная станция" Дейкстра
    public String sortingStation(String expression) {
        if (expression == null || expression.length() == 0)
            throw new IllegalStateException("Ввыражение не задано.");

        stackOperations.clear();
        stackRPN.clear();

        // приготовления
        expression = expression.replace(" ", "").replace("(-", "(0-")
                .replace(",-", ",0-");
        if (expression.charAt(0) == '-') {
            expression = "0" + expression;
        }
        // разбиение входного выражения на токены
        StringTokenizer stringTokenizer = new StringTokenizer(expression,
                OPERATORS + SEPARATOR + "()", true);

        // "Сортировочная станция"
        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken();
            if (isSeparator(token)) {
                while (!stackOperations.empty()
                        && !isOpenBracket(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
            } else if (isOpenBracket(token)) {
                stackOperations.push(token);
            } else if (isCloseBracket(token)) {
                while (!stackOperations.empty()
                        && !isOpenBracket(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.pop();
                if (!stackOperations.empty()
                        && isFunction(stackOperations.lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
            } else if (isNumber(token))
                stackRPN.push(token);

            else if (isOperator(token)) {
                while (!stackOperations.empty()
                        && isOperator(stackOperations.lastElement())
                        && getPrecedence(token) <= getPrecedence(stackOperations
                        .lastElement())) {
                    stackRPN.push(stackOperations.pop());
                }
                stackOperations.push(token);
            } else if (isFunction(token)) {
                stackOperations.push(token);
            } else throw new IllegalArgumentException("Синтаксическая ошибка! Проверьте выражение.");
        }
        while (!stackOperations.empty()) {
            stackRPN.push(stackOperations.pop());
        }


        StringBuilder result = new StringBuilder();
        while (!stackRPN.isEmpty())
            result.append(" ").append(stackRPN.remove(0));
        return result.toString();
    }

    //вычисление выражения, представленного в виде ОПН
    public BigDecimal calculateExpression(String expression) {
        String rpn = this.sortingStation(expression);
        StringTokenizer tokenizer = new StringTokenizer(rpn, " ");
        Stack<BigDecimal> stack = new Stack<BigDecimal>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (!OPERATORS.contains(token)) {
                stack.push(new BigDecimal(token));
            } else {
                BigDecimal operand2 = stack.pop();
                BigDecimal operand1 = stack.empty() ? BigDecimal.ZERO : stack.pop();
                if (token.equals("*")) {
                    stack.push(operand1.multiply(operand2));
                } else if (token.equals("/")) {
                    stack.push(operand1.divide(operand2, 3, RoundingMode.HALF_UP));
                } else if (token.equals("+")) {
                    stack.push(operand1.add(operand2));
                } else if (token.equals("-")) {
                    stack.push(operand1.subtract(operand2));
                }
            }
        }
        if (stack.size() != 1)
            throw new IllegalArgumentException("Синтаксическая ошибка в выражении");
        return stack.pop();
    }




}
