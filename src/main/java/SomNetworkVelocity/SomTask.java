package SomNetworkVelocity;

import com.velocitypowered.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class SomTask {

    public static ScheduledTask run(Runnable runnable) {
        return SomCore.getProxy().getScheduler().buildTask(SomCore.getPlugin(), runnable).schedule();
    }

    public static ScheduledTask timer(Runnable runnable, int time) {
        return SomCore.getProxy().getScheduler().buildTask(SomCore.getPlugin(), runnable).repeat(time, TimeUnit.MILLISECONDS).schedule();
    }

    public static ScheduledTask delay(Runnable runnable, int time) {
        return SomCore.getProxy().getScheduler().buildTask(SomCore.getPlugin(), runnable).delay(time, TimeUnit.MILLISECONDS).schedule();
    }

    public static void wait(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
