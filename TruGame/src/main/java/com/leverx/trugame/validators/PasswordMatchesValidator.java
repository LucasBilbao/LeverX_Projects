package com.leverx.trugame.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private String passwordField;
    private String confirmPasswordField;
    private String message;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        this.passwordField = constraintAnnotation.passwordField();
        this.confirmPasswordField = constraintAnnotation.confirmPasswordField();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;

        try {
            Map<String, PropertyDescriptor> props = Stream.of(Introspector.getBeanInfo(value.getClass())
                            .getPropertyDescriptors())
                    .collect(Collectors.toMap(PropertyDescriptor::getName, pd -> pd));

            PropertyDescriptor p1 = props.get(passwordField);
            PropertyDescriptor p2 = props.get(confirmPasswordField);

            if (p1 == null || p2 == null) {
                setConstraintMessage(context, "Validator configuration error: fields not found");
                return false;
            }

            Object pw = p1.getReadMethod().invoke(value);
            Object confirm = p2.getReadMethod().invoke(value);

            boolean matches = pw != null && pw.equals(confirm);

            if (!matches) {
                setConstraintMessage(context, message);
            }

            return matches;
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException ex) {
            setConstraintMessage(context, "Validation error");
            return false;
        }
    }

    private void setConstraintMessage(ConstraintValidatorContext context, String msg) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(msg)
                .addPropertyNode(confirmPasswordField)
                .addConstraintViolation();
    }
}
