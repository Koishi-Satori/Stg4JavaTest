package top.kkoishi.stg.swing;

import top.kkoishi.stg.env.GraphicsManager;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

/**
 * Process what will be displayed while loading.
 *
 * @author KKoishi_
 */
public final class LoadProcessor {

    private static final Random RANDOM = new Random(-1145141919810L);

    private static LoadProcessor instance = new LoadProcessor(GraphicsManager.instance.get());

    public static LoadProcessor getInstance (Graphics g) {
        if (!instance.available() || instance.g == null) {
            instance = new LoadProcessor(g);
        }
        return instance;
    }

    public static LoadProcessor getInstance () {
        if (!instance.available() || instance.g == null) {
            instance = new LoadProcessor(GraphicsManager.instance.get());
        }
        return instance;
    }

    private int x = 100, y = 100, lastFrames, count = 0;

    private final ArrayList<Image> backgrounds = new ArrayList<>();

    private final ArrayDeque<Image> images = new ArrayDeque<>(8);

    private int interval = 17;

    private final ArrayDeque<Image> buffer = new ArrayDeque<>(8);

    private Graphics g;

    public int lastFrames () {
        return lastFrames;
    }

    public void setLastFrames (int lastFrames) {
        this.lastFrames = lastFrames;
    }

    public void setX (int x) {
        this.x = x;
    }

    public void setY (int y) {
        this.y = y;
    }

    public int getX () {
        return x;
    }

    public int getY () {
        return y;
    }

    public void addBackground (Image background) {
        backgrounds.add(background);
    }

    public void add (Image frame) {
        images.add(frame);
    }

    private LoadProcessor (Graphics g) {
        this.g = g;
    }

    public void setG (Graphics g) {
        this.g = g;
    }

    public int interval () {
        return interval;
    }

    public void setInterval (int interval) {
        this.interval = interval;
    }

    public void dispose () {
        synchronized (this) {
            interval = -1;
            g = null;
            backgrounds.clear();
            images.clear();
            buffer.clear();
            count = 0;
            lastFrames = -1;
        }
    }

    public boolean available () {
        return count < lastFrames && interval > 0;
    }

    public void render () {
        synchronized (this) {
            if (!available()) {
                return;
            }
            ++count;
            //render background.
            g.drawImage(backgrounds.get(RANDOM.nextInt(0, backgrounds.size())), 7, 30, null);
            //render image.
            ImageRender:
            {
                if (images.isEmpty() && buffer.isEmpty()) {
                    break ImageRender;
                }
                if (images.isEmpty()) {
                    images.offerLast(buffer.removeFirst());
                }
                final Image renderImage = images.removeFirst();
                buffer.offerLast(renderImage);
                g.drawImage(renderImage, x, y, null);
            }
            //sleep the thread.
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
