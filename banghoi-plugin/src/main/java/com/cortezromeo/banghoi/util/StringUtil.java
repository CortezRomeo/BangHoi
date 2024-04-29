package com.cortezromeo.banghoi.util;

import com.cortezromeo.banghoi.BangHoi;
import com.cortezromeo.banghoi.enums.ClanRank;
import com.cortezromeo.banghoi.file.MessageFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Stack;

public class StringUtil {

    public static double evaluate(String expression) {
        char[] tokens = expression.toCharArray();

        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ')
                continue;

            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuilder sb = new StringBuilder();
                while (i < tokens.length && (tokens[i] >= '0' && tokens[i] <= '9' || tokens[i] == '.')) {
                    sb.append(tokens[i++]);
                }
                values.push(Double.parseDouble(sb.toString()));
                i--;
            } else if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop();
            } else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!operators.empty() && hasPrecedence(tokens[i], operators.peek())) {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(tokens[i]);
            }
        }

        while (!operators.empty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')')
            return false;
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private static double applyOperation(char operator, double b, double a) {
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
        }
        return 0;
    }

    public static String getRankFormat(ClanRank clanRank) {
        return BangHoi.nms.addColor(BangHoi.plugin.getConfig().getString("bang-hoi-options.chuc-vu-format." + clanRank.toString()));
    }

    public static String timeFormat(long totalSeconds) {

        FileConfiguration messageFile = MessageFile.get();

        if (totalSeconds > 604800) {
            String str = messageFile.getString("time-format.wwddhhmmss");
            str = str.replace("%w%", String.valueOf(totalSeconds / 604800));
            str = str.replace("%d%", String.valueOf(totalSeconds % 604800 / 86400));
            str = str.replace("%h%", String.valueOf((totalSeconds % 86400) / 3600));
            str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
            str = str.replace("%s%", String.valueOf(totalSeconds % 60));

            return BangHoi.nms.addColor(str);
        }

        if (totalSeconds > 86400) {
            String str = messageFile.getString("time-format.ddhhmmss");
            str = str.replace("%d%", String.valueOf(totalSeconds / 86400));
            str = str.replace("%h%", String.valueOf((totalSeconds % 86400) / 3600));
            str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
            str = str.replace("%s%", String.valueOf(totalSeconds % 60));

            return BangHoi.nms.addColor(str);
        }

        if (totalSeconds > 3600) {

            String str = messageFile.getString("time-format.hhmmss");
            str = str.replace("%h%", String.valueOf(totalSeconds / 3600));
            str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
            str = str.replace("%s%", String.valueOf(totalSeconds % 60));

            return BangHoi.nms.addColor(str);
        }

        if (totalSeconds >= 60) {
            String str = messageFile.getString("time-format.mmss");
            str = str.replace("%m%", String.valueOf((totalSeconds % 3600) / 60));
            str = str.replace("%s%", String.valueOf(totalSeconds % 60));

            return BangHoi.nms.addColor(str);
        }

        return BangHoi.nms.addColor(messageFile.getString("time-format.ss").replace("%s%", String.valueOf(totalSeconds)));

    }
}
