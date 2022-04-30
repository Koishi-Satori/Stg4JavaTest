package top.kkoishi.stg.swing;

import top.kkoishi.stg.Main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.stream.Stream;

public final class TitlePanel extends JPanel implements KeyListener {
    private final BufferedImage background;

    private final Font font;

    private final ArrayList<JLabel> titles = new ArrayList<>(6);

    private int pos = 0;

    private final Border border;

    public TitlePanel (BufferedImage background, Font font) {
        super(new GridLayout(6, 1));
        this.background = background;
        this.font = font;
        Stream.of("Start Game", "Replay", "achievement", "Play Data", "Manuel", "Exit").map(this::getLabel).peek(titles::add).forEach(this::add);
        border = titles.get(0).getBorder();
        select();
        setFocusable(true);
        addKeyListener(this);
    }

    public void select () {
        titles.forEach(title -> title.setBorder(border));
        final JLabel selected = titles.get(pos);
        selected.setBorder(new LineBorder(Color.GRAY, 2, true));
        repaint();
    }

    @Override
    public void paint (Graphics g) {
        g.drawImage(background, 0, 0, null);
        super.paint(g);
    }

    private JLabel getLabel (String text) {
        final JLabel title = new JLabel(text, JLabel.CENTER);
        title.setFont(font);
        title.setOpaque(true);
        return title;
    }

    @Override
    public void keyTyped (KeyEvent e) {

    }

    @SuppressWarnings("all")
    public void execute () {
        switch (pos) {
            case 0: {
                removeKeyListener(this);
                setFocusable(false);
                Main.main();
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void keyPressed (KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP: {
                ++pos;
                select();
                break;
            }
            case KeyEvent.VK_DOWN: {
                --pos;
                select();
                break;
            }
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_Z: {
                execute();
                break;
            }
            default: {
                break;
            }
        }
    }

    @Override
    public void keyReleased (KeyEvent e) {

    }
}
