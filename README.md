# tools.github
A repo that allows running automated Actions on a subset of github repos via Filters

# To Build
From top level run mvn clean install

# Usage

`java -jar tools.github-1.0.0-SNAPSHOT.jar -u USERNAME -f FILTER -a ACTION`

Using the jar generated in target (tools.github-1.0.0-SNAPSHOT.jar)

Place your credentials in ~/.github as 

```
login=<username>
password=<password>
```

usage:  [-a <arg>] [-f <arg>] [-if <arg>] [-list <arg>] [-of <arg>] [-u <arg>]
 -a,--action <arg>         action to do when found
 -f,--filter <arg>         filter to apply in search
 -list,--help <arg>        list (actions) or (options)
 -u,--user <arg>           user/organization to apply filter to

-f is a filter
-a is an action

example use cases

clone all private repos
`java -jar tools.github-develop-SNAPSHOT.jar -u dpwspoon -a clone\(myRepos\) -f private`

delete specific repo
`java -jar tools.github-develop-SNAPSHOT.jar -u dpwspoon -a delete -f byName\(gateway.bridge\)`

find repos that have .travis.yml files
`java -jar target/tools.github-develop-SNAPSHOT.jar -u dpwspoon -f hasFile\(/,.travis.yml\) -a print-names`

finds issues that have been updated in last day
`java -jar target/tools.github-develop-SNAPSHOT.jar -u dpwspoon -f all -a list-issues-recently-updated`

finds issues labeled as `bug` that have not yet been assigned
`java -jar target/tools.github-develop-SNAPSHOT.jar -u dpwspoon -f all -a list-bugs-not-assigned`

finds issues with milestone `S10 - 15`
`java -jar target/tools.github-develop-SNAPSHOT.jar -u dpwspoon -f all -a list-issues-by-milestone\(S10\ -\ 15\)`

# List of Filters
```
all                 		    Matches all repos
private		                    Matches private repos
hasFile(path,file-name)		    filters repositories that has file
public		                    Matches public repos
byName(name, name2 etc)	        gets a repo matching the name
```
# List of Actions
```
print-names:	                	prints the name of the repo
addTeam(organization,team):	    	Adds a team from an organization to a repository
clone(directory):               	clones the repo to a directory/name-of-repo
delete:                         	deletes the repo
listOpenPullRequests            	lists open pull requests
listOpenIssues                  	lists open issues (NOT IMPLEMENTED)
list-bugs-not-assigned				lists bugs not assigned
list-issues-recently-closed			lists issues recently closed (1 day)
list-issues-recently-updated		lists issues recently closed (1 day)
list-issues-by-milestone(milestone)	lists issues matching argument milestone
```

# List of Issue Filters
Note: These are not accesible from the command line, rather actions or other apps can build with them.
```
ALL 								matches all issues
OPEN  								matches open issues
CLOSED 								matches closed issues
CLOSED_WITHIN_DAYS(numberDays)		matches issues closed within argument days	
UPDATED_WITHIN_DAYS(numberDays)		matches issues updated within argument days
HAS_LABEL(label)					matches issues containing argument label
HAS_ASSIGNEE(login)					login of user, null argument returns issues with no assignee
BY_MILESTONE(milestone)				filters by milestone
BY_NAME								gets issue matching the name
```

# Apps
### DailyEmail.java

```
cd target && java -cp tools.github-develop-SNAPSHOT.jar org.kaazing.github.apps.DailyEmail && cd ..
```

Compiles and outputs both plain text (command line) and html (out_email.html) of users and key issues.
Currently the app is programmed to search for the following by user:

	1. Open Pull Requests
	2. Issues Updated Recently (within a day)
	3. Backlog Issues (issues not updated within a day but have a milestone)
	4. Issues Closed Recently (within a day)

The `main` takes one optional argument for the login of a user. If a string is passed, the program will
only output the issues (above) for no assignee and for the argument user. If no argument is passed, the
program will output no assignee and then all users.

Future: Perhaps configure utility to send out an email given the html and the plain text output.

# NOTES
Mileage may vary.  Most of these have not been fully tested yet.  Also, the lib is best used to write your own Java Program
