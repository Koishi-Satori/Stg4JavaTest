package top.kkoishi.stg;

import top.kkoishi.concurrent.DefaultThreadFactory;
import top.kkoishi.game.env.CFPSMaker;
import top.kkoishi.proc.ini.INIPropertiesLoader;
import top.kkoishi.proc.ini.Section;
import top.kkoishi.proc.property.BuildFailedException;
import top.kkoishi.proc.property.LoaderException;
import top.kkoishi.proc.property.TokenizeException;
import top.kkoishi.stg.env.GameManager;
import top.kkoishi.stg.env.GraphicsManager;
import top.kkoishi.stg.env.RepaintManager;
import top.kkoishi.stg.env.Stage;
import top.kkoishi.stg.env.StageManager;
import top.kkoishi.stg.object.*;
import top.kkoishi.stg.object.bullets.ZappaBullet;
import top.kkoishi.stg.object.enemies.SimpleTh06Butterfly;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Main class.
 *
 * @author KKoishi_
 */
public final class StgTest extends JFrame implements Runnable {

    /**
     * Players' properties.
     */
    private static final File INDEX_PLAYERS = new File("./data/index/players.ini");

    private static final File INDEX_SIDEBAR = new File("./data/index/sidebar.ini");

    private static final INIPropertiesLoader INI_PLAYERS = new INIPropertiesLoader();

    private static final INIPropertiesLoader INI_SOUNDS = new INIPropertiesLoader();

    private static final INIPropertiesLoader INI_SIDEBAR = new INIPropertiesLoader();

    private static final CFPSMaker FPS_MAKER = new CFPSMaker();

    static BufferedImage[] enemyImages = new BufferedImage[4];

    /**
     * Use Double Buffering to make sure the fluency of the rendering process.
     */
    public static BufferedImage buf = new BufferedImage(1200, 720, BufferedImage.TYPE_INT_ARGB_PRE);

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
    static ScheduledThreadPoolExecutor pool = new ScheduledThreadPoolExecutor(5, new DefaultThreadFactory());

    private static final File INDEX_SOUNDS = new File("./data/index/sounds.ini");

    private static final String ENEMY_TEXTURE_PATH = "./data/enemy/stg1enm_butterfly.png";

    static {
        FPS_MAKER.setNowFPS(System.nanoTime());
        try {
            //load players.
            loadPlayers();

            //load sounds.
            System.out.println("Loading properties:" + INDEX_SOUNDS.getCanonicalPath());
            INI_SOUNDS.load(INDEX_SOUNDS, StandardCharsets.UTF_8);
            File src;
            for (final Section.INIEntry sound : INI_SOUNDS.get("sound").entries) {
                src = new File(sound.getValue());
                if (!src.exists()) {
                    throw new IOException("The file " + src + " does not exist.");
                }
                System.out.println("Adding sound:" + src.getCanonicalPath() + " as " + sound.getKey());
                GameManager.PLAYERS.put(sound.getKey(), GameManager.getAudioManager(src));
            }
            System.out.println("Success to load the properties:" + INI_SOUNDS);

            //load enemy images.
            src = new File(ENEMY_TEXTURE_PATH);
            System.out.println("Loading first butterfly enemy textures:" + src.getCanonicalPath());
            final BufferedImage total = ImageIO.read(src);
            enemyImages[0] = total.getSubimage(4, 6, 27, 28);
            enemyImages[1] = total.getSubimage(38, 5, 24, 28);
            enemyImages[2] = total.getSubimage(73, 5, 20, 30);
            enemyImages[3] = total.getSubimage(106, 5, 20, 29);
            System.out.println("Success to load and split the textures:" + Arrays.toString(enemyImages));
        } catch (IOException | TokenizeException | BuildFailedException | LoaderException |
                UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            System.exit(1919810);
        }
        GraphicsManager.initialInstance(buf.createGraphics());
        GameManager.initialSounds();
        try {
            areaInitial();
            playerInitial(reimu, "reimu");

            //load sidebar
            loadSidebar();
            reimu.bulletR = 10;
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            System.exit(1919810);
        }
    }

