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

import java.io.IOException;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;

public class Crawler {

    private final static String CMD_OPTION_FILTER = "filter";
    private final static String CMD_OPTION_ACTION = "action";
    private final static String CMD_OPTION_USER = "user";
    private final static String CMD_OPTION_LIST = "help";
    private final String user;
    private final Predicate<GHRepository> filters;
    private final Action actions;
    public static final Pattern ARGS_PATTERN = Pattern.compile("(?<name>.*)\\((?<args>.*)\\)");

    public Crawler(String user, Predicate<GHRepository> filters, Action actions) {
        this.user = user;
        this.filters = filters;
        this.actions = actions;
    }

    public static void main(String... args) throws Exception {
        Options options =
                new Options().addOption("f", CMD_OPTION_FILTER, true, "filter to apply in search")
                        .addOption("a", CMD_OPTION_ACTION, true, "action to do when found")
                        .addOption("u", CMD_OPTION_USER, true, "user/organization to apply filter to")
                        .addOption("list", CMD_OPTION_LIST, true, "list (actions) or (options)");

        try {
            Parser parser = new PosixParser();
            CommandLine cmd = parser.parse(options, args);

            Map<String, Filter> filtersByName = Filter.getFiltersByName();
            Map<String, Action> actionsByName = Action.getActionsByName();
            // check if list command
            String list = cmd.getOptionValue(CMD_OPTION_LIST, null);
            if (list != null) {
                if (list.equalsIgnoreCase("actions")) {
                    for (Action action : actionsByName.values()) {
                        System.out.println(action.getName() + action.listArgs() + "\t\t" + action.getDescription());
                    }
                } else if (list.equalsIgnoreCase("filters")) {
                    for (Filter filter : filtersByName.values()) {
                        System.out.println(filter.getName() + filter.listArgs() + "\t\t" + filter.getDescription());
                    }
                } else {
                    throw new IllegalArgumentException("list command accepts \"filters\" or \"actions\"");
                }
            } else {
                // get filters
                String nameOfFilters = cmd.getOptionValue(CMD_OPTION_FILTER, "");
                Predicate<GHRepository> filters = null;
                for (String nameOfFilter : nameOfFilters.split(";")) {
                    Matcher matcher = ARGS_PATTERN.matcher(nameOfFilter);
                    boolean matches = matcher.matches();
                    if(matches){
                        nameOfFilter = matcher.group(1);
                    }
                    Filter filter = filtersByName.get(nameOfFilter.trim());
                    if (filter == null) {
                        throw new IllegalArgumentException("could not find filter: " + nameOfFilter);
                    }
                    if(matches){
                        filter.setArgs(matcher.group(2).split(","));
                    }
                    if (filters == null) {
                        filters = filter;
                    } else {
                        filters.and(filter);
                    }
                }

                // get actions
                String nameOfActions = cmd.getOptionValue(CMD_OPTION_ACTION, "");
                Action actions = null;
                for (String nameOfAction : nameOfActions.split(";")) {
                    Matcher matcher = ARGS_PATTERN.matcher(nameOfAction);
                    boolean matches = matcher.matches();
                    if(matches){
                        nameOfAction = matcher.group(1);
                    }
                    Action action = actionsByName.get(nameOfAction.trim());
                    if (action == null) {
                        throw new IllegalArgumentException("could not find action: " + action);
                    }
                    if(matches){
                        action.setArgs(matcher.group(2).split(","));
                    }
                    if (actions == null) {
                        actions = action;
                    } else {
                        actions.andThen(action);
                    }
                }

                // get user
                String user = cmd.getOptionValue(CMD_OPTION_USER, null);
                if (user == null) {
                    throw new IllegalArgumentException("Cannot accept a null user");
                }

                // see if help command
                cmd.getOptionValue(CMD_OPTION_ACTION, null);

                // init crawler
                Crawler crawler = new Crawler(user, filters, actions);

                // crawl user's repositories
                crawler.run();
            }
        } catch (ParseException ex) {
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp(Crawler.class.getSimpleName(), options, true);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            HelpFormatter helpFormatter = new HelpFormatter();
            helpFormatter.printHelp(Crawler.class.getSimpleName(), options, true);
        }
    }

    public void run() throws IOException {
        String myUserName = GitHubBuilder.fromCredentials().build().getMyself().getLogin();
        PagedIterable<GHRepository> repositories;
        if (myUserName.equalsIgnoreCase(user)) {
            repositories = GitHubBuilder.fromCredentials().build().getMyself().listRepositories();
        } else {
            repositories = GitHubBuilder.fromCredentials().build().getOrganization(user).listRepositories();
        }

        // apply filters and actions
        PagedIterator<GHRepository> iter = repositories.iterator();
        while (iter.hasNext()) {
            GHRepository repo = iter.next();
            if (filters.test(repo)) {
                actions.accept(repo);
            }
        }
    }
}
