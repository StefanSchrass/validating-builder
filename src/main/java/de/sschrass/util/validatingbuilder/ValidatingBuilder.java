package de.sschrass.util.validatingbuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

@SuppressWarnings({"unused", "WeakerAccess"})
@ParametersAreNonnullByDefault
abstract public class ValidatingBuilder<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatingBuilder.class);
    protected abstract Boolean isValid();

    public Optional<T> build() {
        Optional<T> designated = Optional.empty();
        if (isValid()) {
            try {
                final Class<? extends ValidatingBuilder> builderClass = getClass();
                final Class<T> designatedClass = designatedClassFromType(typeFromBuilderClass(builderClass));
                final Optional<Constructor<T>> builderCtor = Optional.ofNullable(designatedClass.getDeclaredConstructor(builderClass));
                if (builderCtor.isPresent()) {
                    builderCtor.get().setAccessible(true);
                    designated = Optional.of(builderCtor.get().newInstance(this));
                }
            }
            catch (Exception e) {
                LOGGER.error("Could not instantiate the designated object, due to {}", e.getMessage());
            }
        }

        return designated;
    }

    private Type typeFromBuilderClass(Class<? extends ValidatingBuilder> builderClass) {
        return ((ParameterizedType) builderClass.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    private Class<T> designatedClassFromType(final Type type) throws ClassNotFoundException {
        return (Class<T>) Class.forName(type.getTypeName());
    }
}
