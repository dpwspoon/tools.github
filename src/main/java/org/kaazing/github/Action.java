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

import static org.kaazing.github.FilterIssues.BY_MILESTONE;
import static org.kaazing.github.FilterIssues.CLOSED_WITHIN_DAYS;
import static org.kaazing.github.FilterIssues.HAS_ASSIGNEE;
import static org.kaazing.github.FilterIssues.HAS_LABEL;
import static org.kaazing.github.FilterIssues.UPDATED_WITHIN_DAYS;
import static org.kohsuke.github.GHIssueState.CLOSED;
import static org.kohsuke.github.GHIssueState.OPEN;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.kohsuke.github.GHUser;
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
        actionsByName.put(LIST_ISSUES_CLOSED_WITHIN_DAY.getName(), LIST_ISSUES_CLOSED_WITHIN_DAY);
        actionsByName.put(LIST_ISSUES_UPDATED_WITHIN_DAY.getName(), LIST_ISSUES_UPDATED_WITHIN_DAY);
        actionsByName.put(LIST_OPEN_PULL_REQUESTS.getName(), LIST_OPEN_PULL_REQUESTS);
        actionsByName.put(LIST_BUGS_NOT_ASSIGNED.getName(), LIST_BUGS_NOT_ASSIGNED);
        actionsByName.put(LIST_ISSUES_BY_MILESTONE.getName(), LIST_ISSUES_BY_MILESTONE);
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
                GHUser user = next.getAssignee();
                if (user != null)
                    System.out
                            .println(t.getName() + "#" + next.getNumber() + " -- " + next.getTitle() + " -- " + user.getLogin());
                else
                    System.out.println(
                            t.getName() + "#" + next.getNumber() + " -- " + next.getTitle() + " -- " + "NO ASSIGNED USER");
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
                                .setURI(r.getUrl().toString()).call();
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

    public static Action LIST_ISSUES_CLOSED_WITHIN_DAY = new Action() {

        @Override
        public void accept(GHRepository t) {
            List<GHIssue> closed = null;
            try {
                closed = t.getIssues(CLOSED);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Iterator<GHIssue> iter = closed.iterator();
            while (iter.hasNext()) {
                GHIssue next = iter.next();
                FilterIssues filter = CLOSED_WITHIN_DAYS;
                String[] args = {"1"};
                filter.setArgs(args);
                if (filter.test(next))
                    System.out.println(t.getFullName() + "#" + next.getNumber() + " -- " + next.getTitle());
            }
        }

        @Override
        public String getName() {
            return "list-issues-recently-closed";
        }

        @Override
        public String getDescription() {
            return "get all recently closed issues";
        }

        @Override
        public String listArgs() {
            return "";
        }

    };

    public static Action LIST_ISSUES_UPDATED_WITHIN_DAY = new Action() {

        @Override
        public void accept(GHRepository t) {
            List<GHIssue> closed = null;
            try {
                closed = t.getIssues(OPEN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Iterator<GHIssue> iter = closed.iterator();
            while (iter.hasNext()) {
                GHIssue next = iter.next();
                FilterIssues filter = UPDATED_WITHIN_DAYS;
                String[] args = {"1"};
                filter.setArgs(args);
                if (filter.test(next))
                    System.out.println(t.getFullName() + "#" + next.getNumber() + " -- " + next.getTitle());
            }
        }

        @Override
        public String getName() {
            // TODO Auto-generated method stub
            return "list-issues-recently-updated";
        }

        @Override
        public String getDescription() {
            // TODO Auto-generated method stub
            return "get all recently updated issues";
        }

        @Override
        public String listArgs() {
            return "";
        }

    };

    public static Action LIST_BUGS_NOT_ASSIGNED = new Action() {

        @Override
        public void accept(GHRepository t) {
            List<GHIssue> closed = null;
            try {
                closed = t.getIssues(OPEN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Iterator<GHIssue> iter = closed.iterator();
            while (iter.hasNext()) {
                GHIssue next = iter.next();
                FilterIssues filters = HAS_LABEL;
                String[] args = {"bug"};
                filters.setArgs(args);
                FilterIssues filter = HAS_ASSIGNEE;
                filters.and(filter);
                if (filters.test(next))
                    System.out.println(t.getFullName() + "#" + next.getNumber() + " -- " + next.getTitle());
            }
        }

        @Override
        public String getName() {
            return "list-bugs-not-assigned";
        }

        @Override
        public String getDescription() {
            return "get all bugs not assigned";
        }

        @Override
        public String listArgs() {
            return "";
        }

    };

    public static Action LIST_ISSUES_BY_MILESTONE = new Action() {

        @Override
        public void accept(GHRepository t) {

            List<GHIssue> closed = null;
            try {
                closed = t.getIssues(OPEN);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Iterator<GHIssue> iter = closed.iterator();
            while (iter.hasNext()) {
                GHIssue next = iter.next();
                FilterIssues filter = BY_MILESTONE;
                filter.setArgs(getArgs());
                if (filter.test(next))
                    System.out.println(t.getFullName() + "#" + next.getNumber() + " -- " + next.getTitle());
            }
        }

        @Override
        public String getName() {
            // TODO Auto-generated method stub
            return "list-issues-by-milestone";
        }

        @Override
        public String getDescription() {
            // TODO Auto-generated method stub
            return "get all recently closed issues";
        }

        @Override
        public String listArgs() {
            // TODO Auto-generated method stub
            return "(milestone)";
        }

    };

}
