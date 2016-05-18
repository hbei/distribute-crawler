package io.github.liuzm.crawler.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class DomUtils {
	
	
	public static boolean createXmlFile(String path, String filename,Document document){
		String file = StringUtils.isNotBlank(path)?path+filename:filename;
		XMLWriter writer = null;
		  try{
	           /** 将document中的内容写入文件中 */
			  writer =  new XMLWriter(new FileWriter(new File(file)), OutputFormat.createPrettyPrint());
	           writer.write(document);
	          
	           /** 执行成功,需返回1 */
	           return true;
	       }catch(Exception ex){
	           ex.printStackTrace();
	       }finally{
	    	   try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       }
		  return false;
	}
	
	public static void printXML(Document document){
		XMLWriter writer = null;
		try {
			writer =  new XMLWriter(System.out,OutputFormat.createPrettyPrint());
			writer.write(document);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void printXML(Element element){
		XMLWriter writer = null;
		try {
			writer =  new XMLWriter(System.out,OutputFormat.createPrettyPrint());
			writer.write(element);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static void main(String[] args) {
		Document document = DocumentHelper.createDocument();
	       /** 建立XML文档的根books */
	       Element booksElement = document.addElement("books");
	       /** 加入一行注释 */
	       booksElement.addComment("This is a test for dom4j, holen, 2004.9.11");
	       /** 加入第一个book节点 */
	       Element bookElement = booksElement.addElement("book");
	       /** 加入show属性内容 */
	       bookElement.addAttribute("show","yes");
	       /** 加入title节点 */
	       Element titleElement = bookElement.addElement("title");
	       /** 为title设置内容 */
	       titleElement.setText("Dom4j Tutorials");
	      
	       /** 类似的完成后两个book */
	       bookElement = booksElement.addElement("book");
	       bookElement.addAttribute("show","yes");
	       titleElement = bookElement.addElement("title");
	       titleElement.setText("Lucene Studing");
	       bookElement = booksElement.addElement("book");
	       bookElement.addAttribute("show","no");
	       titleElement = bookElement.addElement("title");
	       titleElement.setText("Lucene in Action");
	      
	       /** 加入owner节点 */
	       Element ownerElement = booksElement.addElement("owner");
	       ownerElement.setText("O'Reilly");
	       printXML(booksElement);
	}

}
