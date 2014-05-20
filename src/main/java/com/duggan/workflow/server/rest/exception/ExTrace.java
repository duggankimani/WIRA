package com.duggan.workflow.server.rest.exception;

import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ExTrace {

	private String declaringClass;
	
    private String methodName;
    
    private String fileName;

	private int    lineNumber;
    
	public ExTrace() {

	}
	
	public ExTrace(String declaringClass, String methodName, String fileName,
			int lineNumber) {
		this.declaringClass = Objects.requireNonNull(declaringClass,
				"Declaring class is null");
		this.methodName = Objects.requireNonNull(methodName,
				"Method name is null");
		this.fileName = fileName;
		this.lineNumber = lineNumber;
	}
	
    public String getDeclaringClass() {
		return declaringClass;
	}

	public void setDeclaringClass(String declaringClass) {
		this.declaringClass = declaringClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

}
