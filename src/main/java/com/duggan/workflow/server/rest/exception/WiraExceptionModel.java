package com.duggan.workflow.server.rest.exception;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class WiraExceptionModel implements Serializable {

	private static final long serialVersionUID = -3270868741927626663L;
	private String cause;
	private String message;
	// underlying cause
	private WiraExceptionModel exceptionCause;
	private int errorCode;
	private List<ExTrace> exceptionTraceList = new ArrayList<ExTrace>();

	public WiraExceptionModel() {
	}
	
	public Throwable getCause() {
		Exception ex = null;

		Throwable throwableCause = null;

		if (exceptionCause != null)
			throwableCause = exceptionCause.getCause();

		try {

			//Per Class Exception Handling
			if (cause.equals(IOException.class.getName())) {
				if (exceptionCause != null)
					ex = new IOException(message, throwableCause);
			} else {
				ex = getException(throwableCause);
			}

			StackTraceElement[] stackTrace = new StackTraceElement[exceptionTraceList
					.size()];

			int i = 0;
			for (ExTrace trace : exceptionTraceList) {
				stackTrace[i++] = new StackTraceElement(
						trace.getDeclaringClass(), trace.getMethodName(),
						trace.getFileName(), trace.getLineNumber());
			}

			ex.setStackTrace(stackTrace);

		} catch (Exception e) {

		}

		return ex;
	}

	private Exception getException(Throwable throwableCause) {

		Exception ex = new Exception();

		try {
			Class<?> clazz = Class.forName(cause);

			if (throwableCause == null)
				ex = (Exception) clazz.getConstructor(String.class)
						.newInstance(message);
			else
				ex = (Exception) clazz.getConstructor(String.class,
						Throwable.class).newInstance(message, throwableCause);

		} catch (Exception e) {

		}

		return ex;
	}

	public void setCause(Throwable cause, WiraExceptionModel exceptionCause) {
		this.cause = cause.getClass().getName();
		this.message = cause.getMessage();
		this.exceptionCause = exceptionCause;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public void addTrace(String className, String methodName, String fileName,
			int lineNumber) {
		exceptionTraceList.add(new ExTrace(className, methodName, fileName,
				lineNumber));

	}

	public static WiraExceptionModel getExceptionModel(Throwable e) {
		if (e == null) {
			return null;
		}

		WiraExceptionModel ex = new WiraExceptionModel();
		ex.setCause(e, getExceptionModel(e.getCause()));

		ex.setMessage(e.getMessage());

		StackTraceElement[] elm = e.getStackTrace();

		for (StackTraceElement el : elm) {

			ex.addTrace(el.getClassName(), el.getMethodName(),
					el.getFileName(), el.getLineNumber());
		}

		e.setStackTrace(new StackTraceElement[0]);

		return ex;
	}


	public void setCause(String cause) {
		this.cause = cause;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
