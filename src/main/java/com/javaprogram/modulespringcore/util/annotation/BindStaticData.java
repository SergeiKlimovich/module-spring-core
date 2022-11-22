package com.javaprogram.modulespringcore.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.javaprogram.modulespringcore.models.Identifiable;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindStaticData {
    String fileLocation();

    Class<? extends Identifiable> castTo();
}
