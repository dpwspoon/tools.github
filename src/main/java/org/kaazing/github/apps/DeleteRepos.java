package org.kaazing.github.apps;

import org.kaazing.github.Action;
import org.kaazing.github.Crawler;
import org.kaazing.github.Filter;

public class DeleteRepos {

    public static void main(String... args) throws Exception {
        String user = "put user or org here";
        Filter filter = Filter.BY_NAME;
        filter.setArgs(new String[]{"LIST", "REPOS", "here"});
        Crawler crawler = new Crawler(user, filter, Action.DELETE);
        crawler.run();
        System.out.println("Done!");
    }
}
