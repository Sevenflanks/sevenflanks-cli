package tw.org.sevenflanks.cli.renamer.handler;

import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;
import tw.org.sevenflanks.cli.CliApplication;
import tw.org.sevenflanks.cli.common.Handler;

import java.io.File;
import java.util.Optional;

@Component
public class Pather implements Handler {

    static final Options options;
    static final CommandLineParser parser = new DefaultParser();

    static {
        options = new Options();

        Option d = new Option("d", "directory", true, "資料路徑");
        d.setRequired(false);
        options.addOption(d);
    }

    @Override
    public String name() {
        return "cd";
    }

    @Override
    public boolean handle(String... args) throws Exception {
        final CommandLine cmd = parser.parse(options, args);

        final Optional<String> d = Optional.ofNullable(cmd.getOptionValue('d'));

        if (d.isPresent()) {
            final File file = new File(d.get());
            if (file.exists()) {
                CliApplication.cd = d.get();
                System.out.println("\tdirectory to " + CliApplication.cd);
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("\tcurrent directory is " + CliApplication.cd);
            return true;
        }
    }
}
