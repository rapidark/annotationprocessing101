package com.qinxiandiqi.annotationprocessordemo.client;

import com.hannesdorfmann.annotationprocessing101.factory.annotation.Complexity;
import com.hannesdorfmann.annotationprocessing101.factory.annotation.ComplexityLevel;

/**
 * Created by Jianan on 2015/10/14.
 */
@Complexity(ComplexityLevel.VERY_SIMPLE)
public class SimpleAnnotationsTest {

    @Complexity() // this annotation type applies also to methods
    // the default value 'ComplexityLevel.MEDIUM' is assumed
    public void theMethod() {
    	
        System.out.println("consoleut");
    }

}
