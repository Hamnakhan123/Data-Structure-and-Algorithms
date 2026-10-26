import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Module 06 · Session 02 — Stack applications: brackets and expressions.
 *
 * Balanced-bracket checking, infix to postfix conversion with Dijkstra's
 * shunting-yard algorithm (printed step by step), postfix evaluation, prefix
 * evaluation, and a full infix calculator built from the two.
 *
 * Compile:  javac -d out ExpressionEvaluation.java
 * Run:      java -cp out ExpressionEvaluation
 *
 * Prepared by Hamna Zahid Khan, Junior Lecturer
 */
public class ExpressionEvaluation {

    /** Every closing bracket must match the most recent unmatched opening bracket. */
    static boolean isBalanced(String s) {
        Deque<Character> st = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') st.push(c);
            else if (c == ')' || c == ']' || c == '}') {
                if (st.isEmpty()) return false;                 // closer with no opener
                char open = st.pop();
                if ((c == ')' && open != '(') || (c == ']' && open != '[') || (c == '}' && open != '{'))
                    return false;                               // wrong type
            }
        }
        return st.isEmpty();                                    // leftover openers are unmatched
    }

    static int precedence(String op) {
        switch (op) {
            case "^": return 3;
            case "*": case "/": case "%": return 2;
            case "+": case "-": return 1;
            default: return 0;
        }
    }

    static boolean isOperator(String t) { return precedence(t) > 0; }

    /** Splits "12+3*(4-1)" into tokens [12, +, 3, *, (, 4, -, 1, )]. */
    static List<String> tokenize(String expr) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < expr.length(); ) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) { i++; continue; }
            if (Character.isDigit(c)) {
                int j = i;
                while (j < expr.length() && Character.isDigit(expr.charAt(j))) j++;
                out.add(expr.substring(i, j));
                i = j;
            } else { out.add(String.valueOf(c)); i++; }
        }
        return out;
    }

    /** Shunting-yard: operators wait on a stack until an operator of lower precedence arrives. */
    static List<String> toPostfix(String infix, boolean trace) {
        List<String> output = new ArrayList<>();
        Deque<String> ops = new ArrayDeque<>();
        if (trace) System.out.printf("  %-6s %-22s %s%n", "token", "operator stack (top first)", "output");
        for (String t : tokenize(infix)) {
            if (Character.isDigit(t.charAt(0))) output.add(t);
            else if (t.equals("(")) ops.push(t);
            else if (t.equals(")")) {
                while (!ops.peek().equals("(")) output.add(ops.pop());
                ops.pop();                                      // discard "("
            } else {                                            // operator
                while (!ops.isEmpty() && isOperator(ops.peek())
                        && (precedence(ops.peek()) > precedence(t)
                        || (precedence(ops.peek()) == precedence(t) && !t.equals("^")))) // ^ is right-associative
                    output.add(ops.pop());
                ops.push(t);
            }
            if (trace) System.out.printf("  %-6s %-22s %s%n", t, ops, String.join(" ", output));
        }
        while (!ops.isEmpty()) output.add(ops.pop());
        if (trace) System.out.printf("  %-6s %-22s %s%n", "end", ops, String.join(" ", output));
        return output;
    }

    static long apply(String op, long a, long b) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return a / b;
            case "%": return a % b;
            case "^": long r = 1; for (int i = 0; i < b; i++) r *= a; return r;
            default: throw new IllegalArgumentException("unknown operator " + op);
        }
    }

    /** Postfix evaluation: operands are pushed; an operator pops two (right operand first). */
    static long evalPostfix(List<String> tokens, boolean trace) {
        Deque<Long> st = new ArrayDeque<>();
        for (String t : tokens) {
            if (isOperator(t)) {
                long b = st.pop(), a = st.pop();               // order matters for - / ^
                st.push(apply(t, a, b));
            } else st.push(Long.parseLong(t));
            if (trace) System.out.printf("  %-4s stack (top first) %s%n", t, st);
        }
        if (st.size() != 1) throw new IllegalArgumentException("malformed expression");
        return st.pop();
    }

    /** Prefix (Polish) evaluation: scan right to left; an operator pops a (first) then b. */
    static long evalPrefix(String[] tokens) {
        Deque<Long> st = new ArrayDeque<>();
        for (int i = tokens.length - 1; i >= 0; i--) {
            String t = tokens[i];
            if (isOperator(t)) {
                long a = st.pop(), b = st.pop();
                st.push(apply(t, a, b));
            } else st.push(Long.parseLong(t));
        }
        return st.pop();
    }

    static long calculate(String infix) { return evalPostfix(toPostfix(infix, false), false); }

    public static void main(String[] args) {
        System.out.println("=== balanced brackets ===");
        for (String s : new String[]{"{[()()]}", "([)]", "((a+b)*c", "int[] a = {1, (2)};", "]["})
            System.out.printf("  %-22s -> %s%n", s, isBalanced(s));

        String expr = "3+4*2/(1-5)^2^3";
        System.out.println("\n=== infix -> postfix (shunting yard): " + expr + " ===");
        List<String> post = toPostfix(expr, true);
        System.out.println("postfix: " + String.join(" ", post));

        String e2 = "(12+3)*4-10/2";
        List<String> p2 = toPostfix(e2, false);
        System.out.println("\n=== evaluate postfix of " + e2 + "  ->  " + String.join(" ", p2) + " ===");
        System.out.println("result = " + evalPostfix(p2, true));

        System.out.println("\nprefix  \"- * + 12 3 4 / 10 2\" = " + evalPrefix("- * + 12 3 4 / 10 2".split(" ")));
        System.out.println("calculate(\"2^3^2\")       = " + calculate("2^3^2") + "  (right-associative: 2^9)");
        System.out.println("calculate(\"100-20-30\")   = " + calculate("100-20-30") + "  (left-associative)");
        System.out.println("calculate(\"7*(8+2)%9\")   = " + calculate("7*(8+2)%9"));
    }
}
