package libraries.demo.application;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class MemoryMonitor {
    private static final long MB = 1024 * 1024;
    private long lastUpdate = 0;

    public void start(Stage stage) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate > 500_000_000) { // cada 0.5 seg
                    Platform.runLater(MemoryMonitor::printStats);
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    private static void printStats() {
        long heapUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long nonHeapUsed = getNonHeapMemory();
        long prismVram = getPrismVram();

        System.out.printf("[MEM] Heap: %d MB | NonHeap: %d MB | Prism VRAM: %d MB%n",
                heapUsed / MB, nonHeapUsed / MB, prismVram / MB);
    }

    private static long getNonHeapMemory() {
        return ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage().getUsed();
    }

    private static long getPrismVram() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = new ObjectName("com.sun.prism:type=ES2ResourceFactory");
            Object value = mbs.getAttribute(name, "ManagedResourceCount");
            if (value instanceof Number) {
                return ((Number) value).longValue();
            }
        } catch (Exception ignored) {}
        return -1;
    }
}
