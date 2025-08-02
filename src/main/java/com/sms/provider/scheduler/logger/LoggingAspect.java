package com.sms.provider.scheduler.logger;

import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);
	
	/**
     * Pointcut that matches all public methods in the controller package.
     */
    @Pointcut("execution(public * com.sms.provider.scheduler.controller..*(..))")
    private void forControllerPackage() {}
	
    @Before("forControllerPackage()")
    public void logBefore(JoinPoint joinPoint) {
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        LOGGER.info("Controller: {} | Method: {} | Timestamp: {}", controllerName, methodName, new Date());
    }
    
    /**
     * Advice that runs after any method matched by the pointcut completes successfully.
     */
    @AfterReturning("forControllerPackage()")
    public void logAfterReturning(JoinPoint joinPoint) {
        String controllerName = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        LOGGER.info("Controller: {} | Method: {} | Status: Completed", controllerName, methodName);
    }

}
