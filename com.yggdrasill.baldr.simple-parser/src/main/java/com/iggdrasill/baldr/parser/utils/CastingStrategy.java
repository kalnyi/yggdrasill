package com.iggdrasill.baldr.parser.utils;

/**
 *
 * @author Alex Kalnij
 * @date Mar 18, 2014
 */
public interface CastingStrategy<T> {

    T cast(String toCast);
}
