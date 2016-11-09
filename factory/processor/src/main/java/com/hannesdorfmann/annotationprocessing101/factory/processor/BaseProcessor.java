package com.hannesdorfmann.annotationprocessing101.factory.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import com.hannesdorfmann.annotationprocessing101.factory.annotation.BeanInfo;
import com.hannesdorfmann.annotationprocessing101.factory.annotation.Complexity;

/**
 * 
 * @author Darkness
 * @date 2016年11月9日 下午4:31:00
 * @version V1.0
 */
public abstract class BaseProcessor extends AbstractProcessor {

	protected Types typeUtils;
	protected Elements elementUtils;
	protected Filer filer;
	protected Messager messager;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		typeUtils = processingEnv.getTypeUtils();
		elementUtils = processingEnv.getElementUtils();
		filer = processingEnv.getFiler();
		messager = processingEnv.getMessager();
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
	}

	File file;
	FileOutputStream os;

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		
		file = new File("C:\\Users\\Administrator\\git\\annotationprocessing101\\factory-sample\\pizzastore\\target\\" + this.getClass().getCanonicalName() + ".log");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			os = new FileOutputStream(file, true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		log("do process start");

		boolean result = doProcess(annotations, roundEnv);

		log("do process end");

		try {
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result; // no further processing of this annotation type
	}

	protected void log(String message) {
		try {
			os.write(message.getBytes());
			os.write("\r\n".getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prints an error message
	 *
	 * @param e
	 *            The element which has caused the error. Can be null
	 * @param msg
	 *            The error message
	 */
	protected void error(Element e, String msg) {
		messager.printMessage(Diagnostic.Kind.ERROR, msg, e);
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> annotataions = new LinkedHashSet<String>();
		List<Class<?>> acceptAnnotationTypes = acceptAnnotationTypes();
		for (Class<?> clazz : acceptAnnotationTypes) {
			annotataions.add(clazz.getCanonicalName());
		}
		return annotataions;
	}

	public List<Class<?>> acceptAnnotationTypes() {
		return new ArrayList<>();
	}

	protected abstract boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);
}
