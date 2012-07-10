package testcase;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CalculatorHandler extends IoHandlerAdapter { 
    private static final Logger LOGGER = LoggerFactory 
        .getLogger(CalculatorHandler.class); 

    private ScriptEngine jsEngine = null; 

    public CalculatorHandler() { 
        ScriptEngineManager sfm = new ScriptEngineManager(); 
        jsEngine = sfm.getEngineByName("JavaScript"); 
        if (jsEngine == null) { 
            throw new RuntimeException("ÕÒ²»µ½ JavaScript ÒýÇæ¡£"); 
        } 
    } 

    public void exceptionCaught(IoSession session, Throwable cause) 
        throws Exception { 
        LOGGER.warn(cause.getMessage(), cause); 
    } 

    public void messageReceived(IoSession session, Object message) 
        throws Exception { 
        String expression = message.toString(); 
        if ("quit".equalsIgnoreCase(expression.trim())) { 
            session.close(true); 
            return; 
        } 
        try { 
            Object result = jsEngine.eval(expression); 
            session.write(result.toString()); 
        } catch (ScriptException e) { 
            LOGGER.warn(e.getMessage(), e); 
            session.write("Wrong expression, try again."); 
        } 
    } 
}
