package interpretator.editor;

/**
 *
 * @author alex
 */
public class DocumentContext {

    private final String text;

    public DocumentContext(String text) {
        this.text = text;
    }

    public CharSequence getText() {
        return text;
    }

    public int[] getRowCol(int offset) {
        int row = 1;
        int col = 1;
        for (int i = 0; i <= offset && i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\n':
                    row++;
                    col = 1;
                    break;
                default:
                    col++;
                    break;
            }
        }
        return new int[]{row, col};
    }

    public String getLine(int line) {
        int row = 1;
        int lineStart = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (text.charAt(i) == '\n') {
                if (row == line) {
                    return text.substring(lineStart, i);
                }
                row++;
                lineStart = i + 1;
            }
        }
        if (row == line) {
            return text.substring(lineStart, text.length());
        }
        return "";
    }
}
