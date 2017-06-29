package utils.validator;

/**
 * 
 * @author QQZhao
 *
 */
public class StringValidator {
	
	/**
	 * {@link #isNotEmpty(String)} validate if a string is null or an empty string.
	 * @param str
	 * @return true if a string is not empty.
	 */
	public static boolean isNotEmpty(String str){
		if(str == null || str.equals("")){
			return false;
		}
		return true;
	}
	
	/**
	 * {@link #isNumber(String)} validate if a string can be convert to a Integer.
	 * @param str
	 * @return true if valid.
	 */
	public static boolean isNumber(String str){

		try{
			Integer.valueOf(str);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**
	 * {@link #isBetweenRange(String, int, int)} validate if a string passed in is between the start and end.
	 * @param str
	 * @param start
	 * @param end
	 * @return true if valid
	 */
	public static boolean isBetweenRange(String str, int start, int end){
		if(!isNumber(str)){
			return false;
		}
		int targetNum = Integer.parseInt(str);
		if(targetNum <= start || targetNum > end){
			return false;
		}
		return true;
	}
	
}
