package com.mattinsler.guiceymongo.data.query;

import com.mattinsler.guiceymongo.data.Query;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/29/10
 * Time: 3:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class ValueClause<T> extends AbstractClause<T> {
    Query is(T value) {
        return null;
    }

    Query isNot(T value) {
        return null;
    }

    Query isGreaterThan(T value) {
        return null;
    }

    Query isGreaterThanEqual(T value) {
        return null;
    }

    Query isLessThan(T value) {
        return null;
    }

    Query isLessThanEqual(T value) {
        return null;
    }
}
