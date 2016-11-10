package com.hannesdorfmann.annotationprocessing101.factory.processor;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.google.auto.service.AutoService;
import com.hannesdorfmann.annotationprocessing101.factory.annotation.BeanInfo;

/**
 * Created by Jianan on 2015/10/24.
 */
public class VelocityBeanInfoProcessor extends BaseProcessor {
	
    @Override
    public boolean doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv){
        try {
            String fqClassName = null;
            String className = null;
            String packageName = null;
            Map<String, VariableElement> fields = new HashMap<String, VariableElement>();
            Map<String, ExecutableElement> methods = new HashMap<String, ExecutableElement>();

            for (Element e : roundEnv.getElementsAnnotatedWith(BeanInfo.class)) {

                if (e.getKind() == ElementKind.CLASS) {

                    TypeElement classElement = (TypeElement) e;
                    PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();

                    messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "annotated class: " + classElement.getQualifiedName(), e);

                    fqClassName = classElement.getQualifiedName().toString();
                    className = classElement.getSimpleName().toString();
                    packageName = packageElement.getQualifiedName().toString();

                } else if (e.getKind() == ElementKind.FIELD) {

                    VariableElement varElement = (VariableElement) e;

                    messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "annotated field: " + varElement.getSimpleName(), e);

                    fields.put(varElement.getSimpleName().toString(), varElement);

                } else if (e.getKind() == ElementKind.METHOD) {

                    ExecutableElement exeElement = (ExecutableElement) e;

                    messager.printMessage(
                            Diagnostic.Kind.NOTE,
                            "annotated method: " + exeElement.getSimpleName(), e);

                    methods.put(exeElement.getSimpleName().toString(), exeElement);
                }
            }

            if (fqClassName != null) {

                Properties props = new Properties();
//                props.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
//                props.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getCanonicalName());

                URL url = this.getClass().getClassLoader().getResource("velocity.properties");
                URL beaninfoVm = this.getClass().getClassLoader().getResource("beaninfo.vm");
                log("velocity.properties path: " + url.getPath());
                log("beaninfo.vm path: " + beaninfoVm.getPath());
                props.load(url.openStream());
                
//                String templateName = "";
                final String templatePath = "beaninfo.vm";
//                final String templatePath = "templates/" + templateName + ".vm";
                InputStream input = this.getClass().getClassLoader().getResourceAsStream(templatePath);
                if (input == null) {
                    throw new IOException("Template file doesn't exist");
                }

                InputStreamReader reader = new InputStreamReader(input);
                
              //设置velocity资源加载方式为jar  
//                props.setProperty("resource.loader", "jar");  
                //设置velocity资源加载方式为file时的处理类  
//                props.setProperty("jar.resource.loader.class", "org.apache.velocity.runtime.resource.loader.JarResourceLoader");  
                //设置jar包所在的位置  
//                props.setProperty("jar.resource.loader.path", "jar:file:C:\\Users\\Administrator\\git\\annotationprocessing101\\factory\\processor\\target\\annotationprocessing101-processor_fat.jar");  
                
//                Properties p = new Properties();
//                p.setProperty("file.resource.loader.path", vmPath+"//");
//                ve.init(p);

                
				log("[velocity.properties] " + props.toString());
                
                VelocityEngine ve = new VelocityEngine(props);
                ve.init();
                
                VelocityContext vc = new VelocityContext();

                vc.put("className", className);
                vc.put("packageName", packageName);
                vc.put("fields", fields);
                vc.put("methods", methods);

//                Template vt = ve.getTemplate("beaninfo.vm");
                log("load vm success");
				JavaFileObject jfo = filer.createSourceFile(fqClassName + "BeanInfo");

				messager.printMessage(
                        Diagnostic.Kind.NOTE,
                        "creating source file: " + jfo.toUri());

                Writer writer = jfo.openWriter();

                messager.printMessage(
                        Diagnostic.Kind.NOTE,
                        "applying velocity template: " + "beaninfo.vm");

//                vt.merge(vc, writer);
                
                ve.evaluate(vc, writer, "beaninfo.vm", reader);

                writer.close();
            }
		}
        
        catch (Exception e) {
			error(null, e.getMessage());
			 log("error:" + e.getMessage());
		}
        return true;
    }
    
	@Override
	public List<Class<?>> acceptAnnotationTypes() {
		return Arrays.asList(BeanInfo.class);
	}

}