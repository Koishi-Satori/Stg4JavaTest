package top.kkoishi.swing;

import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleText;
import javax.swing.*;
import javax.swing.text.AttributeSet;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;

public abstract class ConsoleComponent extends JPanel
        implements Scrollable, Accessible {

    public static void main (String[] args) {
        final JFrame sandbox = new JFrame("Sandbox");
        final ConsoleComponent consoleComponent = new ConsoleComponent() {
            @Override
            public StrBufferZone freshCommandBuffer () {
                return null;
            }

            @Override
            public StrBufferZone freshOutputBuffer () {
                return null;
            }
        };
        consoleComponent.setCurrent("Fuck Lzu!");
        consoleComponent.setSize(300, 300);
        sandbox.add(new JScrollPane(consoleComponent));
        sandbox.setSize(300, 300);
        sandbox.setVisible(true);
    }

    public ConsoleComponent () {

    }

    public class AccessiableContentImpl extends AccessibleAWTComponent
            implements AccessibleComponent, AccessibleText {

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

    protected interface StrBufferZone extends Iterable<String> {
        int size ();

        boolean empty ();
    }

    protected static final class StringBuf
            implements StrBufferZone {

        int size = 0;

        @Override
        public int size () {
            return size;
        }

        @Override
        public boolean empty () {
            return size == 0;
        }

        @Override
        public Iterator<String> iterator () {
            return null;
        }
    }

    protected int pos = 0;

    protected Font font = new Font("FreeStyle Script", Font.ITALIC, 14);

    protected int page = 1;

    protected int alignmentY = 5;

    protected Color color = Color.BLACK;

    protected StrBufferZone commandBuf = new StringBuf();

    protected StrBufferZone outputBuf = new StringBuf();

    protected StringBuilder current = new StringBuilder();

    public void setCurrent (String current) {
        this.current = new StringBuilder(current);
        repaint();
    }

    public abstract StrBufferZone freshCommandBuffer ();

    public abstract StrBufferZone freshOutputBuffer ();

    @Override
    public Dimension getPreferredScrollableViewportSize () {
        return super.getPreferredSize();
    }

    @Override
    public int getScrollableUnitIncrement (Rectangle visibleRect, int orientation, int direction) {
        return switch (orientation) {
            case SwingConstants.VERTICAL:
                yield visibleRect.height / 10;
            case SwingConstants.HORIZONTAL:
                yield visibleRect.width / 10;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
        };
    }

    @Override
    public int getScrollableBlockIncrement (Rectangle visibleRect, int orientation, int direction) {
        return switch (orientation) {
            case SwingConstants.VERTICAL:
                yield visibleRect.height;
            case SwingConstants.HORIZONTAL:
                yield visibleRect.width;
            default:
                throw new IllegalArgumentException("Invalid orientation: " + orientation);
        };
    }

    @Override
    public boolean getScrollableTracksViewportWidth () {
        Container parent = SwingUtilities.getUnwrappedParent(this);
        if (parent instanceof JViewport) {
            return parent.getHeight() > super.getPreferredSize().height;
        }
        super.repaint();
        return false;
    }

    @Override
    public boolean getScrollableTracksViewportHeight () {
        Container parent = SwingUtilities.getUnwrappedParent(this);
        if (parent instanceof JViewport) {
            return parent.getWidth() > super.getPreferredSize().width;
        }
        return false;
    }

    @Override
    public void paint (Graphics g) {
        super.paint(g);
        renderHistory(g.create());
        renderCurrent(g.create());
    }

    protected final void renderHistory (Graphics g) {

    }

    protected final void renderCurrent (Graphics g) {
        g.setColor(color);
        final int commandLines = commandBuf.size();
        final int outputLines = outputBuf.size();
        final int y = commandLines * font.getSize() + outputLines * font.getSize() + (commandLines + outputLines + 1) * alignmentY + font.getSize();
        System.out.println(current);
        g.drawString(current.toString(), 0, y);
    }
}
