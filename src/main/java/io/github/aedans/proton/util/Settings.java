package io.github.aedans.proton.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.pf4j.ExtensionPoint;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;

public final class Settings {
    public static final File file = new File(".proton");
    public static final Lazy<Gson> gson = new Lazy<>(() ->
            Plugins.all(GsonExtension.class)
                    .foldLeft((a, b) -> { b.build(a); return a; }, new GsonBuilder().setPrettyPrinting().setLenient())
                    .create());

    public static <T> T get(Class<T> tClass) {
        Gson gson = Settings.gson.get();
        return IO.run(() -> {
            String name = tClass.getSimpleName();
            File file = new File(Settings.file, name + ".json");
            if (file.exists()) {
                T t = gson.fromJson(Files.newBufferedReader(file.toPath()), tClass);
                BufferedWriter writer = Files.newBufferedWriter(file.toPath());
                gson.toJson(t, writer);
                writer.flush();
                return t;
            } else {
                T orElse = tClass.newInstance();
                BufferedWriter writer = Files.newBufferedWriter(file.toPath());
                gson.toJson(orElse, writer);
                writer.flush();
                return get(tClass);
            }
        }).runUnsafe();
    }

    interface GsonExtension extends ExtensionPoint {
        void build(GsonBuilder builder);
    }
}
