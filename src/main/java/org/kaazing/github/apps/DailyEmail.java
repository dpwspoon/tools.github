package org.kaazing.github.apps;

import org.kaazing.github.*;
import org.kohsuke.github.*;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


public class DailyEmail {
	private String htmlText  = "";
	private String plainText = "";
	private List<GHUser>  users             = new ArrayList<GHUser>();
	private List<GHIssue> pullRequests      = new ArrayList<GHIssue>();
	private List<GHIssue> updatedOpenIssues = new ArrayList<GHIssue>();
	private List<GHIssue> backlogOpenIssues = new ArrayList<GHIssue>();
	private List<GHIssue> newlyClosedIssues = new ArrayList<GHIssue>();
	private List<GHIssue> escalations       = new ArrayList<GHIssue>();
	//private List<GHIssue> newlyFiledBugs    = new ArrayList<GHIssue>();
	
	
    public static void main(String... args) throws Exception {
        DailyEmail email = new DailyEmail();
        
        email.gatherData();
        if (args.length > 0)
        	email.compileMessage(args[0]);
        else
        	email.compileMessage(null);
        email.send();
    }
    
    // gather all data
    // currently download open pull requests, issues updated recently, backlog issues w/ milestones, and issues closed recently
    private void gatherData() throws IOException {
    	
    	List<GHRepository> repositories;
    	repositories = GitHub.connect().getOrganization("kaazing").listRepositories().asList();
    	repositories.addAll(GitHub.connect().getOrganization("kaazing-private").listRepositories().asList());
    	repositories.addAll(GitHub.connect().getOrganization("k3po").listRepositories().asList());
    	
    	// Get all open pull requests, get open issues, get issues closed w/i day, get issues updated within day
    	for (GHRepository repo : repositories) {
    		List<GHIssue> temp = repo.getIssues(GHIssueState.OPEN);
    		for (GHIssue issue : temp) {
    			
    			if (issue.isPullRequest())
    				pullRequests.add(issue);
    			else {
    				FilterIssues filter = FilterIssues.HAS_LABEL;
    				String[] args = {"escalation"};
    				filter.setArgs(args);
    				
    				if (filter.test(issue)) {
    					escalations.add(issue);
    				} else {
    					filter = FilterIssues.UPDATED_WITHIN_DAYS;
    					String[] args2 = {"1"};
    					filter.setArgs(args2);
    					if (filter.test(issue))
    						updatedOpenIssues.add(issue);
    					else if (issue.getMilestone() != null)
    						backlogOpenIssues.add(issue);
    				}
    				
    			}
    			
    		}
    		
    		temp = repo.getIssues(GHIssueState.CLOSED);
    		for (GHIssue issue : temp) {

    			FilterIssues filter = FilterIssues.CLOSED_WITHIN_DAYS;
    			String[] args = {"1"};
				filter.setArgs(args);
    			if (!issue.isPullRequest() && filter.test(issue))
    				newlyClosedIssues.add(issue);
    		}
    	}
    	
    	// gather and sort users with activity
    	for (GHIssue issue : pullRequests) {
    		GHUser user = issue.getAssignee();
			if (user != null && !users.contains(user))
				users.add(user);
    	}
    	
    	for (GHIssue issue : updatedOpenIssues) {
    		GHUser user = issue.getAssignee();
			if (user != null && !users.contains(user))
				users.add(user);
    	}
    	for (GHIssue issue : backlogOpenIssues) {
    		GHUser user = issue.getAssignee();
			if (user != null && !users.contains(user))
				users.add(user);
    	}
    	for (GHIssue issue : newlyClosedIssues) {
    		GHUser user = issue.getAssignee();
			if (user != null && !users.contains(user))
				users.add(user);
    	}
    	
    	Collections.sort(users, new Comparator<GHUser>() {
    		
			@Override
			public int compare(GHUser o1, GHUser o2) {
				return o1.getLogin().compareTo(o2.getLogin());
			}
    	});  	
    }
    
    
    // compiles the text output based on forUserLogin
    // null argument will output ALL Users
    // string will only output matching user logins
    private void compileMessage(String forUserLogin) throws IOException {
    	// Header    
    	
    	plainText += getEscalationsText();
    	
    	plainText += getNoAssigneeText();

    	for (GHUser user : users) {
    		if (forUserLogin != null) {
    	    	if (user.getLogin().equals(forUserLogin))
    	    		plainText += getUserText(user);
        	} else {
        		plainText += getUserText(user);
        	}
    	}
    	
    }
    
