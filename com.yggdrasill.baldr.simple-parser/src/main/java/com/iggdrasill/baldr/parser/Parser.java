package com.iggdrasill.baldr.parser;

import java.io.InputStream;

/**
 *
 * @author Alex Kalnij
 * @date Mar 11, 2014
 */
public interface Parser {

    <T> T parse(InputStream file, Class<T> type, InputStream config);
}
