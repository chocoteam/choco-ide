package datas.compilation;

import org.junit.Test;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by yann on 06/06/15.
 */
public class CompileStrategyTest {

    @Test
    public void testKeepLine() throws Exception {
        assertThat(StringFilter.keepLine("", Arrays.asList()), is(true));
        assertThat(StringFilter.keepLine("Picked up JAVA_TOOL_OPTIONS: -Xmx384m -Xss512k -Dfile.encoding=UTF-8",Arrays.asList()), is(false));
    }
}