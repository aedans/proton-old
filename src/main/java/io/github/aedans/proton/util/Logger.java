package io.github.aedans.proton.util;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vavr.CheckedRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Logger {
    private static final Map<Class<?>, List<String>> map = new HashMap<>();

    private final Class<?> clazz;

    public Logger(Class<?> clazz) {
        this.clazz = clazz;

        if (!map.containsKey(clazz))
            map.put(clazz, new ArrayList<>());
    }

    public Completable log(String log, CheckedRunnable runnable) {
        return Completable.fromSingle(log(log, () -> {
            runnable.run();
            return Unit.unit;
        }));
    }

    public <T> Single<T> log(String log, CheckedSupplier<T> supplier) {
        return log(log).toSingle(() -> {
            try {
                return supplier.get();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }

    public Completable log(String log) {
        return Completable.fromAction(() -> {
            System.out.println(log);
            map.get(clazz).add(log);
        });
    }

    public static Logger get(Class<?> clazz) {
        return new Logger(clazz);
    }
}