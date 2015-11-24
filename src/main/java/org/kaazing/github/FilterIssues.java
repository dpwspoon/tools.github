/**
 * Copyright 2007-2015, Kaazing Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kaazing.github;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHMilestone;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;

public abstract class FilterIssues extends SubCommand implements Predicate<GHIssue> {

    public static Map<String, FilterIssues> getFiltersByName() {
        Map<String, FilterIssues> filtersByName = new HashMap<>();
        filtersByName.put(ALL.getName(), ALL);
        filtersByName.put(BY_NAME.getName(), BY_NAME);
        return filtersByName;
    }

    public static final FilterIssues ALL = new FilterIssues() {
        @Override
        public boolean test(GHIssue t) {
            return true;
        }

        @Override
        public String getName() {
            return "all";
        }

        @Override
        public String getDescription() {
            return "Matches all repos";
        }

        @Override
        public String listArgs() {
            return "";
        }
    };

    public static final FilterIssues OPEN = new FilterIssues() {
        @Override
        public boolean test(GHIssue t) {
            return t.getState() == GHIssueState.OPEN;
        }

        @Override
        public String getName() {
            return "private";
        }

        @Override
        public String getDescription() {
            return "Matches private repos";
        }

        @Override
        public String listArgs() {
            return "";
        }
    };

    public static final FilterIssues CLOSED = new FilterIssues() {
        @Override
        public boolean test(GHIssue t) {
            return t.getState() == GHIssueState.CLOSED;
        }

        @Override
        public String getName() {
            return "public";
        }

        @Override
        public String getDescription() {
            return "Matches public repos";
        }

        @Override
        public String listArgs() {
            return "";
        }
    };

    public static final FilterIssues CLOSED_WITHIN_DAYS = new FilterIssues() {
        @Override
        public boolean test(GHIssue t) {
        	if (t.getState() == GHIssueState.OPEN)
        		return false;
        	
        	String[] args = getArgs();
            int days = Integer.parseInt(args[0]);
        	
        	Calendar dateClosed = new GregorianCalendar();
        	dateClosed.setTime(t.getClosedAt());
            Calendar cutOffDate = new GregorianCalendar();
            cutOffDate.setTime(new Date());
            cutOffDate.add(Calendar.DAY_OF_MONTH, -days);
            
            if (dateClosed.after(cutOffDate))
            	return true;
            return false;
        }

        @Override
        public String getName() {
            return "closed-within-days";
        }

        @Override
        public String getDescription() {
            return "Issues closed within argument days";
        }

        @Override
        public String listArgs() {
            return "(numberDays)";
        }
    };
    
    public static final FilterIssues UPDATED_WITHIN_DAYS = new FilterIssues() {
    	@Override
        public boolean test(GHIssue t) {
    		String[] args = getArgs();
            int days = Integer.parseInt(args[0]);
            
        	Calendar dateClosed = new GregorianCalendar();
        	try {
        		dateClosed.setTime(t.getUpdatedAt());
        	} catch(IOException e) {
        		System.out.println("This issue did not have updated: " + t.getTitle());
        		return false;
        	}
            Calendar cutOffDate = new GregorianCalendar();
            cutOffDate.setTime(new Date());
            cutOffDate.add(Calendar.DAY_OF_MONTH, -days);
            cutOffDate.add(Calendar.HOUR_OF_DAY, -1);
            
            if (dateClosed.after(cutOffDate))
            	return true;
            return false;
    	}

        @Override
        public String getName() {
            return "updated-within-days";
        }

        @Override
        public String getDescription() {
            return "Issued updated within argument days";
        }

        @Override
        public String listArgs() {
            return "(numberDays)";
        }
    };
    
    public static final FilterIssues HAS_LABEL = new FilterIssues() {
    	@Override
        public boolean test(GHIssue t) {
    		String[] args = getArgs();
            String targetLabel = args[0];
    		
        	Collection<GHLabel> labels = null;
        	try {
        		labels = t.getLabels();
        	} catch(IOException e) {
        		return false;
        	}
        	
        	for (GHLabel label : labels)
        		if (label.getName().equals(targetLabel)) {
        			return true;
        		}
            
            return false;
    	}

        @Override
        public String getName() {
            return "has-label";
        }

        @Override
        public String getDescription() {
            return "filters issues containing argument label";
        }

        @Override
        public String listArgs() {
            return "(label)";
        }
    };
    
    public static final FilterIssues HAS_ASSIGNEE = new FilterIssues() {
    	@Override
        public boolean test(GHIssue t) {
    		String[] args = getArgs();
    		String assignee = null;
    		if (args.length > 0)
    			assignee = args[0];
    		
        	
        	GHUser user = t.getAssignee();
        	if (user == null) {
        		if (assignee == null)
        			return true;
        	} else if (user.getLogin() == assignee) {
        		return true;
        	}
            
            return false;
    	}

        @Override
        public String getName() {
            return "has-label";
        }

        @Override
        public String getDescription() {
            return "filters issues by assignee user login";
        }

        @Override
        public String listArgs() {
            return "(login) [no arguments for no assignee]";
        }
    };
    
    public static final FilterIssues BY_MILESTONE = new FilterIssues() {
    	@Override
        public boolean test(GHIssue t) {
    		String[] args = getArgs();
    	    String milestone = args[0];
    	    
    	    if (milestone == null)
    	    	throw new RuntimeException("milestone needs not null argument");
    	    
        	GHMilestone issueMilestone = t.getMilestone();
        	
        	if (issueMilestone == null) {
        		return false;
        	}
        	
        	if (issueMilestone.getTitle().equals(milestone))
        		return true;
            
            return false;
    	}

        @Override
        public String getName() {
            return "public";
        }

        @Override
        public String getDescription() {
            return "Matches public repos";
        }

        @Override
        public String listArgs() {
			return "(milestone)";
        }
    };
    
    public static final FilterIssues BY_NAME = new FilterIssues() {
        
        @Override
        public boolean test(GHIssue t) {
            List<String> names = Arrays.asList(getArgs());
            if(names.isEmpty()){
                throw new RuntimeException("Filter by name requires non null arg");
            }
            if(names.contains(t.getTitle())){
                return true;
            }
            return false;
        }
        
        @Override
        public String listArgs() {
            return "(name)";
        }
        
        @Override
        public String getName() {
            return "byName";
        }
        
        @Override
        public String getDescription() {
            return "gets a repo matching the name";
        }
    };

}
