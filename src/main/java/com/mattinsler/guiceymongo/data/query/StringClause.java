package com.mattinsler.guiceymongo.data.query;

import com.mattinsler.guiceymongo.data.Query;

import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/29/10
 * Time: 3:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringClause extends ValueClause<String> {
    Query isLike(String value) {
        return null;
    }

    Query isLike(Pattern value) {
        return null;
    }
}
