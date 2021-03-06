package main;

import сalculationExpression.CalculationExpression;

/**
 * Задание:
 * Есть формула с цифрами, операциями +-*(/) и скобками ( ). Нужно написать
 * программу, которая ее вычисляет.  Формула может быть большой.
 * Дополнительная часть: предусмотреть (архитектурно) возможность доработки –
 * возможность вводить функции, вводить параметры для расчета формулы.
 * <p/>
 * Решил задачу, преобразуя выражение из инфиксной нотации в обратную польскую нотацию (ОПН) по алгоритму "Сортировочная
 * станция" Эдскера Дейкстры. Отличительной особенностью обратной польской нотации является то, что все
 * аргументы (или операнды) расположены перед операцией. Это позволяет избавиться от необходимости использования
 * скобок. Например, выражение, записаное в инфиксной нотации как 3 * (4 + 7), будет выглядеть как 3 4 7 + *
 * в ОПН.
 */


public class Main {
    public static void main(String[] args) {
        CalculationExpression cE = new CalculationExpression();
        String expression = "-3 + 4.4 * 2.1 / (1 - 5) - 2";
        System.out.println("Выражение:   " + expression);
        String rpn = cE.sortingStation(expression);
        System.out.println("Выражение в ОПН:  " + rpn);
        System.out.println("Результат: " + cE.calculateExpression(expression));
    }
}
