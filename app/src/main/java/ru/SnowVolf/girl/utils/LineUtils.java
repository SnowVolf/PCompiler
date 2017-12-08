package ru.SnowVolf.girl.utils;

import android.text.Layout;
import android.text.TextUtils;
import android.widget.ScrollView;

/**
 * Created by Snow Volf on 07.12.2017, 22:58
 */
public class LineUtils {
    private boolean[] toCountLinesArray;
    private int[] realLines;

    public boolean[] getGoodLines() {
        return toCountLinesArray;
    }

    public int[] getRealLines() {
        return realLines;
    }

    public static int getYAtLine(ScrollView scrollView, int lineCount, int line) {
        return scrollView.getChildAt(0).getHeight() / lineCount * line;
    }

    public static int getFirstVisibleLine(ScrollView scrollView, int childHeight, int lineCount) throws ArithmeticException {
        int line = (scrollView.getScrollY() * lineCount) / childHeight;
        if (line < 0) line = 0;
        return line;
    }

    public static int getLastVisibleLine(ScrollView scrollView, int childHeight, int lineCount, int deviceHeight) {
        int line = ((scrollView.getScrollY() + deviceHeight) * lineCount) / childHeight;
        if (line > lineCount) line = lineCount;
        return line;
    }

    public void updateHasNewLineArray(int startingLine, int lineCount, Layout layout, String text) {

        boolean[] hasNewLineArray = new boolean[lineCount];
        toCountLinesArray = new boolean[lineCount];
        realLines = new int[lineCount];

        if (TextUtils.isEmpty(text)) {
            toCountLinesArray[0] = false;
            realLines[0] = 1;
            return;
        }

        int i;

        // for every line on the edittext
        for (i = 0; i < lineCount; i++) {
            // check if this line contains "\n"
            //hasNewLineArray[i] = text.substring(layout.getLineStart(i), layout.getLineEnd(i)).endsWith("\n");
            hasNewLineArray[i] = text.charAt(layout.getLineEnd(i) - 1) == '\n';
            // if true
            if (hasNewLineArray[i]) {
                int j = i - 1;
                while (j > -1 && !hasNewLineArray[j]) {
                    j--;
                }
                toCountLinesArray[j + 1] = true;

            }
        }

        toCountLinesArray[lineCount - 1] = true;

        int realLine = startingLine; // the first line is not 0, is 1. We start counting from 1

        for (i = 0; i < toCountLinesArray.length; i++) {
            if (toCountLinesArray[i]) {
                realLine++;
            }
            realLines[i] = realLine;
        }
    }

    /**
     * Gets the line from the index of the letter in the text
     *
     * @param index
     * @param lineCount
     * @param layout
     * @return
     */
    public static int getLineFromIndex(int index, int lineCount, Layout layout) {
        int line;
        int currentIndex = 0;

        for (line = 0; line < lineCount; line++) {
            currentIndex += layout.getLineEnd(line) - layout.getLineStart(line);
            if (currentIndex > index) {
                break;
            }
        }

        return line;
    }

    public int firstReadLine() {
        return realLines[0];
    }

    public int lastReadLine() {
        return realLines[realLines.length - 1];
    }

    public int fakeLineFromRealLine(int realLine) {
        int i;
        int fakeLine = 0;
        for (i = 0; i < realLines.length; i++) {
            if (realLine == realLines[i]) {
                fakeLine = i;
                break;
            }
        }
        return fakeLine;
    }
}


