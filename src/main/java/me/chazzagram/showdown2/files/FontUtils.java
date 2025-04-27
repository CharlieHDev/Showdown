package me.chazzagram.showdown2.files;

import java.util.HashMap;
import java.util.Map;

public class FontUtils {
    private static final Map<Character, Integer> CHAR_WIDTHS = new HashMap<>();

    static {
        // Widths based on default Minecraft font
        String small = "!.,:;i| ";
        String medium = "'`l";
        String wide = "t[]{}<>/\\";
        String wider = "fk()";
        String widest = "mw@";

        for (char c : small.toCharArray()) CHAR_WIDTHS.put(c, 2);
        for (char c : medium.toCharArray()) CHAR_WIDTHS.put(c, 3);
        for (char c : wide.toCharArray()) CHAR_WIDTHS.put(c, 4);
        for (char c : wider.toCharArray()) CHAR_WIDTHS.put(c, 5);
        for (char c : widest.toCharArray()) CHAR_WIDTHS.put(c, 6);

        // Default for letters and numbers
        for (char c = 'a'; c <= 'z'; c++) CHAR_WIDTHS.putIfAbsent(c, 5);
        for (char c = 'A'; c <= 'Z'; c++) CHAR_WIDTHS.putIfAbsent(c, 5);
        for (char c = '0'; c <= '9'; c++) CHAR_WIDTHS.putIfAbsent(c, 5);

        CHAR_WIDTHS.putIfAbsent(' ', 4);
        CHAR_WIDTHS.putIfAbsent('\u00A7', 0); // section symbol (formatting code)
    }

    public static int getStringWidth(String text) {
        int width = 0;
        boolean bold = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\u00A7' && i + 1 < text.length()) {
                char code = text.charAt(i + 1);
                if (code == 'l' || code == 'L') {
                    bold = true;
                } else if (code == 'r' || code == 'R') {
                    bold = false;
                }
                i++; // Skip formatting code
                continue;
            }

            int charWidth = CHAR_WIDTHS.getOrDefault(c, 5);
            if (bold && charWidth > 0) {
                charWidth += 1;
            }
            width += charWidth + 1; // +1 for space between chars
        }

        return width;
    }
}

