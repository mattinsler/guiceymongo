package com.mattinsler.guiceymongo.data.property;

import com.mattinsler.guiceymongo.data.Property;
import com.mattinsler.guiceymongo.data.query.ValueClause;
import org.bson.types.ObjectId;

/**
 * @author Matt Insler
 *         Created on 2/11/11
 */
public class ObjectIdProperty extends AbstractProperty<ObjectId, ValueClause<ObjectId>> {
    public ObjectIdProperty(String name) {
        super(name);
    }

    protected ObjectIdProperty(String name, Class<? extends Property> parent) {
        super(name, parent);
    }
}
