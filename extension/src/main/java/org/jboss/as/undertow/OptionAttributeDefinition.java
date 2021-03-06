package org.jboss.as.undertow;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.jboss.as.controller.AbstractAttributeDefinitionBuilder;
import org.jboss.as.controller.AttributeMarshaller;
import org.jboss.as.controller.DeprecationData;
import org.jboss.as.controller.ParameterCorrector;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.client.helpers.MeasurementUnit;
import org.jboss.as.controller.operations.validation.ParameterValidator;
import org.jboss.as.controller.registry.AttributeAccess;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.xnio.Option;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a> (c) 2012 Red Hat Inc.
 */
class OptionAttributeDefinition extends SimpleAttributeDefinition {
    private Option option;

    private OptionAttributeDefinition(String name, String xmlName, ModelNode defaultValue, ModelType type, boolean allowNull, boolean allowExpression,
                                      MeasurementUnit measurementUnit, ParameterCorrector corrector, ParameterValidator validator, boolean validateNull,
                                      String[] alternatives, String[] requires, AttributeMarshaller attributeMarshaller, boolean resourceOnly,
                                      DeprecationData deprecated, Option option, AttributeAccess.Flag... flags) {
        super(name, xmlName, defaultValue, type, allowNull, allowExpression, measurementUnit, corrector, validator, validateNull, alternatives, requires, attributeMarshaller, resourceOnly, deprecated, flags);
        this.option = option;
    }

    public Option getOption() {
        return option;
    }

    public static class Builder extends AbstractAttributeDefinitionBuilder<Builder, OptionAttributeDefinition> {
        private Option option;

        public Builder(String attributeName, Option option) {
            super(attributeName, getType(option),true);
            this.option = option;
        }


        @Override
        public OptionAttributeDefinition build() {
            return new OptionAttributeDefinition(name, xmlName, defaultValue, type, allowNull, allowExpression, measurementUnit,
                    corrector, validator, validateNull, alternatives, requires, attributeMarshaller, resourceOnly, deprecated, option, flags);
        }

        private static ModelType getType(Option option) {

            try {
                Field typeField = option.getClass().getDeclaredField("type");
                typeField.setAccessible(true);
                Class type = (Class) typeField.get(option);


                if (type.isAssignableFrom(Integer.class)) {
                    return ModelType.INT;
                } else if (type.isAssignableFrom(Long.class)) {
                    return ModelType.LONG;

                } else if (type.isAssignableFrom(BigInteger.class)) {
                    return ModelType.BIG_INTEGER;
                } else if (type.isAssignableFrom(Double.class)) {
                    return ModelType.DOUBLE;
                } else if (type.isAssignableFrom(BigDecimal.class)) {
                    return ModelType.BIG_DECIMAL;

                } else if (type.isAssignableFrom(String.class)) {
                    return ModelType.STRING;

                } else if (type.isAssignableFrom(Boolean.class)) {
                    return ModelType.BOOLEAN;

                } else {
                    return ModelType.OBJECT;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return ModelType.OBJECT;
        }
    }


}
