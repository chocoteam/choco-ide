package datas.compilation;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by yann on 08/06/15.
 */
public class StringFilter {
    static String filterOutLines(String messErr, List<String> patternsToRemove) {
        String[] split = messErr.split("\n");
        Collector<CharSequence, ?, String> joining = Collectors.joining("\n");
        return Arrays.stream(split).filter(l -> keepLine(l, patternsToRemove)).collect(joining);
    }

    public static boolean keepLine(String l, List<String> patternsToRemove) {
        // indique si aucun des patterns match la ligne
        return patternsToRemove.stream().noneMatch(p-> Pattern.compile(p).matcher(l).matches());
    }
}
