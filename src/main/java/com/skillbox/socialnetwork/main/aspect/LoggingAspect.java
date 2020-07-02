package com.skillbox.socialnetwork.main.aspect;

import com.skillbox.socialnetwork.main.dto.universal.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Aspect
//https://github.com/eugenp/tutorials/tree/master/spring-aop/src/main/java/com/baeldung
public class LoggingAspect {
    private Log log = LogFactory.getLog(getClass());

    @Pointcut("@annotation(MethodLogWithTime) && args(response,..)")
    public void callAtMyServiceSecurityAnnotation(Response response) { }


    @Around("@annotation(MethodLogWithTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();


        Object proceed = null;
        Throwable toThrow = null;
        String result;
        try {
            proceed = joinPoint.proceed();
            result = mask(proceed.toString());
        }catch (Throwable t){
            toThrow = t;
            result = toThrow.getMessage();
        }
        long executionTime = System.currentTimeMillis() - start;

        String args = mask(Arrays.toString(joinPoint.getArgs()));

        log.info("Method: " + joinPoint.getSignature().getName() + ", args: " +
                args + ", result: " + result + ", executed in " + executionTime + "ms");
        if(toThrow != null)
            throw toThrow;

        return proceed;
    }

    private String mask(String string){
        String result = string;
        if(string.matches(".*(password.*|token)=.*")){
            result = string.replaceAll("(password.*|token)=[^,\\s)]+", "secret=*****");
        }
        return result;
    }

}
