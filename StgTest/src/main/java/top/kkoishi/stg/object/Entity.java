package top.kkoishi.stg.object;

import top.kkoishi.stg.env.GraphicsManager;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author KKoishi_
 */
public abstract class Entity implements RenderAccess {

    /**
     * The identify of the entity instance.
     */
    protected final long uuid;

    /**
     * The name of the entity instance.
     */
    protected String name;

    /**
     * The texture of the entity instance.
     */
    protected BufferedImage image;

    /**
     * The centre point of the entity instance,
     */
    protected Point pos = new Point(0, 0);

    /**
     * Protected constructor of the entity class.
     *
     * @param uuid  uuid.
     * @param name  name.
     * @param image texture.
     */
    protected Entity (long uuid, String name, BufferedImage image) {
        this.uuid = uuid;
        this.name = name;
        this.image = image;
    }

    /**
     * Change the name field.
     *
     * @param name new name;
     */
    public void setName (String name) {
        this.name = name;
    }

    /**
     * Load the new texture and get the old one.(Maybe it is null)
     *
     * @param image new texture.
     * @return old texture
     */
    public BufferedImage loadImage (BufferedImage image) {
        final var oldVal = this.image;
        this.image = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        final Graphics2D g2d = this.image.createGraphics();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.DST, 1.0f));
        g2d.drawImage(image, null, 0, 0);
        g2d.dispose();
        System.out.println("load image to:" + image);
        render();
        return oldVal;
    }

    /**
     * Set the centre point.
     *
     * @param x x.
     * @param y y.
     */
    public void setPos (int x, int y) {
        this.pos.x = x;
        this.pos.y = y;
    }

    @Override
    public long uuid () {
        return uuid;
    }

    @Override
    public String name () {
        return name;
    }

    /**
     * The render method, and the render thread will invoke this method to render an entity instance.
     */
    @Override
    public void render () {
        render0(GraphicsManager.instance.get());
    }

    @Override
    public void prepareRepaint () {
        repaint0(GraphicsManager.instance.get());
    }

    @Override
    public void clear () {
        image = null;
        pos = null;
        name = null;
    }

    /**
     * The actual executor of repaint method, and the default Graphics instance is {@code GraphicsManager.instance.get()}
     *
     * @param g Graphics instance.
     * @see GraphicsManager
     */
    protected abstract void repaint0 (Graphics g);

    /**
     * The actual executor of render method, and the default Graphics instance is {@code GraphicsManager.instance.get()}
     *
     * @param g Graphics instance.
     * @see GraphicsManager
     */
    protected abstract void render0 (Graphics g);
}
