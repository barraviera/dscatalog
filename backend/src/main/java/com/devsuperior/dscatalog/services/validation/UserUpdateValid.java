package com.devsuperior.dscatalog.services.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// import javax.validation.Constraint;
// import javax.validation.Payload;

// O UserUpdateValidator é a classe que vai implementar está interface
@Constraint(validatedBy = UserUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)

// Este é um codigo boyler plate, ou seja, um codigo padrao pra todos e nao precisa ser decorado
public @interface UserUpdateValid {
    String message() default "Validation error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}