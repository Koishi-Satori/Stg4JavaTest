package top.kkoishi.pixel.swing;

import top.kkoishi.swing.JVMStateDisplay;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class Board extends JPanel {

    private final JVMStateDisplay vmStateDisplay = new JVMStateDisplay();

    private final BufferedImage image;

    private final JPanel tools = new JPanel();

    private final JPanel state = new JPanel();

    private final JPanel config = new JPanel();

    public Board (BufferedImage image) {
        super(new BorderLayout());
        this.image = image;
    }
}
