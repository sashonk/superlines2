/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package superlines.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Sashonk
 */
public class Application {
    private static Log log = LogFactory.getLog(Application.class);
    
    public static void exit(int code){
        log.debug(String.format("application terminated with code %d", code));
        System.exit(code);
    }
}
