package io.github.aedans.proton.util;

public final class ExceptionUtils {
    private ExceptionUtils() {
    }

    public interface CheckedSupplier<T> {
        T get() throws Throwable;
    }

    public interface CheckedRunnable {
        void run() throws Throwable;
    }

    public static <T> T doUnchecked(CheckedSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static void doUnchecked(CheckedRunnable runnable) {
        try {
            runnable.run();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
