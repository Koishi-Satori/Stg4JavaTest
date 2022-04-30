package top.kkoishi.game.env;

import java.text.DecimalFormat;

/**
 * <p>Title: LoonFramework</p>
 * <p>Description:</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: LoonFramework</p>
 * @author chenpeng
 * @version 0.1
 *
 */
public class CFPSMaker
{
    /**
     * 设定动画运行多少帧后统计一次帧数
     */
    public static final int FPS = 8;

    /**
     * 换算为运行周期
     * 单位: ns(纳秒)
     */
    public static final long PERIOD = (long) (1.0 / FPS * 1000000000);
    /**
     * FPS最大间隔时间，换算为1s = 10^9ns
     * 单位: ns
     */
    public static long FPS_MAX_INTERVAL = 1000000000L;

    /**
     * 实际的FPS数值
     */
    private double nowFPS = 0.0;

    /**
     * FPS累计用间距时间
     * in ns
     */
    private long interval = 0L;
    private long time;
    /**
     * 运行桢累计
     */
    private long frameCount = 0;

    /**
     * 格式化小数位数
     */
    private final DecimalFormat df = new DecimalFormat("0.0");

    /**
     * 制造FPS数据
     *
     */
    public void makeFps()
    {
        frameCount++;
        interval += PERIOD;
        if (interval >= FPS_MAX_INTERVAL)
        {
            long timeNow = System.nanoTime();
            long realTime = timeNow - time;
            nowFPS = ((double) frameCount / realTime) * FPS_MAX_INTERVAL;

            frameCount = 0L;
            interval = 0L;
            time = timeNow;
        }
    }

    public long getFrameCount()
    {
        return frameCount;
    }

    public void setFrameCount(long frameCount)
    {
        this.frameCount = frameCount;
    }

    public long getInterval()
    {
        return interval;
    }

    public void setInterval(long interval)
    {
        this.interval = interval;
    }

    public double getNowFPS()
    {
        return nowFPS;
    }

    public void setNowFPS(double nowFPS)
    {
        this.nowFPS = nowFPS;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public String getFPS()
    {
        return df.format(nowFPS);
    }
}
