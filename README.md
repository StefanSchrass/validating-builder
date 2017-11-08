# validating-builder
A validating Builder with java.util.Optional-support for the resulting instance.

# Usage
It is as simple as that:
```
Optional<Designated> validValue = Designated.newBuilder().withValue("a value").build();
```

The Designated objects builder, just needs to implement the ```ValidatingBuilder<T>``` interface 
and by that implement the rules for validity and the Class of the designated object.

```
public class Designated {
    private final String value;

    private Designated(Builder builder) {
        value = builder.value;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder extends ValidatingBuilder<Designated> {
        private String value;
        
        @Override
        protected Boolean isValid() {
            return null != value;
        }
        
        @Override
        protected Class<Designated> getDesignatedClass() {
            return Designated.class;
        }
        
        public Builder withValue(String value) {
            this.value = value;
            return this;
        }
    }
}
```

Please note that one does not have to implement a ``build()``-method.
