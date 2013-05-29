/**
 * <p> AOP 에러메시지 구현 (메소드에 대한 에러메시지 표시)
 * @author coolang(김유근)
 */

package web.common.advice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;


public class EzMailLoggingAdvice implements MethodInterceptor{

	
	private Logger logger = Logger.getLogger(this.getClass());

    /*
     * (non-Javadoc)
     * 
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String className = invocation.getThis().getClass().getName();       
        logger.debug(className + " | " + invocation.getMethod().getName() + " method start!!");
        
        Object retVal = invocation.proceed();
        
        logger.debug(className + " | " + invocation.getMethod().getName()  + " method end!!");

        return retVal;

    }

}
