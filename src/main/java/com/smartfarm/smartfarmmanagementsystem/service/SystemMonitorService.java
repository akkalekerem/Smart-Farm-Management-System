package com.smartfarm.smartfarmmanagementsystem.service;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;

@Service
public class SystemMonitorService {

    // Gerçek zamanlı CPU kullanım yüzdesini çeker
    public double getCpuUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuLoad = osBean.getCpuLoad() * 100; // 0.0 ile 1.0 arası döner, 100 ile çarpıyoruz

        // Eğer sistem ilk açılışta okuyamazsa NaN döner, onu 0.0 olarak düzeltiyoruz
        return Double.isNaN(cpuLoad) || cpuLoad < 0 ? 0.0 : Math.round(cpuLoad * 10.0) / 10.0;
    }

    // Kullanılan RAM miktarını çeker (Megabyte cinsinden)
    public long getUsedRam() {
        Runtime runtime = Runtime.getRuntime();
        return (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    }

    // JVM'ye ayrılan Toplam RAM miktarını çeker (Megabyte cinsinden)
    public long getTotalRam() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() / (1024 * 1024);
    }
}