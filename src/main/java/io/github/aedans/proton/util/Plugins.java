package io.github.aedans.proton.util;

import io.reactivex.Completable;
import io.vavr.collection.List;
import org.pf4j.DefaultPluginManager;
import org.pf4j.ExtensionPoint;
import org.pf4j.PluginManager;

import java.io.File;

public final class Plugins {
    private static final Logger logger = Logger.get(Plugins.class);
    private static final PluginManager manager = new DefaultPluginManager(new File(".proton/plugins").toPath());

    public static Completable start() {
        return logger.log("Starting plugins", () -> {
            manager.loadPlugins();
            manager.startPlugins();
        });
    }

    public static Completable stop() {
        return logger.log("Stopping plugins", manager::stopPlugins);
    }

    public static <T extends ExtensionPoint> List<T> all(Class<T> clazz) {
        return List.ofAll(manager.getExtensions(clazz));
    }
}