package client.controller;

import client.view.ClientFrame;
import utils.message.AbstractMessageHandler;

/**
 * ClientMessageHandlersChain is an implementation of chain of responsibility.
 * By default, an EmptyHandler is registered as the last node.  
 * Handlers handling different messages, as long as it extends the AbstractMessageHandler, can be registered (inserted) in the first position of chain.
 * Once a message arrives, the chain will match the type of message to the corresponding handler by scanning from the first node to the last node.
 * @author QQZhao
 *
 */
public class ClientMessageHandlersChain extends AbstractMessageHandler{
	
	private ClientFrame clientChatView;
	private Thread thread;
	private AbstractMessageHandler nextHandler;
		
	public ClientMessageHandlersChain(ClientFrame clientChatView, Thread thread){		
		this.clientChatView = clientChatView;
		this.thread = thread;
		this.nextHandler = new EmptyHandler(this.clientChatView);
	}
	
	public void process(String message){
		this.nextHandler.process(message);
	}
	
	/**
	 * {@link #register(AbstractMessageHandler)} method adds (inserts) the new handler, which should be a subclass of AbstractMessageHandler, to the ClientMessageHandlersChain.
	 * @param newHandler
	 */
	public void register(AbstractMessageHandler newHandler){
		newHandler.nextHandler = this.nextHandler; 
		this.nextHandler = newHandler;		
	}
	
}

/**
 * The EmptyHandler class is the last node in ClientMessageHandlersChain.
 * It does nothing but printing out the message it received.
 * Since it is the last node in chain, it should not be reached ideally. Once it is reached, it means the message can not be handled  by the previous registered handler.
 * @author QQZhao
 *
 */
class EmptyHandler extends AbstractMessageHandler{
	
	private ClientFrame clientChatView;
	public AbstractMessageHandler nextHandler;
	
	public EmptyHandler(ClientFrame clientChatView){
		this.clientChatView = clientChatView;
		this.nextHandler = null;
	}
	
	@Override
	public void process(String message) {
		System.out.println("ERROR: Message can not be handled! message: " + message);
		this.clientChatView.showCanNotHandleMessageError();
		return;
	}
	
}
