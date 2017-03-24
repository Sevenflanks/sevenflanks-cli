package tw.org.sevenflanks.cli.renamer.handler;

import org.apache.commons.cli.*;
import org.springframework.stereotype.Component;
import tw.org.sevenflanks.cli.CliApplication;
import tw.org.sevenflanks.cli.common.Handler;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class Finder implements Handler {

    static final Options options;
    static final CommandLineParser parser = new DefaultParser();

    static {
        options = new Options();

        Option p = new Option("p", "pattern", true, "比對表達式");
        p.setRequired(true);
        options.addOption(p);

        Option r = new Option("r", "replacement", true, "取代");
        r.setRequired(false);
        options.addOption(r);

        Option f = new Option("f", "force", false, "確定進行取代");
        f.setRequired(false);
        options.addOption(f);

        Option d = new Option("d", "directory", true, "資料路徑");
        d.setRequired(false);
        options.addOption(d);
    }

    @Override
    public String name() {
        return "find";
    }

    @Override
    public boolean handle(String... args) throws Exception {
        final CommandLine cmd = parser.parse(options, args);

        final String d = Optional.ofNullable(cmd.getOptionValue('d')).orElse(CliApplication.cd);
        final Optional<String> replacement = Optional.ofNullable(cmd.getOptionValue('r'));
        final Pattern pattern = Pattern.compile(cmd.getOptionValue('p'));

        try (final Stream<Path> files = Files.walk(Paths.get(d), 1)) {
            files.forEach(path -> {
                final Matcher matcher = pattern.matcher(path.getFileName().toString());
                final File file = path.toFile();
                if (file.isFile() && matcher.matches()) {
                    if (replacement.isPresent()) {
                        final String newFileName = matcher.replaceFirst(replacement.get());
                        System.out.println("\t" + path.getFileName().toString() + " -> " + newFileName);
                        if (cmd.hasOption('f')) {
                            if (file.renameTo(new File(file.toPath().getParent().toString() + "/" + newFileName))) {
                                System.out.println("\trename success");
                            } else {
                                System.out.println("\trename failed");
                            }
                        }
                    } else {
                        System.out.println("\t" + path.getFileName().toString());
                    }
                }
            });
            if (cmd.hasOption('r') && !cmd.hasOption('f')) {
                System.out.println("\tthis is preview mode, if rename checked, please add arg -f");
            }
        }

        return true;
    }

}