    private static void loadSidebar () throws IOException, TokenizeException, BuildFailedException,
            LoaderException, NoSuchFieldException, IllegalAccessException {
        System.out.println("Loading properties:" + INDEX_SIDEBAR.getCanonicalPath());
        INI_SIDEBAR.load(INDEX_SIDEBAR, StandardCharsets.UTF_8);
        System.out.println("Success to load the properties:" + INI_SIDEBAR);
        System.out.println("Initialing side bar...");
        final BufferedImage[] arr = new BufferedImage[2];
        for (Section.INIEntry image : INI_SIDEBAR.get("Image").entries) {
            System.out.println("Loading texture:" + image.getValue() + " as " + image.getKey());
            if ("life".equals(image.getKey())) {
                arr[0] = ImageIO.read(new File(image.getValue()));
            } else if ("bomb".equals(image.getKey())) {
                arr[0] = ImageIO.read(new File(image.getValue()));
            }
        }
        RepaintManager.sideBar = new SideBar(arr[0], arr[1]);
        //use reflection to fill the fields.
        final Class<SideBar> clz = SideBar.class;
        for (final Section.INIEntry entry : INI_SIDEBAR.get("Text").entries) {
            final Field f = clz.getDeclaredField(entry.getKey());
            System.out.println("Filling field:" + f);
            f.setAccessible(true);
            f.set(RepaintManager.sideBar, entry.getValue());
        }
    }

    private static void loadPlayers () throws IOException, TokenizeException, BuildFailedException, LoaderException {
        System.out.println("Loading properties:" + INDEX_PLAYERS.getCanonicalPath());
        INI_PLAYERS.load(INDEX_PLAYERS, StandardCharsets.UTF_8);
        System.out.println("Success to load the properties:" + INI_PLAYERS);
    }

