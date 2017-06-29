package utils.validator;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * 
 * @author QQZhao
 *
 */
public class PortValidator {
	
	/**
	 * {@link #isValidPort(String)}} method validate whether a port number is available for server to start.
	 * @param port
	 * @return
	 */
	public static boolean isValidPort(String port){
		boolean formatValidationSuccess = (StringValidator.isNotEmpty(port) 
										&& StringValidator.isNumber(port)
										&& StringValidator.isBetweenRange(port, 1024, 65535));
		
		if(!formatValidationSuccess){
			return false;
		}
		
		boolean portAvailabilityValidationSuccess = false;
		ServerSocket s = null;
		try {
	        s = new ServerSocket(Integer.parseInt(port));
	    } catch (IOException e) {
	    } finally {
	        if( s != null){
	            try {
	                s.close();
	                portAvailabilityValidationSuccess = true;
	            } catch (IOException e){
	            }
	        }
	    }
		
		return portAvailabilityValidationSuccess;
	}
}
