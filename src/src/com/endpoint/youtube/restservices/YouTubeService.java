package com.endpoint.youtube.restservices;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path; 
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.guava.Lists;

import com.endpoint.youtube.common.Entry;
import com.endpoint.youtube.feedreader.JsonFeedReader;  
@Path("/YouTubeService") 

public class YouTubeService {
	
	private final String url = "https://jon.endpoint.com/youtube-popular-20121222.json";
	private final int intervalMiliSeconds = 60000;
	

	public YouTubeService(@Context ServletContext servletContext)
	{
		/* I tried to get url and interval as a parameter from web.xml. But could not get it work. So I commented out
	    	this.url = servletContext.getInitParameter("url");
	    	this.intervalMiliSeconds = Integer.parseInt(servletContext.getInitParameter("intervalMiliSeconds"));
	    */
		// First invocation to initialize values in the  JsonFeedReader. if not called once. First request gets default results 
		JsonFeedReader reader = JsonFeedReader.getInstance(url, intervalMiliSeconds);
	}
	
	
	@GET 
	@Path("/MostPopularVideo") 
	@Produces(MediaType.APPLICATION_JSON) 
	public Response getMostPopularVideo(){ 
		return handleRequest();
    }

	
	@POST // Both post and get requests are handled
	@Path("/MostPopularVideo") 
	@Produces(MediaType.APPLICATION_JSON)
	public Response getMostPopularVideoPost(){ 
		return handleRequest();
    }
	
	
	private Response handleRequest() {
		JsonFeedReader reader = JsonFeedReader.getInstance(url, intervalMiliSeconds);
		Entry entry = new Entry();
		entry.setViewedCount(reader.getMaxViewedCount());
		entry.setTitle(reader.getMaxViewedTitle());
		List<Entry> result = new ArrayList<Entry>();
		result.add(entry);
		GenericEntity<List<Entry>> entity = new GenericEntity<List<Entry>>(Lists.newArrayList(result)) {};
		return Response.ok().entity(entity).build();
	}
	
	
	// Other services can go here in the future

}
