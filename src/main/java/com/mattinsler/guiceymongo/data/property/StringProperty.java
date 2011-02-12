package com.mattinsler.guiceymongo.data.property;

import com.mattinsler.guiceymongo.data.Property;
import com.mattinsler.guiceymongo.data.property.AbstractProperty;
import com.mattinsler.guiceymongo.data.query.StringClause;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/29/10
 * Time: 2:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringProperty extends AbstractProperty<String, StringClause> {
    public StringProperty(String name) {
        super(name);
    }

    protected StringProperty(String name, Class<? extends Property> parent) {
        super(name, parent);
    }
}