    private static void areaInitial () throws IOException {
        window.setTitle("Sandbox");
        window.setSize(1224, 734);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setFocusable(true);
        StageManager.displayHeight = 720;
        StageManager.displayWidth = 1200;
        StageManager.areaWidth = buf.getWidth() * 3 / 5;
        StageManager.areaHeight = buf.getHeight() * 9 / 10;
        StageManager.initPos = new Point(StageManager.areaWidth / 2, StageManager.areaHeight * 17 / 20);
        StageManager.setStage(0, new Stage(ImageIO.read(new File("./data/stage/st1/background.png"))) {
            @Override
            protected void action () {
                GameManager.loopSound("stage_1_bg");
                genSimpleEnemy(2, 30, 50);
                genSimpleEnemy(1, 300, 70);
                genSimpleEnemy(2, 130, 130);
                rain();
            }
        });
        StageManager.cur = StageManager.getStage(0);
        File sideBar = new File("./data/title/sidebar.png");
        System.out.println("Loading sidebar:" + sideBar.getCanonicalPath());
        StageManager.sideBar = ImageIO.read(sideBar);
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
                f = Entity.class.getDeclaredField(entry.getKey());
            }
            f.setAccessible(true);
            src = new File(entry.getValue());
            System.out.println("Loading resouces:" + f + " for " + p.name());
            f.set(p, ImageIO.read(src));
            System.out.println("Sussess to load:" + src.getCanonicalPath());
        }
    }

    private static void sidebarInitial () {

    }

    public static void main (String[] args) {
        window.setVisible(true);
        System.out.println("Initialing treads.");
        System.out.println("Loading Logic Thread->Delegated to:" + RepaintManager.class);
        pool.scheduleAtFixedRate(RepaintManager.getLogicThread(), 0, 16, TimeUnit.MILLISECONDS);
        System.out.println("Loading Render Thread->" + ((Runnable) window).toString());
        pool.scheduleAtFixedRate(window, 2, 16, TimeUnit.MILLISECONDS);
        pool.scheduleAtFixedRate(() -> {
            if (select != null) {
                select.shot();
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
        pool.scheduleAtFixedRate(() -> {
            if (select != null) {
                select.shotAction();
            }
        }, 0, 60, TimeUnit.MILLISECONDS);
        selectPlayer();
    }

    @SuppressWarnings("all")
    private static void selectPlayer () {
        synchronized (LOCK) {
            if (select != null) {
                window.removeKeyListener(select);
            }
            setSelect(reimu);
            start();
        }
    }

    private static void setSelect (Player p) {
        select = p;
        GameManager.bomb = select::bomb;
        GameManager.life = select::life;
        GameManager.score = select::score;
        window.addKeyListener(select);
        StageManager.playerPosGetter = select::centre;
        select.setPower(1.00);
        select.setPos(StageManager.areaWidth / 2, StageManager.areaHeight * 19 / 20);
        System.out.println(select.centre());
        window.addKeyListener(select);
        StageManager.cur.render();
    }

    private static void rain () {
        final Random rand = new Random(System.nanoTime());
        for (int i = 0; i < 514; i++) {
            for (int j = 0; j < rand.nextInt(8, 16); j++) {
                rand.setSeed(System.nanoTime());
                final int r = rand.nextInt(5, 10);
                createBulletWithSound((10 * r * j), ZappaBullet.PURPLE);
                for (int k = 0; k < 10; k++) {
                    createBullet((10 * r * j) + r * j, ZappaBullet.RED);
                    pause(40L);
                }
            }
        }
    }

    private static void createBullet (int x, int type) {
        final ZappaBullet bullet = ZappaBullet.getInstance(type, x, 20, 3);
        bullet.setSpeed(2);
        RepaintManager.add(bullet);
    }

    private static void createBulletWithSound (int x, int type) {
        final ZappaBullet bullet = ZappaBullet.getInstance(type, x, 20, 3);
        bullet.setSpeed(2);
        RepaintManager.add(bullet);
        GameManager.playSound("stage_1_danmuku_0");
    }

    @SuppressWarnings("all")
    private static Bullet createBullet (int x, int y, int type, int speed) {
        final ZappaBullet bullet = ZappaBullet.getInstance(type, x, y, 3);
        bullet.setSpeed(speed);
        return bullet;
    }

    @SuppressWarnings("all")
    private static void pause (long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("all")
    private static void genSimpleEnemy (int speed, int x, int y) {
        final Enemy e = new SimpleTh06Butterfly(114514, enemyImages) {

            @Override
            public void addBullet (Bullet bullet) {
                super.addBullet(bullet);
                GameManager.playSound("enemy_shot");
            }

            @Override
            public void action () {
                setPos(x, y);
                GameManager.playSound("enemy_show_0");
                while (!deleteTest() && life > 0) {
                    super.pos.x += speed;
                    addBullet(createBullet(super.pos.x, super.pos.y, ZappaBullet.PURPLE, 4));
                    pause(500);
                }
            }
        };
        RepaintManager.ENEMIES.add(e);
        new Thread(e::action).start();
    }

    private static void start () {
        StageManager.stageLogicStart();
        select.render();
    }

    @Override
    public void paint (Graphics g) {
        g.drawImage(buf, 0, 0, null);
    }

    @Override
    public void run () {
        synchronized (LOCK) {
            logic();
            repaint();
        }
    }

    private void logic () {
        StageManager.cur.render();
        if (select != null) {
            synchronized (RepaintManager.TASKS) {
                Bullet b = null;
                for (Bullet task : RepaintManager.TASKS) {
                    if (task.pound(select)) {
                        System.out.println("Hit");
                        b = task;
                        select.hit();
                        break;
                    }
                }
                RepaintManager.TASKS.remove(b);
            }
            select.run();
        }
        RepaintManager.getRenderThread().run();
    }
}
