package com.mattinsler.guiceymongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 10/30/10
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Order {
    public static class Direction {
        private final Order _order;
        private final String _field;

        private Direction(Order order, String field) {
            _order = order;
            _field = field;
        }

        public Order ascending() {
            _order._set.put(_field, 1);
            return _order;
        }

        public Order descending() {
            _order._set.put(_field, -1);
            return _order;
        }
    }

    private final DBObject _set = new BasicDBObject();

    private Order() {
    }

    public Direction and(String field) {
        return new Direction(this, field);
    }

    public DBObject build() {
        return _set;
    }

    public static Direction by(String field) {
        return new Order().and(field);
    }
}
