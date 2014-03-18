package com.iggdrasill.baldr.parser.utils;

/**
 *
 * @author Alex Kalnij
 * @date Mar 18, 2014
 */
public class IntegerCaster implements CastingStrategy<Integer> {

    public Integer cast(String toCast) {
        return Integer.parseInt(toCast.trim());
    }
}
