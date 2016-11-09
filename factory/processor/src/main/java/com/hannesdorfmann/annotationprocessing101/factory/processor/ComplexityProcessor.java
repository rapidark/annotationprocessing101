package com.hannesdorfmann.annotationprocessing101.factory.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.hannesdorfmann.annotationprocessing101.factory.annotation.BeanInfo;
import com.hannesdorfmann.annotationprocessing101.factory.annotation.Complexity;

/**
 * Created by Jianan on 2015/10/14.
 */
@AutoService(Processor.class)
public class ComplexityProcessor extends BaseProcessor {
	
    @Override
	protected boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element elem : roundEnv.getElementsAnnotatedWith(Complexity.class)) {
			Complexity complexity = elem.getAnnotation(Complexity.class);
			String message = System.currentTimeMillis() + " - annotation found in " + elem.getSimpleName() + " with complexity " + complexity.value();

			log(message);

			messager.printMessage(Diagnostic.Kind.NOTE, message);
		}
		return false;
	}

    @Override
	public List<Class<?>> acceptAnnotationTypes() {
		return Arrays.asList(Complexity.class);
	}
    
}
