package com.hannesdorfmann.annotationprocessing101.factory.annotation;

/**
 * Created by Jianan on 2015/10/14.
 */
public @interface Complexity {
    ComplexityLevel value() default ComplexityLevel.MEDIUM;
}
