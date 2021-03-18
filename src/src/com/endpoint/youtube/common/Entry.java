package com.endpoint.youtube.common;

import java.io.Serializable; 
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlRootElement; 
@XmlRootElement(name = "entry") 

public class Entry implements Serializable {

	private static final long serialVersionUID = 1L; 
	
	private long viewedCount = 0;
	private String title;
	
	// Other fields go here
	
	public Entry(){}
	
	public Entry(long viewedCount, String title){  
	     this.viewedCount = viewedCount;
	     this.title = title;
	} 
	
	public long getViewedCount() { 
	      return viewedCount; 
	   } 
	
	@XmlElement
	public void setViewedCount(long viewedCount) { 
	      this.viewedCount = viewedCount;
	} 
	
	public String getTitle() { 
	      return title; 
	   } 
	
	@XmlElement
	public void setTitle(String title) { 
	      this.title = title;
	} 
	
	// Other getter/setters go here

}
