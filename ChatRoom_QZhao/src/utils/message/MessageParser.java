package utils.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * MessageParser is a class for message construction and analysis in format of XML, using use JDOM library.
 * @author QQZhao
 *
 */
public class MessageParser {

	private static Document decode(String message) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();		
		Document doc = builder.build(new ByteArrayInputStream(message.getBytes()));
		return doc;
	}
	
	public static String convertDocToString(Document doc){
		Format format = Format.getRawFormat();
		XMLOutputter out = new XMLOutputter(format);
		return out.outputString(doc);	
	}
	
	/**
	 * {@link #getElementValueByName(String, String)} method extract the value from message in format of XML based on name
	 * @param message
	 * @param elementName
	 * @return
	 */
	public static String getElementValueByName(String message, String elementName) {
		try {
			return decode(message).getRootElement().getChild(elementName).getText();
		} catch (JDOMException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * {@link #getElementsListValueByName(String, String)} method extract the values from message in format of XML based on name, and returned as list.
	 * @param message
	 * @param elementName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getElementsListValueByName(String message, String elementName){
		
		List<String> resultList = new ArrayList<>();
		
		try {
			List<Element> childrenElements = decode(message).getRootElement().getChildren(elementName);
			
			for(Element element : childrenElements){
				resultList.add(element.getText());
			}
			
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 * The overload {@link #buildMessage(String, String)} method constructs an xml message having one element.
	 * @param element_1_name
	 * @param element_1_text
	 * @return
	 */
	public static String buildMessage(String element_1_name, String element_1_text){
		Document doc = new Document();
		
		Element root = new Element("root");
		doc.addContent(root);
		
		Element element_1 = new Element(element_1_name).setText(element_1_text);
		root.addContent(element_1);
		
		return convertDocToString(doc);	
	}
	
	/**
	 * The overload {@link #buildMessage(String, String, String, String)} method constructs an xml message having two elements.
	 * @param element_1_name
	 * @param element_1_text
	 * @param element_2_name
	 * @param element_2_text
	 * @return
	 */
	public static String buildMessage(String element_1_name, String element_1_text, String element_2_name, String element_2_text){
		Document doc = new Document();
		
		Element root = new Element("root");
		doc.addContent(root);
		
		Element element_1 = new Element(element_1_name).setText(element_1_text);
		Element element_2 = new Element(element_2_name).setText(element_2_text);
		root.addContent(element_1).addContent(element_2);
		
		return convertDocToString(doc);	
	}
	
	/**
	 * The overload {@link #buildMessage(String, String, String, String, String, String)} method constructs an xml message having three elements.
	 * @param element_1_name
	 * @param element_1_text
	 * @param element_2_name
	 * @param element_2_text
	 * @param element_3_name
	 * @param element_3_text
	 * @return
	 */
	public static String buildMessage(String element_1_name, String element_1_text, 
										String element_2_name, String element_2_text,
										String element_3_name, String element_3_text){
		Document doc = new Document();
		
		Element root = new Element("root");
		doc.addContent(root);
		
		Element element_1 = new Element(element_1_name).setText(element_1_text);
		Element element_2 = new Element(element_2_name).setText(element_2_text);
		Element element_3 = new Element(element_3_name).setText(element_3_text);
		root.addContent(element_1).addContent(element_2).addContent(element_3);
		
		return convertDocToString(doc);	
	}
	
	/**
	 * The overload {@link #buildMessage(String, String, String, String, String, String)} method constructs an xml message having four elements.
	 * @param element_1_name
	 * @param element_1_text
	 * @param element_2_name
	 * @param element_2_text
	 * @param element_3_name
	 * @param element_3_text
	 * @param element_4_name
	 * @param element_4_text
	 * @return
	 */
	public static String buildMessage(String element_1_name, String element_1_text, 
											String element_2_name, String element_2_text,
											String element_3_name, String element_3_text,
											String element_4_name, String element_4_text){
			Document doc = new Document();
			
			Element root = new Element("root");
			doc.addContent(root);
			
			Element element_1 = new Element(element_1_name).setText(element_1_text);
			Element element_2 = new Element(element_2_name).setText(element_2_text);
			Element element_3 = new Element(element_3_name).setText(element_3_text);
			Element element_4 = new Element(element_4_name).setText(element_4_text);
			root.addContent(element_1).addContent(element_2).addContent(element_3).addContent(element_4);
			
			return convertDocToString(doc);	
			}
	
	/**
	 * The {@link #buildUserNameListMessage(String, String, String, List, String, String) }method constructs an xml message having list elements.
	 * This method is specifically used for building the username list message.
	 * @param element_1_name
	 * @param element_1_text
	 * @param element_2_name
	 * @param element_2_text_as_list
	 * @param element_3_name
	 * @param element_3_text
	 * @return
	 */
	public static String buildUserNameListMessage(String element_1_name, String element_1_text, 
													String element_2_name, List<String> element_2_text_as_list,
													String element_3_name, String element_3_text){
		
		Document doc = new Document();		
		Element root = new Element("root");
		doc.addContent(root);
		
		Element element_1 = new Element(element_1_name).setText(element_1_text);
		root.addContent(element_1);
		
		for (String currentUsername : element_2_text_as_list){
			Element currentUserElement = new Element(element_2_name).setText(currentUsername);
			root.addContent(currentUserElement);
		}
		
		Element element_3 = new Element(element_3_name).setText(element_3_text);
		root.addContent(element_3);
		
		return convertDocToString(doc);
	}
}
