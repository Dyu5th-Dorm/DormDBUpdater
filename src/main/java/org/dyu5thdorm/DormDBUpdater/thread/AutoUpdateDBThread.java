package org.dyu5thdorm.DormDBUpdater.thread;

import org.dyu5thdorm.DormDBUpdater.DormDBUpdater;
import org.dyu5thdorm.DormDBUpdater.configuration.Config;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Update the database at regular intervals Thread.
 */
public class AutoUpdateDBThread extends Thread {
    private Integer lastUpdateTime;
    private final SimpleDateFormat format;

    /**
     * Constructor.
     */
    public AutoUpdateDBThread() {
        this.format = getSimpleDateFormat();
        super.setName(this.getClass().getSimpleName());
    }

    /**
     * According configuration updateFrequency to update time.
     * @return SimpleTimeFormat
     */
    private SimpleDateFormat getSimpleDateFormat() {
        String u = Config.updateFrequency.toLowerCase();
        if ("d".equals(u)) {
            return new SimpleDateFormat("dd");
        }
        return new SimpleDateFormat("HH");
    }

    /**
     * Get current hour of base 24.
     * @return Hour of base 24.
     */
    private Integer getCurrentHour() {
        return Integer.valueOf(
                this.format.format(new Date())
        );
    }

    /**
     * Update database.
     */
    private synchronized void updateDatabase() {
        DormDBUpdater.commands.get("update").execute();
    }

    @Override
    public void run() {
        super.run();

        while (!Thread.currentThread().isInterrupted()) {
            Integer latelyTime = this.getCurrentHour();
            if (!latelyTime.equals(lastUpdateTime)) {
                lastUpdateTime = latelyTime;
                updateDatabase();
            }
        }
    }
}
