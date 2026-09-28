package io.swagger.v3.core.util;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.MapperBuilder;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Lazily-built, replaceable holder for the mappers exposed by {@link Json}, {@link Yaml}, {@link Json31} and
 * {@link Yaml31}. A mapper built from the factory is rebuilt when {@link ObjectMapperFactory#generation()} moves
 * (i.e. customizers were added or removed); a mapper explicitly installed with {@link #set(ObjectMapper)} is kept
 * until {@link #reset()}.
 */
final class MapperHolder {

    private final Supplier<ObjectMapper> factory;
    private volatile ObjectMapper mapper;
    private volatile long generation = -1;
    private volatile boolean explicit;

    MapperHolder(Supplier<ObjectMapper> factory) {
        this.factory = factory;
    }

    ObjectMapper get() {
        ObjectMapper current = mapper;
        if (current != null && (explicit || generation == ObjectMapperFactory.generation())) {
            return current;
        }
        synchronized (this) {
            current = mapper;
            long currentGeneration = ObjectMapperFactory.generation();
            if (current == null || (!explicit && generation != currentGeneration)) {
                current = factory.get();
                mapper = current;
                generation = currentGeneration;
            }
            return current;
        }
    }

    synchronized void set(ObjectMapper newMapper) {
        mapper = Objects.requireNonNull(newMapper, "mapper");
        explicit = true;
    }

    synchronized ObjectMapper configure(Consumer<MapperBuilder<?, ?>> customizer) {
        Objects.requireNonNull(customizer, "customizer");
        MapperBuilder<?, ?> builder = get().rebuild();
        customizer.accept(builder);
        ObjectMapper rebuilt = builder.build();
        set(rebuilt);
        return rebuilt;
    }

    synchronized void reset() {
        mapper = null;
        explicit = false;
        generation = -1;
    }
}
