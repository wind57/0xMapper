package com.github.er.mapper.exception;

/**
 * @author ER
 * <p>
 * a simpler way to explain to the callers what method was not found
 */
public final class MethodNotFoundException extends RuntimeException {

    public MethodNotFoundException(Class<?> against, Class<?> rawType, String methodName) {
        super(
            "Class : '" + against.getSimpleName() + "' has no method : '" + methodName +
                (methodName.startsWith("get") ? "' that returns : '" + rawType.getSimpleName() + "'" :
                 "' that takes a " + rawType.getSimpleName() + " as input"));
    }

}
