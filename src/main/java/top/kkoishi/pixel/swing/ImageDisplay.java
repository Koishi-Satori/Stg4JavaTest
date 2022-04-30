package top.kkoishi.pixel.swing;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

/**
 * @author KKoishi_
 */
public final class ImageDisplay extends JPanel
        implements MouseWheelListener {

    public static void main (String[] args) throws IOException {
        final JFrame f = new JFrame("Sandbox");
        f.setSize(400, 437);
        final ImageDisplay display = new ImageDisplay(ImageIO.read(new File("./background.jpg"))
                , 400, 400);
        display.setSize(400, 400);
        f.add(display);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
        display.render();
    }

    private static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();

    private final BufferedImage image;

    int width;

    int height;

    private float scale = 1.00f;

    public ImageDisplay (BufferedImage image, int width, int height) {
        super(new BorderLayout());
        this.image = image;
        this.width = width;
        this.height = height;
        setSize(width, height);
    }

    public BufferedImage getImage () {
        return image;
    }

    public void render () {
        drawImage(getGraphics());
    }

    @Override
    public void mouseWheelMoved (MouseWheelEvent e) {

    }

    private strictfp void drawImage (Graphics g) {
        final int imgWidth = image.getWidth();
        final int imgHeight = image.getHeight();
        final int scaledWidth = width;
        final int scaledHeight = height;
        System.out.println("imgWidth:" + imgWidth + ", imgHeight:" + imgHeight + "|panel:" + width + ", " + height);
        if (imgWidth <= scaledWidth && imgHeight <= scaledHeight) {
            System.out.println("1");
            g.drawImage(getScaledImage(scaledWidth, scaledHeight),
                    (width - scaledWidth) / 2, (height - scaledHeight) / 2, null);
        } else if (imgWidth <= scaledWidth) {
            System.out.println("2");
            final int realWidth = height * imgWidth / imgHeight;
            g.drawImage(getScaledImage(realWidth, height),
                    (width - realWidth) / 2, 0, null);
        } else if (imgHeight <= scaledHeight) {
            System.out.println("succ");
            final int realHeight = width * imgWidth / imgHeight;
            g.drawImage(getScaledImage(width, realHeight),
                    0, (height - realHeight) / 2, null);
        } else {
            System.out.println("3");
            final int realWidth = height * imgWidth / imgHeight;
            final int realHeight = width * imgWidth / imgHeight;
            g.drawImage(getScaledImage(realWidth, realHeight),
                    (width - realWidth) / 2, (height - realHeight) / 2, null);
        }
    }

    private BufferedImage getScaledImage (int width, int height) {
        final BufferedImage scaledItem = new BufferedImage(width, height, image.getType());
        final Graphics g = scaledItem.createGraphics();
        g.drawImage(TOOLKIT.createImage(new FilteredImageSource(image.getSource(),
                new AreaAveragingScaleFilter(width, height))), 0, 0, null);
        return scaledItem;
    }

    @Override
    public void paint (Graphics g) {
        super.paint(g);
        drawImage(g);
    }
}
