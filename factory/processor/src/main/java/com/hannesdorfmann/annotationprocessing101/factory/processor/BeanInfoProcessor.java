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
@AutoService(Processor.class)
public class BeanInfoProcessor extends BaseProcessor {
	
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

//                vc.put("className", className);
//                vc.put("packageName", packageName);
//                vc.put("fields", fields);
//                vc.put("methods", methods);

				JavaFileObject jfo = filer.createSourceFile(fqClassName + "BeanInfo");

				messager.printMessage(
                        Diagnostic.Kind.NOTE,
                        "creating source file: " + jfo.toUri());

                Writer writer = jfo.openWriter();

                writer.write(generateJavaClassText());

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
	
	public String generateJavaClassText() {
		return "package com.abigdreamer.infinity.persistence.fastdb;\n" + 
				"\n" + 
				"import java.nio.ByteBuffer;\n" + 
				"\n" + 
				"public class PersonRecordConverter extends RecordConverter<Person> {\n" + 
				"\n" + 
				"	FastColumn nameColumn = new FastColumn(FastColumnType.String, \"name\", 10);\n" + 
				"	FastColumn ageColumn = new FastColumn(FastColumnType.Int, \"age\");\n" + 
				"	FastColumn moneyColumn = new FastColumn(FastColumnType.Double, \"money\");\n" + 
				"	FastColumn salaryColumn = new FastColumn(FastColumnType.Float, \"salary\");\n" + 
				"	FastColumn marriedColumn = new FastColumn(FastColumnType.Boolean, \"married\");\n" + 
				"	FastColumn[] columns = new FastColumn[]{\n" + 
				"		nameColumn, ageColumn, moneyColumn, salaryColumn, marriedColumn\n" + 
				"	};\n" + 
				"	\n" + 
				"	@Override\n" + 
				"	public Class<Person> acceptEntityClass() {\n" + 
				"		return Person.class;\n" + 
				"	}\n" + 
				"	\n" + 
				"	@Override\n" + 
				"	public FastColumn[] getColumns() {\n" + 
				"		return columns;\n" + 
				"	}\n" + 
				"\n" + 
				"	@Override\n" + 
				"	public void writeEntity2Buffer(Person person, ByteBuffer recordBuffer) {\n" + 
				"		String name = person.getName();\n" + 
				"		writeString(recordBuffer, name, nameColumn.getLength());\n" + 
				"		\n" + 
				"		recordBuffer.putInt(person.getAge());// int\n" + 
				"		recordBuffer.putDouble(person.getMoney());// double\n" + 
				"		recordBuffer.putFloat(person.getSalary());// float\n" + 
				"		recordBuffer.put((byte)(person.isMarried() ? 1 : 0));// boolean\n" + 
				"	}\n" + 
				"\n" + 
				"	public Person builderObject(ByteBuffer recordBuffer) {\n" + 
				"		Person person = new Person();\n" + 
				"		\n" + 
				"		String name = readString(recordBuffer, nameColumn.getLength());\n" + 
				"		person.setName(name);\n" + 
				"		\n" + 
				"		int age = recordBuffer.getInt();\n" + 
				"		person.setAge(age);\n" + 
				"		\n" + 
				"		double money = recordBuffer.getDouble();\n" + 
				"		person.setMoney(money);\n" + 
				"		\n" + 
				"		float salary = recordBuffer.getFloat();\n" + 
				"		person.setSalary(salary);\n" + 
				"		\n" + 
				"		boolean isMarried = recordBuffer.get() == 1;\n" + 
				"		person.setMarried(isMarried);\n" + 
				"		\n" + 
				"		return person;\n" + 
				"	}\n" + 
				"\n" + 
				"}\n" + 
				"";
	}

}