import java.util.Scanner;
import java.util.Stack;

public class ThreeAddressCodeGenerator {

    static int tempCount = 1;

    // Function to return precedence of operators
    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
        }
        return -1;
    }

    // A utility method to apply an operator 'op' on operands 'a' and 'b' 
    private static String applyOp(char op, String a, String b) {
        String result = "t" + tempCount++;
        System.out.println(result + " = " + a + " " + op + " " + b);
        return result;
    }
    

    // Function that converts infix expression to three address code
    public static void infixToTAC(String exp) {

        // stack to store operators.
        Stack<Character> operators = new Stack<>();

        // stack to store values or operands
        Stack<String> values = new Stack<>();

        for (int i = 0; i < exp.length(); i++) {
            char c = exp.charAt(i);

            // Skip whitespaces
            if (c == ' ')
                continue;

            // Push operands to values stack
            if (Character.isLetterOrDigit(c)) {
                StringBuilder sbuf = new StringBuilder();
                while (i < exp.length() && (Character.isLetterOrDigit(exp.charAt(i)) || exp.charAt(i) == '_')) {
                    sbuf.append(exp.charAt(i++));
                }
                i--;
                values.push(sbuf.toString());
            }

            // Handle opening parentheses
            else if (c == '(')
                operators.push(c);

            // Handle closing parentheses
            else if (c == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    String val2 = values.pop();
                    String val1 = values.pop();
                    char op = operators.pop();
                    values.push(applyOp(op, val1, val2));
                }
                operators.pop(); // Pop '('
            }

            // Handle operators
            else {
                while (!operators.empty() && precedence(operators.peek()) >= precedence(c)) {
                    String val2 = values.pop();
                    String val1 = values.pop();
                    char op = operators.pop();
                    values.push(applyOp(op, val1, val2));
                }
                operators.push(c);
            }
        }

        // Process remaining operators
        while (!operators.empty()) {
            String val2 = values.pop();
            String val1 = values.pop();
            char op = operators.pop();
            values.push(applyOp(op, val1, val2));
        }

        // Top of 'values' contains result, this is the final result
        System.out.println("Result = " + values.pop());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter an infix expression: ");
        String exp = scanner.nextLine(); // Read user input
        System.out.println("Infix Expression: " + exp);
        System.out.println("Generated Three Address Code:");
        infixToTAC(exp);
        scanner.close();
    }
}
