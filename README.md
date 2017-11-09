[ ![Download](https://api.bintray.com/packages/sschrass/validating-builder/validating-builder/images/download.svg) ](https://bintray.com/sschrass/validating-builder/validating-builder/_latestVersion)
[![Build Status](https://travis-ci.org/StefanSchrass/validating-builder.svg?branch=master)](https://travis-ci.org/StefanSchrass/validating-builder)

## Item 2 of Effective Java (2nd Edition), Joshua Bloch
>It is critical that they be checked after copying the parameters from the builder to the object, and that they be checked on the object fields rather than the builder fields (Item 39). If any invariants are violated, the build method should throw an IllegalStateException (Item 60). 

This Builder does not follow these items by the letter.

1. I found it cumbersome to perform the validation in the designated objects constructor itself and
2. I don't like Exceptions at all, so I opted to return ``Optional.empty()`` in such cases.

## Usage
It is as simple as that:
```
Optional<Designated> validValue = Designated.newBuilder().withValue("a value").build();
```
while this:
```
Optional<Designated> invalidValue = Designated.newBuilder().build();
```

results in an ``Optional.empty()``.


The ``Builder`` just needs to implement the ``ValidatingBuilder<T>`` interface 
and by that implement the rules for validity.

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
        
        public Builder withValue(String value) {
            this.value = value;
            return this;
        }
    }
}
```

Please note that one does not have to implement a ``build()``-method.
