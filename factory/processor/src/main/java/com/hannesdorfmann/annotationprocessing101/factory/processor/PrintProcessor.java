package com.hannesdorfmann.annotationprocessing101.factory.processor;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.google.auto.service.AutoService;
import com.hannesdorfmann.annotationprocessing101.factory.annotation.Print;

/**
 * 
 * @author Darkness
 * @date 2016年11月9日 下午3:40:09
 * @version V1.0
 */
@AutoService(Processor.class)
public class PrintProcessor extends BaseProcessor {

	@Override
	public boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment env) {
		Messager messager = processingEnv.getMessager();
		for (TypeElement te : annotations) {
			for (Element e : env.getElementsAnnotatedWith(te)) {
				String message = "Printing: " + e.toString();
				messager.printMessage(Diagnostic.Kind.NOTE, message);
				log(message);
			}
		}
		return true;
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotataions = new LinkedHashSet<String>();
		annotataions.add(Print.class.getCanonicalName());
		return annotataions;
	}

}
