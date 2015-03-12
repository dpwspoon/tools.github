# tools.github
A repo that allows running automated Actions on a subset of github repos via Filters

# To Build
From top level run mvn clean install

# Usage

`java -jar tools.github-1.0.0-SNAPSHOT.jar -u USERNAME -f FILTER -a ACTION`

Using the jar generated in target (tools.github-1.0.0-SNAPSHOT.jar)

usage:  [-a <arg>] [-f <arg>] [-if <arg>] [-list <arg>] [-of <arg>] [-u <arg>]
 -a,--action <arg>         action to do when found
 -f,--filter <arg>         filter to apply in search
 -list,--help <arg>        list (actions) or (options)
 -u,--user <arg>           user/organization to apply filter to

-f is a filter
-a is an action

example use cases

clone all private repos
`java -jar tools.github-1.0.0-SNAPSHOT.jar -u dpwspoon -a clone\(myRepos\) -f private`

delete specific repo
`java -jar tools.github-1.0.0-SNAPSHOT.jar -u dpwspoon -a delete -f byName\(gateway.bridge\)`

find repos that have .travis.yml files
java -jar target/tools.github-1.0.0-SNAPSHOT.jar -u dpwspoon -f hasFile\(/,.travis.yml\) -a print-names

# List of Filters
all: Matches all repos

private:	Matches private repos

hasFile(path,file-name): filters repositories that has file

public: Matches public repos

byName(name): gets a repo matching the name


# List of Actions
print-names:	prints the name of the repo

addTeam(organization,team):	Adds a team from an organization to a repository

clone(directory): clones the repo to a directory/name-of-repo

delete: deletes the repo

# NOTES
Mileage may vary.  Most of these have not been fully tested yet.  Also, the lib is best used to write your own Java Program
