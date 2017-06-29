package utils.message;

/**
 * AbstractMessageHandler is an abstract class of both client and server message handler chain, as well as the handler nodes registered on the chain.
 * @author QQZhao
 *
 */
public abstract class AbstractMessageHandler {
	
	public AbstractMessageHandler nextHandler;	
	public abstract void process(String message);
	
}
