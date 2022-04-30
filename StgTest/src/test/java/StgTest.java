import top.kkoishi.concurrent.DefaultThreadFactory;
import top.kkoishi.game.env.CFPSMaker;
import top.kkoishi.proc.ini.INIPropertiesLoader;
import top.kkoishi.proc.ini.Section;
import top.kkoishi.proc.property.BuildFailedException;
import top.kkoishi.proc.property.LoaderException;
import top.kkoishi.proc.property.TokenizeException;
import top.kkoishi.stg.env.GraphicsManager;
import top.kkoishi.stg.env.Stage;
import top.kkoishi.stg.env.StageManager;
import top.kkoishi.stg.object.Entire;
import top.kkoishi.stg.object.Player;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Main class.
 *
 * @author KKoishi_
 */
public final class StgTest extends JFrame implements Runnable {

    public static KeyAdapter getAdapter (Consumer<Integer> action, boolean release) {
        return release ? new KeyAdapter() {
            @Override
            public void keyReleased (KeyEvent e) {
                action.accept(e.getKeyCode());
            }
        }
                :
                new KeyAdapter() {
                    @Override
                    public void keyPressed (KeyEvent e) {
                        action.accept(e.getKeyCode());
                    }
                };
    }

    /**
     * Players' properties.
     */
    private static final File INDEX_PLAYERS = new File("./data/index/players.ini");

    private static final INIPropertiesLoader INI_PLAYERS = new INIPropertiesLoader();

    private static final CFPSMaker FPS_MAKER = new CFPSMaker();

    /**
     * Use Double Buffering to make sure the fluency of the rendering process.
     */
    public static BufferedImage buf = new BufferedImage(1200, 720, BufferedImage.TYPE_INT_ARGB);

    /**
     * The lock object.
     */
    private static final Object LOCK = new Object();

    /**
     * Display frame.
     */
    static StgTest window = new StgTest();

    /**
     * Hakuri reimu.
     */
    static Player reimu = new Player("Hakuri Reimu");

    /**
     * The player selected, actually it is the pointer to one element in the player list.
     */
    static Player select;

    /**
     * Calculate and render the bullet of selected player.
     */
    static Runnable shootTask;

    /**
     * Thread pool.
     */
    static ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(2, new DefaultThreadFactory());

    static {
        FPS_MAKER.setNowFPS(System.nanoTime());
        try {
            System.out.println("Loading properties:" + INDEX_PLAYERS.getCanonicalPath());
            INI_PLAYERS.load(INDEX_PLAYERS, StandardCharsets.UTF_8);
            System.out.println("Success to load the properties:" + INI_PLAYERS);
        } catch (IOException | TokenizeException | BuildFailedException | LoaderException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            System.exit(1919810);
        }
        GraphicsManager.initialInstance(buf.createGraphics());
        try {
            areaInitial();
            playerInitial(reimu, "reimu");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            System.exit(1919810);
        }
    }

    private static void areaInitial () throws IOException {
        window.setSize(1224, 754);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setFocusable(true);
        StageManager.areaWidth = buf.getWidth() * 3 / 5;
        StageManager.areaHeight = buf.getHeight() * 9 / 10;
        StageManager.setStage(0, new Stage(ImageIO.read(new File("./data/stage/st1/background.jpg"))) {

        });
    }

    @SuppressWarnings("all")
    private static void playerInitial (Player p, String sectionName) throws IOException, IllegalAccessException, NoSuchFieldException {
        System.out.println("Initialing player:" + p);
        final Section section = INI_PLAYERS.get(sectionName);
        if (section == null) {
            throw new NullPointerException("The section[" + sectionName + "] does not exist.");
        }
        final Class<Player> clz = Player.class;
        File src;
        System.out.println("Load class:" + clz + " from " + clz.getPackageName());
        for (final Section.INIEntry entry : section.entries) {
            Field f;
            try {
                f = clz.getDeclaredField(entry.getKey());
            } catch (NoSuchFieldException e) {
                f = Entire.class.getDeclaredField(entry.getKey());
            }
            f.setAccessible(true);
            src = new File(entry.getValue());
            System.out.println("Loading resouces:" + f + " for " + p.name());
            f.set(p, ImageIO.read(src));
            System.out.println("Sussess to load:" + src.getCanonicalPath());
        }
//        ImageIO.write(ImageIO.read(new File("./data/player/reimu/reimu_bullet.png")).getSubimage(0, 0, 10, 64), "png", new File("./reimu_bullet.png"));
    }

    public static void main (String[] args) {
        window.setVisible(true);
        pool.scheduleAtFixedRate(window, 0, 17, TimeUnit.MILLISECONDS);
        pool.scheduleAtFixedRate(() -> {
            if (select != null) {
                select.shot();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
        selectPlayer();
    }

    private static void selectPlayer () {
        synchronized (LOCK) {
            if (select != null) {
                window.removeKeyListener(select);
            }
            select = reimu;
            shootTask = reimu::shot;
            System.out.println("Set shot task to:" + shootTask);
            window.addKeyListener(select);

            select.setPower(4.00);
            select.setPos(StageManager.areaWidth / 2, StageManager.areaHeight * 19 / 20);
            System.out.println(select.centre());
            window.addKeyListener(select);
            StageManager.cur.render();
            start();
        }
    }

    private static void start () {
        select.render();
    }

    @Override
    public void paint (Graphics g) {
        FPS_MAKER.makeFps();
        g.drawImage(buf, 0, 0, null);
    }

    @Override
    public void run () {
        synchronized (LOCK) {
            if (select != null) {
                select.run();
            }
            repaint();
        }
    }
}
