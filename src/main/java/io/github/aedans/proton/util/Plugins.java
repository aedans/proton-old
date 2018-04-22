package io.github.aedans.proton.util;

import fj.Unit;
import fj.data.List;
import fj.data.Option;
import fj.data.Stream;
import io.github.aedans.pfj.IO;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class Plugins {
    private static final PluginManager manager = new DefaultPluginManager(new File(".proton/plugins").toPath());

    public static IO<Unit> start() {
        return IO.run(() -> {
            manager.loadPlugins();
            manager.startPlugins();
        });
    }

    public static IO<Unit> stop() {
        return IO.run(manager::stopPlugins);
    }

    private static final Map<Class, Map<Key, Object>> cache = new HashMap<>();

    public static <T> List<T> all(Class<T> tClass) {
        return List.iterableList(manager.getExtensions(tClass));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Unique> Option<T> forKey(Class<T> tClass, Key key) {
        Map<Key, Object> tMap = cache.get(tClass);
        if (tMap == null) {
            Stream<Unique> elems = Stream.iterableStream(manager.getExtensions(tClass)).map(x -> ((Unique) x));
            Map<Key, Object> newTMap = new HashMap<>();
            elems.forEach(x -> newTMap.put(x.key(), x));
            cache.put(tClass, newTMap);
            return forKey(tClass, key);
        } else {
            return Option.fromNull((T) tMap.get(key));
        }
    }
}
