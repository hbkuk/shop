package com.shop.common.reTry;

import com.shop.common.exception.ErrorType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.StaleObjectStateException;
import org.springframework.core.Ordered;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Getter
@Setter
@Aspect
@Slf4j
@Component
public class TryAgainAspect implements Ordered {

    private int maxRetries;
    private int order = 1;

    @Pointcut("@annotation(IsTryAgain)")
    public void retryOnOptFailure() {
    }

    @Around("retryOnOptFailure()")
    public Object doConcurrentOperation(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object target = joinPoint.getTarget();
        Method currentMethod = target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
        IsTryAgain annotation = currentMethod.getAnnotation(IsTryAgain.class);
        this.setMaxRetries(annotation.tryTimes());

        int numAttempts = 0;
        do {
            numAttempts++;
            try {
                return joinPoint.proceed();
            } catch (ObjectOptimisticLockingFailureException | StaleObjectStateException exception) {
                if (numAttempts > maxRetries) {
                    throw new NoMoreTryException(ErrorType.NO_MORE_TRY);
                } else {
                    log.info("0 === retry ===" + numAttempts + "times");
                }
            }
        } while (numAttempts <= this.maxRetries);

        return null;
    }

}