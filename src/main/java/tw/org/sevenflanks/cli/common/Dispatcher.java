package tw.org.sevenflanks.cli.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Dispatcher {

    private Pattern pattern = Pattern.compile("(?:\"[\\s\\S]*?\"|[-\\S]*)");
    private final Map<String, Handler> handlers;

    @Autowired
    public Dispatcher(List<Handler> handlers) {
        this.handlers = handlers.stream().collect(Collectors.toMap(Handler::name, Function.identity()));
    }

    public boolean go(String input) throws Exception {
        final Matcher matcher = pattern.matcher(input);

        String handlerName = null;
        List<String> args = new ArrayList<>();
        while (matcher.find()) {
            if (handlerName == null) {
                handlerName = matcher.group();
            } else if (!matcher.group().isEmpty()) {
                args.add(StringUtils.trimTrailingCharacter(StringUtils.trimLeadingCharacter(matcher.group(), '"'), '"'));
            }
        }

        return handlers.containsKey(handlerName) && handlers.get(handlerName).handle(args.stream().toArray(String[]::new));
    }

}
