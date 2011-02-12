package com.mattinsler.guiceymongo.data;

import com.mattinsler.guiceymongo.GuiceyCursor;
import com.mattinsler.guiceymongo.Order;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/29/10
 * Time: 2:18 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ExecutableQuery<T extends IsData> {
    public interface Ordering<T extends IsData> {
        ExecutableQuery<T> ascending();

        ExecutableQuery<T> descending();
    }

    <P, C extends Clause<P>> Ordering<T> orderBy(Property<P, C> property);

    ExecutableQuery<T> sort(Order order);

    ExecutableQuery<T> skip();

    ExecutableQuery<T> limit();

    T executeOne();

    GuiceyCursor<T> execute();
}
