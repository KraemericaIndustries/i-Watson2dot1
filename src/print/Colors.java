package print;

public class Colors {
    public static final class Ansi {

        // ===== Reset =====
        public static final String RESET = "\u001B[0m";

        // ===== Standard Colors =====
        public static final String BLACK   = "\u001B[30m";
        public static final String RED     = "\u001B[31m";
        public static final String GREEN   = "\u001B[32m";
        public static final String YELLOW  = "\u001B[33m";
        public static final String BLUE    = "\u001B[34m";
        public static final String PURPLE  = "\u001B[35m";
        public static final String CYAN    = "\u001B[36m";
        public static final String WHITE   = "\u001B[37m";

        // ===== Bright Colors =====
        public static final String BRIGHT_BLACK   = "\u001B[90m";
        public static final String BRIGHT_RED     = "\u001B[91m";
        public static final String BRIGHT_GREEN   = "\u001B[92m";
        public static final String BRIGHT_YELLOW  = "\u001B[93m";
        public static final String BRIGHT_BLUE    = "\u001B[94m";
        public static final String BRIGHT_PURPLE  = "\u001B[95m";
        public static final String BRIGHT_CYAN    = "\u001B[96m";
        public static final String BRIGHT_WHITE   = "\u001B[97m";

        // ===== Background Colors =====
        public static final String BG_BLACK   = "\u001B[40m";
        public static final String BG_RED     = "\u001B[41m";
        public static final String BG_GREEN   = "\u001B[42m";
        public static final String BG_YELLOW  = "\u001B[43m";
        public static final String BG_BLUE    = "\u001B[44m";
        public static final String BG_PURPLE  = "\u001B[45m";
        public static final String BG_CYAN    = "\u001B[46m";
        public static final String BG_WHITE   = "\u001B[47m";

        // ===== Bright Backgrounds =====
        public static final String BG_BRIGHT_BLACK   = "\u001B[100m";
        public static final String BG_BRIGHT_RED     = "\u001B[101m";
        public static final String BG_BRIGHT_GREEN   = "\u001B[102m";
        public static final String BG_BRIGHT_YELLOW  = "\u001B[103m";
        public static final String BG_BRIGHT_BLUE    = "\u001B[104m";
        public static final String BG_BRIGHT_PURPLE  = "\u001B[105m";
        public static final String BG_BRIGHT_CYAN    = "\u001B[106m";
        public static final String BG_BRIGHT_WHITE   = "\u001B[107m";

        // ===== 256-Color Foreground =====
        public static String color256(int code) {
            return "\u001B[38;5;" + code + "m";
        }

        // ===== 256-Color Background =====
        public static String bg256(int code) {
            return "\u001B[48;5;" + code + "m";
        }

        // ===== TrueColor (24-bit RGB) Foreground =====
        public static String rgb(int r, int g, int b) {
            return "\u001B[38;2;" + r + ";" + g + ";" + b + "m";
        }

        // ===== TrueColor (24-bit RGB) Background =====
        public static String bgRgb(int r, int g, int b) {
            return "\u001B[48;2;" + r + ";" + g + ";" + b + "m";
        }

        // ===== Convenience Wrapper =====
        public static String paint(String color, String text) {
            return color + text + RESET;
        }

        private Ansi() {} // prevent instantiation

    }
}
