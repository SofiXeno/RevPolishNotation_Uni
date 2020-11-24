/**
* @author Xenofontova Sofia 24.11.2020
 *
 * Розглядаємо арифметичні вирази, які  ми розглядали, коли робили вправи до перекладу в ПОЛІЗ,
 * тобто використовуємо арифметичні операції, круглі дужки, індексні вирази  та функції.
 * Зробити програму, яка перекладає вхідний рядок у вихідний (ПОЛІЗ).
 * Вибір мови програмування за студентом.
 * Для випадку, коли вхідний рядок містить арифметичні вирази з числами (і,  можливо,  одну  функцію
 * типу  sin()), вирахувати значення  виразу, використовуючи вихідний рядок, обчислення продемонструвати крок за кроком
 **/


import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class Poliz {


/**
 * Дані для тестування
 *  1. ( 45.5 - ( 8 + 9 / 2 ) ) * 2 ^ 2
 *  2. cos ( 1 ) + sin ( -1 )
 *  3. ( sign ( 6 ) * ( sqrt ( 9 ) - 1 ) ) / ( tg ( 2 ^ 0 ) - 1 )
 */

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        Poliz p = new Poliz();
        p.eval(p.poliz(s));


    }

    private boolean isDigit(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }


    private boolean isOperator(String t) {
        return (t.equals("+")|| t.equals("-")|| t.equals("*")|| t.equals("/") || t.equals("^"));

    }

    private boolean isFunction(String t) {
        return (t.equals("sin")|| t.equals("cos")|| t.equals("tg")|| t.equals("sqrt") || t.equals("sign"));
    }

    private Integer priority(String s) {
        if (s.equals("+")|| s.equals("-")) return 7;
        else if (s.equals("*")|| s.equals("/")) return 8;
             else if (s.equals("sin")|| s.equals("cos")|| s.equals("tg")|| s.equals("sqrt") || s.equals("sign")) return 10;
                  else if (s.equals("^")) return 9;
                       else return 0;
    }



    public Stack<String> poliz(String expression) {
        System.out.println("Зчитую стрічку: "  + expression);
        Stack<String> res = new Stack<>();
        Stack<String> operators = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(expression);
        while (tokenizer.hasMoreTokens()) {
            processToken(tokenizer.nextToken(), res, operators);
        }
        while (!operators.empty()) {
            res.push(operators.pop());
        }
        System.out.println("\nПОЛІЗ: " + res.toString() + "\n");
        return res;

    }

    private void processToken(String token, Stack<String> res, Stack<String> operators) {
        if (isDigit(token)) {
            res.push(token);
        } else if (isFunction(token) || token.equals("(")) {
            operators.push(token);
        } else if (isOperator(token)) {
            while (!operators.empty() && !operators.peek().equals("(") && (priority(operators.peek()) >= priority(token))) {
                res.push(operators.pop());
            }
            operators.push(token);
        } else if (token.equals(")")) {
            while (!operators.empty() && !operators.peek().equals("(")) {
                res.push(operators.pop());
            }
            if (!operators.empty() && operators.peek().equals("(")) {
                operators.pop();
            } else {
                throw new Error("Невідповідність дужок");
            }
        } else {
            throw new Error("Невідома функція чи оператор: " + token);
        }
        System.out.println("\nЛексема \"" + token + "\" оброблена.");
        System.out.print("Стек операторів/функцій: " + operators.toString() + " ");

        System.out.print("Стек для відповіді: " + res.toString() + " ");

    }



    double processOperator(double x1, double x2, String operator){
        if (operator.equals("+")) return x1+x2;
        else if (operator.equals("-")) return x1-x2;
             else if (operator.equals("*")) return x1*x2;
                  else if (operator.equals("/")) return x1/x2;
                       else return Math.pow(x1,x2);
    }

    double processFunction(double x, String func){
        if (func.equals("sin")) return Math.sin(x);
        else if (func.equals("cos")) return Math.cos(x);
             else if (func.equals("tg")) return Math.tan(x);
                  else if (func.equals("sqrt")) return Math.sqrt(x);
                       else return Math.signum(x);
    }




    public void eval(Iterable<String> postfix) {
        System.out.println("Починаємо обчислення виразу: " + postfix);
        Stack<Double> result = new Stack<>();
        for(String current : postfix){
            if (isDigit(current)) {
                System.out.print("Константа " + current + " додається в результуючий стек. ");
                result.push(Double.parseDouble(current));

            } else if (isOperator(current)) {
                double a1 = result.pop(), a2 = result.pop();
                double res = processOperator(a2,a1,current);
                result.push(res);
                System.out.println("Дістаємо зі стеку операторів/функцій оператор: \"" + current + "\" і виконуємо: " + a2 + " " + current + " " + a1 + " = " + res);

            }else if (isFunction(current)){
                double x = result.pop();
                double res = processFunction(x,current);
                result.push(res);
                System.out.println("Дістаємо зі стеку операторів/функцій функцію: \"" + current + "\" і виконуємо: " + current + '(' + x + ") = " + res);
            }

            System.out.println("Поточний стан стеку відповіді: " + result.toString() + " " );
        }
        System.out.println("Результат обчислення ПОЛІЗу : " + result.peek());
    }






}