    private String getEscalationsText() {
    	String escalationText = "\nESCALATIONS:\n";
    	htmlText += "\n<h4>Escalations</h4>\n";
    	
    	boolean addedSomething = false;
    	
		htmlText += "<ul>\n";
		for (GHIssue issue : escalations) {
    		escalationText += "\t\t" + plainTextForIssue(issue) + "\n";
    		htmlText += "\t<li>"+ htmlTextForIssue(issue) + "</li>\n";
    		addedSomething = true;
    	}
    	htmlText += "</ul>\n";
    	
    	if (addedSomething)
    		return escalationText;
    	return "";
    	
    }
    
    // get/save text for GHUser
    // plain text is returned, html is saved
    private String getUserText(GHUser user) {
    	String userText = "\n" + user.getLogin() + ":\n";
    	htmlText += "\n<h4>" + user.getLogin() + "</h4>\n";
    			
		boolean addedSomething = false;
		
		userText += "\n\tOPEN PULL REQUESTS:\n";
		htmlText += "<ul>\n\t<li>Open Pull Requests\n\t\t<ul>\n";
    	for (GHIssue pull : pullRequests) {
    		if (user.equals(pull.getAssignee())) {
    			userText += "\t\t" + plainTextForIssue(pull) + " -- submitted by: " + pull.getUser().getLogin() + "\n";
    			htmlText += "\t\t\t<li>"+ htmlTextForIssue(pull) + " <i>submitted by: <b>" + pull.getUser().getLogin() + "</b></i></li>\n";
    			addedSomething = true;
    		}
    	}
    	htmlText += "\t\t</ul>\n\t</li>\n";
    	
    	userText += "\n\tISSUES UPDATED RECENTLY:\n";
		htmlText += "\t<li>Issues Updated Recently\n\t\t<ul>\n";
		for (GHIssue issue : updatedOpenIssues) {
    		if (user.equals(issue.getAssignee())) {
    			userText += "\t\t" + plainTextForIssue(issue) + "\n";
    			htmlText += "\t\t\t<li>"+ htmlTextForIssue(issue) + "</li>\n";
    			addedSomething = true;
    		}
    	}
    	htmlText += "\t\t</ul>\n\t</li>\n";
    	
    	userText += "\n\tBACKLOG ISSUES:\n";
		htmlText += "\t<li>Backlog Issues\n\t\t<ul>\n";
    	for (GHIssue issue : backlogOpenIssues) {
    		if (user.equals(issue.getAssignee())) {
    			userText += "\t\t" + plainTextForIssue(issue) + "\n";
    			htmlText += "\t\t\t<li>"+ htmlTextForIssue(issue) + "</li>\n";
    			addedSomething = true;
    		}
    	}
    	htmlText += "\t\t</ul>\n\t</li>\n";
    	
    	userText += "\n\tISSUES CLOSED RECENTLY:\n";
		htmlText += "\t<li>Issues Closed Recently\n\t\t<ul>\n";
    	for (GHIssue issue : newlyClosedIssues) {
    		if (user.equals(issue.getAssignee())) {
    			userText += "\t\t" + plainTextForIssue(issue) + "\n";
    			htmlText += "\t\t\t<li>"+ htmlTextForIssue(issue) + "</li>\n";
    			addedSomething = true;
    		}
    	}
    	htmlText += "\t\t</ul>\n\t</li>\n</ul>\n";
    	
    	if (addedSomething)
    		return userText;
    	return "";
    }
    
