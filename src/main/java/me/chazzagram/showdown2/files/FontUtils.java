package me.chazzagram.showdown2.files;

import java.util.HashMap;
import java.util.Map;

public class FontUtils {
    private static final Map<Character, Integer> CHAR_WIDTHS = new HashMap<>();

    static {
        // Widths based on default Minecraft font
        // Small characters (2 pixels)

        String smallest = "!.,:;i| ";
        for (char c : smallest.toCharArray()) CHAR_WIDTHS.put(c, 1);

        // Medium characters (3 pixels)
        String medium = "`l"; // includes uppercase I, l, i, quotes
        for (char c : medium.toCharArray()) CHAR_WIDTHS.put(c, 2);

        // Wide characters (4 pixels)
        String wide = "t[]{}<>/\\?~^*-=+I\"";
        for (char c : wide.toCharArray()) CHAR_WIDTHS.put(c, 3);

        // Wider characters (5 pixels)
        String wider = "fk()rJ$&%#";
        for (char c : wider.toCharArray()) CHAR_WIDTHS.put(c, 4);

        // Widest characters (6 pixels)
        String widest = "_abcdeghjmnopqrsuvwxyz@ABCDEFGHJKLMNOPQRSTUVWXYZᴀʙᴄᴅᴇғɢʜɪᴊᴋʟᴍɴᴏᴘʀsᴛᴜᴠᴡxʏᴢ";
        for (char c : widest.toCharArray()) CHAR_WIDTHS.put(c, 5);

        // Numbers 0-9
        for (char c = '0'; c <= '9'; c++) CHAR_WIDTHS.putIfAbsent(c, 5);
    }

    public static int getStringWidth(String text) {
        int width = 0;
        boolean bold = false;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (c == '\u00A7' && i + 1 < text.length()) {
                char code = text.charAt(i + 1);
                bold = code == 'l' || code == 'L';
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

