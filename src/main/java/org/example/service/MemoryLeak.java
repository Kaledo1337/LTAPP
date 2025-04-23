package org.example.service;

import org.example.utils.Person;
import org.springframework.stereotype.Component;


import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class MemoryLeak {

//    public static void startLeak(int numIter, boolean memoryLeak) {
//        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
//        for (int i = 0; i < 1; i++) {
//            executor.execute(new Leak(numIter, memoryLeak));
//        }
//        executor.shutdown();
//    }

    public static class Leak implements Runnable {
        private static int num;
        private static boolean leak;
        private volatile boolean leakFlag; // Флаг для остановки утечки

        public Leak(int numIter, boolean memoryLeak) {
            num = numIter;
            leak = memoryLeak;
//            leakFlag = true; // Инициализация флага
        }

        public void setLeakFlagLeak(boolean on) {
            leakFlag = on;
        }

        public void stopLeak() {
            leakFlag = false; // Устанавливаем флаг остановки утечки
        }

        @Override
        public void run() {
            List<Map<SoftReference<String>, Integer>> mapList = new ArrayList<>();
            while (leakFlag && leak) { // Проверяем и флаг, и условие утечки
                System.out.println(leakFlag);
                Map<SoftReference<String>, Integer> map = new HashMap<>();
                for (int i = 0; i < num; i++) {
                    SoftReference<String> stringRef = new SoftReference<>(new String("jon" + i)); // Создаем пустую строку
                    map.put(stringRef, i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                mapList.add(map);
            }
        }
    }
}
