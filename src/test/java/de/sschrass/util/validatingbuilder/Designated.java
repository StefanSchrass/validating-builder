package de.sschrass.util.validatingbuilder;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
class Designated {
    private final String value;

    private Designated(Builder builder) {
        value = builder.value;
    }

    static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder extends ValidatingBuilder<Designated> {
        private String value;

        @Override
        protected Boolean isValid() {
            return null != value;
        }

        Builder withValue(String value) {
            this.value = value;
            return this;
        }

    }
}
