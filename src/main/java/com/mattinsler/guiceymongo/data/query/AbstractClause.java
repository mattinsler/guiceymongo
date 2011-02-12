package com.mattinsler.guiceymongo.data.query;

import com.mattinsler.guiceymongo.data.Clause;
import com.mattinsler.guiceymongo.data.Query;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/29/10
 * Time: 3:35 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractClause<T> implements Clause<T> {
    public Query doesNotExist() {
        return null;
    }

    public Query exists() {
        return null;
    }

    public Query isOfType(BSONType type) {
        return null;
    }
}
