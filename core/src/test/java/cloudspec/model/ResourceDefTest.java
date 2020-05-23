package cloudspec.model;

import cloudspec.util.ModelGenerator;
import cloudspec.util.ModelTestUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ResourceDefTest {
    @Test
    public void shouldNotGetPropertyByPath() {
        List<String> path = Arrays.asList(ModelTestUtils.PROP_NESTED_NAME, ModelGenerator.randomName());

        Optional<PropertyDef> propertyDef = ModelTestUtils.RESOURCE_DEF.getPropertyByPath(path);
        assertNotNull(propertyDef);
        assertFalse(propertyDef.isPresent());
    }

    @Test
    public void shouldGetPropertyByPath() {
        List<String> path = Arrays.asList(ModelTestUtils.PROP_NESTED_NAME, ModelTestUtils.PROP_INTEGER_NAME);

        Optional<PropertyDef> propertyDefOpt = ModelTestUtils.RESOURCE_DEF.getPropertyByPath(path);
        assertNotNull(propertyDefOpt);
        assertTrue(propertyDefOpt.isPresent());
        assertEquals(ModelTestUtils.PROP_INTEGER_DEF, propertyDefOpt.get());
    }
}