    // get/save text for no assignee
    // plain text is returned, html is saved
    private String getNoAssigneeText() {
    	String userText;
    	userText = "\nNO ASSIGNEE:\n";
    	htmlText += "\n<h4>NO ASSIGNEE</h4>\n";

		boolean addedSomething = false;
		
		userText += "\n\tOPEN PULL REQUESTS:\n";
		htmlText += "<ul>\n\t<li>Open Pull Requests\n\t\t<ul>\n";
    	for (GHIssue pull : pullRequests) {
    		if (null == pull.getAssignee()) {
    			userText += "\t\t" + plainTextForIssue(pull) + "\n";
    			htmlText += "\t\t\t<li>"+ htmlTextForIssue(pull) + " <i>submitted by: <b>" + pull.getUser().getLogin() + "</b></i></li>\n";
    			addedSomething = true;
    		}
    	}
    	htmlText += "\t\t</ul>\n\t</li>\n";

    	userText += "\n\tISSUES UPDATED RECENTLY:\n";
		htmlText += "\t<li>Issues Updated Recently\n\t\t<ul>\n";
    	for (GHIssue issue : updatedOpenIssues) {
    		if (null == issue.getAssignee()) {
    			userText += "\t\t" + plainTextForIssue(issue) + "\n";
    			htmlText += "\t\t\t<li>"+ htmlTextForIssue(issue) + "</li>\n";
    			addedSomething = true;
    		}
    	}
    	htmlText += "\t\t</ul>\n\t</li>\n";

    	userText += "\n\tISSUES CLOSED RECENTLY:\n";
		htmlText += "\t<li>Issues Closed Recently\n\t\t<ul>\n";
    	for (GHIssue issue : newlyClosedIssues) {
    		if (null == issue.getAssignee()) {
    			userText += "\t\t" + plainTextForIssue(issue) + "\n";
    			htmlText += "\t\t\t<li>"+ htmlTextForIssue(issue) + "</li>\n";
    			addedSomething = true;
    		}
    	}
    	htmlText += "\t\t</ul>\n\t</li>\n</ul>\n";

    	if (addedSomething)
    		return userText;
    	return "";
    }
    
    // generates plain text for issue
    private String htmlTextForIssue(GHIssue issue) {
    	String html = "";
    	html += "<span style =\"line-height:150%\">";
    	html += "<a href=\"" + issue.getRepository().getHtmlUrl() + "\"><strong>" + issue.getRepository().getName() + "</strong></a>";
    	html += "<a href=\"" + issue.getHtmlUrl() + "\">  #" + issue.getNumber() + " " + issue.getTitle() + " </a>";
    	try {
			for (GHLabel label : issue.getLabels()) {
				html += "<span style=\"background-color: #" + label.getColor() + "; color: " + colorForBackground(label.getColor()) + "; padding: 2px 2px; border-radius: 2px 2px 2px 2px\">" + label.getName() + "</span> ";
			}
		} catch (IOException e) {
			
		}
    	GHMilestone milestone = issue.getMilestone();
    	if (milestone != null)
    		html += " <i>" + milestone.getTitle() + "</i>";

    	html += "</span>";
    	return html;
    }
    
    // generates plain text for issue
    private String plainTextForIssue(GHIssue issue) {
    	String text = issue.getRepository().getName() + "#" + issue.getNumber() + " -- " + issue.getTitle();
    	try {
			for (GHLabel label : issue.getLabels()) {
				text += " / " + label.getName();
			}
		} catch (IOException e) {
			
		}
    	return text;
    }
    
    // Outputs plain text to command line, and saves html to file (potentially configure to email in future?)
    private void send() throws FileNotFoundException {
    	System.out.println(plainText);
    	System.out.println("done\n\n\n");
    	
    	PrintStream out = new PrintStream("out_email.html");
    	out.println(htmlText);
    	out.close();
    }
    
    // Helper to determine black or white text on labels
    private String colorForBackground(String hex) {
    	// convert hex string to int
    	int rgb = Integer.parseInt(hex, 16);

    	Color c = new Color(rgb);

    	double brightness = 0.299*c.getRed() + 0.587*c.getGreen() + 0.114*c.getBlue();

    	if (brightness > 200) {
    		return "black";
    	} else {
    		return "white";
    	}
    }
}
