package de.sschrass.util.validatingbuilder;

import org.junit.Assert;
import org.junit.Test;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
public class ValidatingBuilderTest {

    @Test
    public void testBuilder() {
        final Optional<Designated> validValue = Designated.newBuilder().withValue("a value").build();
        Assert.assertTrue(validValue.isPresent());

        final Optional<Designated> invalidValue = Designated.newBuilder().build();
        Assert.assertFalse(invalidValue.isPresent());
    }

}