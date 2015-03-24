package org.kaazing.github;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHOrganization;
import org.kohsuke.github.GHPullRequest;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTeam;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

public abstract class Action extends SubCommand implements Consumer<GHRepository> {

    public static Map<String, Action> getActionsByName() {
        Map<String, Action> actionsByName = new HashMap<>();
        actionsByName.put(PRINT_NAMES.getName(), PRINT_NAMES);
        actionsByName.put(CLONE.getName(), CLONE);
        actionsByName.put(ADD_TEAM_TO_REPO.getName(), ADD_TEAM_TO_REPO);
        actionsByName.put(DELETE.getName(), DELETE);
        return actionsByName;
    }

    public static Action PRINT_NAMES = new Action() {
        @Override
        public void accept(GHRepository t) {
            System.out.println(t.getFullName());
        }

        @Override
        public String getName() {
            return "print-names";
        }

        @Override
        public String getDescription() {
            return "prints the name of the repo";
        }

        @Override
        public String listArgs() {
            return "";
        }
    };

    public static Action LIST_OPEN_PULL_REQUESTS = new Action() {

        @Override
        public void accept(GHRepository t) {
            PagedIterable<GHPullRequest> open = t.listPullRequests(GHIssueState.OPEN);
            PagedIterator<GHPullRequest> iter = open.iterator();
            while (iter.hasNext()) {
                GHPullRequest next = iter.next();
                System.out.println(t.getName() + "#" + next.getNumber() + " -- " + next.getTitle());
            }
        }

        @Override
        public String getName() {
            return "listOpenPullRequests";
        }

        @Override
        public String getDescription() {
            return "lists open pull requests";
        }

        @Override
        public String listArgs() {
            return "";
        }
    };

    public static Action ADD_TEAM_TO_REPO = new Action() {

        @Override
        public void accept(GHRepository r) {
            String[] args = getArgs();
            String organization = args[0];
            String team = args[1];
            if (team == null || organization == null) {
                throw new RuntimeException(this.getName() + " requires a non null arg");
            }
            try {
                GHOrganization org = GitHubBuilder.fromCredentials().build().getOrganization(organization);
                GHTeam githubTeam = org.getTeamByName(team);
                githubTeam.add(r);
                System.out.println("Added: " + githubTeam.getName() + " to " + r.getName());
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
        }

        @Override
        public String getName() {
            return "addTeam";
        }

        @Override
        public String getDescription() {
            return "Adds a team from an organization to a repository";
        }

        @Override
        public String listArgs() {
            return "(organization,team)";
        }
    };

    public static Action CLONE = new Action() {

        UsernamePasswordCredentialsProvider credentialsProvider = new PropertyFileCredentialsProvider();

        @Override
        public void accept(GHRepository r) {
            try {
                String[] args = getArgs();
                String directoryName = args[0];
                if (directoryName == null) {
                    throw new RuntimeException(this.getName() + " requires a non null arg");
                }

                File directory = new File(directoryName);
                if (!directory.exists()) {
                    directory.mkdirs();
                }
                directory = new File(directory, r.getName());
                for (int trys = 0; true; trys++) {
                    try {
                        Git.cloneRepository().setCredentialsProvider(credentialsProvider).setDirectory(directory)
                                .setURI(r.getUrl()).call();
                        break;
                    } catch (TransportException e) {
                        // sporadic failure
                        Utils.deleteFolder(directory);
                        if (trys > 3) {
                            throw e;
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String getName() {
            return "clone";
        }

        @Override
        public String getDescription() {
            return "clones the repo to a directory/name-of-repo";
        }

        @Override
        public String listArgs() {
            return "(directory)";
        }
    };
    
    public static Action DELETE = new Action() {
        
        @Override
        public void accept(GHRepository t) {
            try {
                t.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public String listArgs() {
            return "";
        }
        
        @Override
        public String getName() {
            return "delete";
        }
        
        @Override
        public String getDescription() {
            return "deletes the repo";
        }
    };

    public static Action LIST_OPEN_ISSUES = new Action() {

        @Override
        public void accept(GHRepository t) {
            PagedIterable<GHIssue> open = t.listIssues(GHIssueState.OPEN);
            PagedIterator<GHIssue> iter = open.iterator();
            while (iter.hasNext()) {
                GHIssue next = iter.next();
                System.out.println(t.getFullName() + "#" + next.getNumber() + " -- " + next.getTitle());
            }
        }

        @Override
        public String getName() {
            return "listOpenIssuess";
        }

        @Override
        public String getDescription() {
            return "lists open issues";
        }

        @Override
        public String listArgs() {
            return "";
        }
    };
}
