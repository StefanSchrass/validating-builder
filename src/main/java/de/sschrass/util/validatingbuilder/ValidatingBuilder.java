package de.sschrass.util.validatingbuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
            final Optional<Class<T>> designatedClass = designatedClassFromType(typeFromBuilderClass(getClass()));
            if (designatedClass.isPresent()) {
                designated = designatedFromDesignatedClass(designatedClass.get());
            }
        }

        return designated;
    }

    private Optional<T> designatedFromDesignatedClass(Class<T> designatedClass) {
        Optional<T> designated = Optional.empty();
        Optional<Constructor<T>> builderCtor = Optional.empty();

        try { builderCtor = Optional.ofNullable(designatedClass.getDeclaredConstructor(getClass())); }
        catch (NoSuchMethodException e) {
            LOGGER.error("Could get a suitable constructor for the designated object, due to {}", e.getMessage());
        }

        if (builderCtor.isPresent()) {
            builderCtor.get().setAccessible(true);

            try { designated = Optional.of(builderCtor.get().newInstance(this)); }
            catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                LOGGER.error("Could not instantiate the designated object, due to {}", e.getMessage());
            }
        }

        return designated;
    }

    private Type typeFromBuilderClass(Class<? extends ValidatingBuilder> builderClass) {
        return ((ParameterizedType) builderClass.getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("unchecked")
    private Optional<Class<T>> designatedClassFromType(final Type type){
        Optional<Class<T>> designatedClass = Optional.empty();
        try { designatedClass = Optional.ofNullable((Class<T>) Class.forName(type.getTypeName())); }
        catch (ClassNotFoundException e) {
            LOGGER.error("Could not get Class for name {}, due to {}", type.getTypeName(), e.getMessage());
        }

        return designatedClass;
    }
}
