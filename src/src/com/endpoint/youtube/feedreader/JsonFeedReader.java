package com.endpoint.youtube.feedreader;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import com.endpoint.youtube.common.Entry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Salim Asan
 */
public class JsonFeedReader extends TimerTask {

    private List<Entry> entryList = new ArrayList<>();
    private long maxViewedCount = 0;
    private String maxViewedTitle = "";
    private final String  urlStr;
    private static JsonFeedReader instance;
    
    private JsonFeedReader(String url){
        this.urlStr = url;
    }
    
    public static synchronized JsonFeedReader getInstance(String url, int intervalMiliSeconds ){
        if(instance == null){
            instance = new JsonFeedReader(url);
            instance.run(); // To make sure results are gathered. Otherwise first request gets default values
            Timer timer = new Timer(false);
            timer.scheduleAtFixedRate(instance, 0, intervalMiliSeconds);
        }
        return instance;
    }
    
    @Override
    public void run()  {
        ObjectMapper mapper = new ObjectMapper(); 
        JsonNode rootNode;
	try{
            URL url = new URL(this.urlStr);
            rootNode  = mapper.readTree(url);	        
        } catch (IOException exc){
            System.out.println(exc.getStackTrace());
            // send notification email and sms
            // Please note that in case of a netwok failure we will continue to service with stale data
            // I think it is much better than our Json service fails to serve the results
            return;
        }
        entryList.clear();
        Iterator<JsonNode> iter = rootNode.findValues("entry").get(0).iterator();
        while (iter.hasNext()) {
           JsonNode node  =  iter.next();
           Entry entry = new Entry();
           entry.setViewedCount(node.findValue("viewCount").asLong());
           entry.setTitle(node.findValue("title").findValue("$t").asText());
           entryList.add(entry);
            if  (entry.getViewedCount() > maxViewedCount) {
                maxViewedCount = entry.getViewedCount();
                maxViewedTitle = entry.getTitle();
            }
        } 
    }
        
    public List<Entry> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<Entry> entryList) {
        this.entryList = entryList;
    }

    public long getMaxViewedCount() {
        return maxViewedCount;
    }

    public void setMaxViewedCount(long maxViewedCount) {
        this.maxViewedCount = maxViewedCount;
    }

    public String getMaxViewedTitle() {
        return maxViewedTitle;
    }

    public void setMaxViewedTitle(String maxViewedTitle) {
        this.maxViewedTitle = maxViewedTitle;
    }
    
}
