package io.github.liuzm.crawler.vo;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class JobTemplate {

	private String name;
	private String name_comment="任务Id,任务的表识。请输入中文，标识能代表任务的意思";
	private String indexName;
	private String indexName_comment = "任务的名字";
	
	private String jobtime = "-1";
	private String jobtime_comment = "表示爬虫运行指定的时间长度后自动结束.-1表示不启用该特性";
	
	private String urlNum ="-1";
	private String urlNum_comment = "表示抓取指定数量的Url后结束.-1表示不启用该特性";
	
	private FetchTemplate fetch;
	
	private ExtratTemplate extract;
	
	private StoreTemplate storeTemplate;
	
	private String note;
	
	public JobTemplate init(JobInfo info) {
		try {
			Document doc = DocumentHelper.parseText(new String(info.getConfigure(),"utf-8"));
			Element e_job = doc.getRootElement();
			this.name = e_job.attributeValue("name");
			this.jobtime = e_job.elementText("jobtime");
			this.indexName = e_job.attributeValue("indexName");
			this.urlNum = e_job.elementText("urlNum");
			fetch = new FetchTemplate();
			fetch.init(e_job);
			
			extract = new ExtratTemplate();
			extract.init(e_job);
			
			storeTemplate = new StoreTemplate();
			storeTemplate.init(e_job);
			
		} catch (UnsupportedEncodingException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this;
	};
	
	
	public Document createTemplate(JobTemplate saveTemplate) {
		JobTemplate job = saveTemplate;
		Document doc = DocumentHelper.createDocument();
		doc.addComment(StringUtils.isNotBlank(job.getNote())?job.getNote():this.note);
		//job
		Element element = doc.addElement("job");
		element.addAttribute("name",StringUtils.isNotBlank(job.getName())?job.getName():this.name );
		element.addAttribute("indexName",StringUtils.isNotBlank(job.getIndexName())?job.getIndexName():this.indexName );
		//doc.add(element);
		
		//jobtime
		element.addComment(StringUtils.isNotBlank(job.getJobtime_comment())?job.getJobtime_comment():this.jobtime_comment);
		element.addElement("jobtime").addText(StringUtils.isNumeric(job.getJobtime())?job.getJobtime():this.jobtime);
		
		//urlNum
		element.addComment(StringUtils.isNotBlank(job.getUrlNum_comment())?job.getUrlNum_comment():this.urlNum_comment);
		element.addElement("urlNum").addText(StringUtils.isNumeric(job.getUrlNum())?job.getUrlNum():this.urlNum);
		
		//fetch
		if(null!=fetch){
			element.addComment(StringUtils.isNotBlank(job.getFetch().getNote())?job.getFetch().getNote():fetch.getNote());
			element.add(fetch.createTemplate(job.getFetch()));
		}
		
		//extract
		if(extract!=null){
			element.addComment(StringUtils.isNotBlank(job.getExtract().getNote())?job.getExtract().getNote():extract.getNote());
			element.add(extract.createTemplate(job.getExtract()));
		}
		//store
		if(storeTemplate!=null){
			element.add(storeTemplate.createTemplate(job.getStoreTemplate()));
		}
		return doc;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getIndexName() {
		return indexName;
	}


	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}


	public String getJobtime() {
		return jobtime;
	}


	public void setJobtime(String jobtime) {
		this.jobtime = jobtime;
	}


	public String getUrlNum() {
		return urlNum;
	}


	public void setUrlNum(String urlNum) {
		this.urlNum = urlNum;
	}


	public FetchTemplate getFetch() {
		return fetch;
	}


	public void setFetch(FetchTemplate fetch) {
		this.fetch = fetch;
	}


	public ExtratTemplate getExtract() {
		return extract;
	}


	public void setExtract(ExtratTemplate extract) {
		this.extract = extract;
	}


	public String getNote() {
		return note;
	}
	

	public StoreTemplate getStoreTemplate() {
		return storeTemplate;
	}


	public void setStoreTemplate(StoreTemplate storeTemplate) {
		this.storeTemplate = storeTemplate;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public String getName_comment() {
		return name_comment;
	}


	public void setName_comment(String name_comment) {
		this.name_comment = name_comment;
	}


	public String getIndexName_comment() {
		return indexName_comment;
	}


	public void setIndexName_comment(String indexName_comment) {
		this.indexName_comment = indexName_comment;
	}


	public String getJobtime_comment() {
		return jobtime_comment;
	}


	public void setJobtime_comment(String jobtime_comment) {
		this.jobtime_comment = jobtime_comment;
	}


	public String getUrlNum_comment() {
		return urlNum_comment;
	}


	public void setUrlNum_comment(String urlNum_comment) {
		this.urlNum_comment = urlNum_comment;
	}
	
	
	
}
