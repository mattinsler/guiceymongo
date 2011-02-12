package com.mattinsler.guiceymongo.data;

import com.mattinsler.guiceymongo.data.query.BSONType;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/28/10
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Clause<T> {
    Query exists();
    Query doesNotExist();
    Query isOfType(BSONType type);
}
