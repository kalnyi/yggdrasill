package com.iggdrasill.baldr.parser.impl;

import com.iggdrasill.baldr.parser.Parser;
import java.io.ByteArrayInputStream;
import java.util.List;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Alex Kalnij
 * @date Mar 11, 2014
 */
public class CsvParserTest {

    @Test
    public void testParse() {
        Parser parser = new CsvParser();

        String toParse = "Vasya; Papkin; 23";
        ByteArrayInputStream is = new ByteArrayInputStream(toParse.getBytes());
        String cfg = "<root><separator>;</separator>"
                + "<firstName>1</firstName>"
                + "<lastName>2</lastName>"
                + "<age>3</age>"
                + "</root>";
        List<Person> parsedList = parser.parse(is, Person.class, new ByteArrayInputStream(cfg.getBytes()));
        Assert.assertNotNull(parsedList);
        Assert.assertFalse(parsedList.isEmpty());
    }
}
