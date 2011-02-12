package com.mattinsler.guiceymongo.data;

import com.mattinsler.guiceymongo.FieldSet;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/29/10
 * Time: 2:18 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Query {
    <T, C extends Clause<T>> C and(Property<T, C> property);

    <T extends IsData> ExecutableQuery<T> select(Class<T> type);
    <T extends IsData> ExecutableQuery<T> select(Class<T> type, FieldSet set);
}
