package com.iggdrasill.baldr.parser;

import java.io.InputStream;
import java.util.List;

/**
 *
 * @author Alex Kalnij
 * @date Mar 11, 2014
 */
public interface Parser {

    <T> List<T> parse(InputStream file, Class<T> type, InputStream config);
}
