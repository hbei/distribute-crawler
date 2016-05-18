package io.github.liuzm.crawler.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.Loader;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;
public class LoggerUtil {
   
    public static Logger createLoggerByName(String name) {
        // 生成新的Logger
        // 如果已經有了一個Logger實例返回現有的
        Logger logger = Logger.getLogger(name);
        // 清空Appender。特別是不想使用現存實例時一定要初期化
        //logger.removeAllAppenders();
        // 設定Logger級別。
        
        SAXReader reader = new SAXReader();
		String fileName = Loader.getResource("log4j.xml").getFile();
		File xmlFile = new File(fileName);
		FileInputStream fis = null;
		try {
			reader.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd",
					false);
			fis = new FileInputStream(xmlFile);
			Document doc = (Document) reader.read(fis);
			List errorOut = doc.selectNodes("/log4j:configuration/appender[@name=\"bootstrap\"]/filter/param[@name=\"LevelMin\"]");
			String level = ((Element) errorOut.get(0)).attributeValue("value");
			logger.setLevel(Level.toLevel(level));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
        
        
        // 設定是否繼承父Logger。
        // 默認為true。繼承root輸出。
        // 設定false後將不輸出root。
        logger.setAdditivity(true);
        // 生成新的Appender
        FileAppender appender = new RollingFileAppender();
        PatternLayout layout = new PatternLayout();
        // log的输出形式
        String conversionPattern = "%d{yyyy.MM.dd HH:mm:ss} %m%n";
        layout.setConversionPattern(conversionPattern);
        appender.setLayout(layout);
        // log输出路径
        appender.setFile(System.getProperty("user.dir")+ "/logs/" + name +DateTimeUtil.getDateTime("yyyyMMddHHmmss") + ".log");
        // log的文字码
        appender.setEncoding("UTF-8");
        // true:在已存在log文件后面追加 false:新log覆盖以前的log 
        appender.setAppend(true);
        // 适用当前配置
        appender.activateOptions();
        // 将新的Appender加到Logger中
        logger.addAppender(appender);
        return logger;
    }
}