package com.iggdrasill.baldr.parser.utils;

/**
 *
 * @author Alex Kalnij
 * @date Mar 18, 2014
 */
public class StringCaster implements CastingStrategy<String> {

    public String cast(String toCast) {
        return toCast;
    }
}
