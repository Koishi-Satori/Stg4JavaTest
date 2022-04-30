package top.kkoishi.swing;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleText;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class TextComponent extends JTextComponent implements Scrollable, Accessible {

    int pos;
    Point oldPoint;

    @SuppressWarnings("all")
    public class AccessibleKoishiTextComponent extends AccessibleJComponent
            implements AccessibleText {

        @Override
        public int getIndexAtPoint (Point p) {
            return 0;
        }

        @Override
        public Rectangle getCharacterBounds (int i) {
            return null;
        }

        @Override
        public int getCharCount () {
            return 0;
        }

        @Override
        public int getCaretPosition () {
            return 0;
        }

        @Override
        public String getAtIndex (int part, int index) {
            return null;
        }

        @Override
        public String getAfterIndex (int part, int index) {
            return null;
        }

        @Override
        public String getBeforeIndex (int part, int index) {
            return null;
        }

        @Override
        public AttributeSet getCharacterAttribute (int i) {
            return null;
        }

        @Override
        public int getSelectionStart () {
            return 0;
        }

        @Override
        public int getSelectionEnd () {
            return 0;
        }

        @Override
        public String getSelectedText () {
            return null;
        }
    }

    @Override
    public AccessibleContext getAccessibleContext () {
        return super.getAccessibleContext();
    }

    @Override
    public Dimension getPreferredScrollableViewportSize () {
        return null;
    }

    @Override
    public int getScrollableUnitIncrement (Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public int getScrollableBlockIncrement (Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public boolean getScrollableTracksViewportWidth () {
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight () {
        return false;
    }
}
