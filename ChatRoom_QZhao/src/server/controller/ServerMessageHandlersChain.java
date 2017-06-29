package server.controller;

import server.view.ServerFrame;
import utils.message.AbstractMessageHandler;

/**
 * ServerMessageHandlersChain has a linked list structure.
 * Each registered node via {@link #register(AbstractMessageHandler)}, which is sub-class of AbstractMessageHandler will handles message it is capable of.
 * The Terminal of this linked list is a EmptyHandler. 
 * 
 * @author QQZhao
 *
 */
public class ServerMessageHandlersChain extends AbstractMessageHandler{
	
	private ServerFrame serverFrameView;
	private AbstractMessageHandler nextHandler;
		
	public ServerMessageHandlersChain(ServerFrame serverFrameView){		
		this.serverFrameView = serverFrameView;
		this.nextHandler = new EmptyHandler(this.serverFrameView);
	}
	
	public void process(String message){
		this.nextHandler.process(message);
	}
	
	/**
	 * {@link #register(AbstractMessageHandler)} insert a new handler to the head position of the chain.
	 * @param newHandler
	 */
	public void register(AbstractMessageHandler newHandler){
		newHandler.nextHandler = this.nextHandler; 
		this.nextHandler = newHandler;		
	}	
}

/**
 * EmptyHandler is the last node in the chain structure. The {@link #process(String)} method just prints out message.
 * This EmptyHandler should never be reached since all valid message are supposed to be handled by nodes in the front.
 * @author QQZhao
 *
 */
class EmptyHandler extends AbstractMessageHandler{
	
	public AbstractMessageHandler nextHandler;
	
	public EmptyHandler(ServerFrame serverFrameView){
		this.nextHandler = null;
	}

	@Override
	public void process(String message) {
		System.out.println("ERROR: Message can not be handled by server! message: " + message);
		return;
	}
	
}

