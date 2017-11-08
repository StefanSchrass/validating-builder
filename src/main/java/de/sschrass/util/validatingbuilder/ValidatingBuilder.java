package de.sschrass.util.validatingbuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
abstract public class ValidatingBuilder<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatingBuilder.class);
    @SuppressWarnings("WeakerAccess")
    protected abstract Boolean isValid();
    @SuppressWarnings("WeakerAccess")
    protected abstract Class<T> getDesignatedClass();

    public Optional<T> build() {
        Optional<T> designated = Optional.empty();
        if (isValid()) {
            try {
                final Optional<Constructor<T>> builderCtor = Optional.ofNullable(getDesignatedClass().getDeclaredConstructor(this.getClass()));
                if (builderCtor.isPresent()) {
                    builderCtor.get().setAccessible(true);
                    designated = Optional.of(builderCtor.get().newInstance(this));
                }
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                LOGGER.error("Could not instantiate the designated object, due to {}", e.getMessage());
            }
        }

        return designated;
    }
}
