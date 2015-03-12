package org.kaazing.github;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;

public abstract class Filter extends SubCommand implements Predicate<GHRepository> {

    public static Map<String, Filter> getFiltersByName() {
        Map<String, Filter> filtersByName = new HashMap<>();
        filtersByName.put(ALL.getName(), ALL);
        filtersByName.put(PRIVATE.getName(), PRIVATE);
        filtersByName.put(PUBLIC.getName(), PUBLIC);
        filtersByName.put(HAS_FILE.getName(), HAS_FILE);
        filtersByName.put(BY_NAME.getName(), BY_NAME);
        return filtersByName;
    }

    public static final Filter ALL = new Filter() {
        @Override
        public boolean test(GHRepository t) {
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

    public static final Filter PRIVATE = new Filter() {
        @Override
        public boolean test(GHRepository t) {
            return t.isPrivate();
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

    public static final Filter PUBLIC = new Filter() {
        @Override
        public boolean test(GHRepository t) {
            return !t.isPrivate();
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

    public static final Filter HAS_FILE = new Filter() {

        @Override
        public boolean test(GHRepository t) {
            String[] args = getArgs();
            String path = args[0];
            String name = args[1];
            try {
                for (GHContent content : t.getDirectoryContent(path)) {
                    if (name.equalsIgnoreCase(content.getName())) {
                        return true;
                    }
                }
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        public String getName() {
            return "hasFile";
        }

        @Override
        public String getDescription() {
            return "filters repositories that has files";
        }

        @Override
        public String listArgs() {
            return "(path,file-name)";
        }

    };
    
    public static final Filter BY_NAME = new Filter() {
        
        @Override
        public boolean test(GHRepository t) {
            String[] args = getArgs();
            String name = args[0];
            if(name == null){
                throw new RuntimeException("Filter by name requires non null arg");
            }
            if(t.getName().equalsIgnoreCase(name)){
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
