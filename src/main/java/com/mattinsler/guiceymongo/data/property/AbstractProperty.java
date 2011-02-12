package com.mattinsler.guiceymongo.data.property;

import com.mattinsler.guiceymongo.data.Clause;
import com.mattinsler.guiceymongo.data.Property;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/29/10
 * Time: 4:03 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractProperty<T, C extends Clause<T>> implements Property<T, C> {
    private final String _name;
    private final String _path;
    private final Class<? extends Property> _parent;

    protected AbstractProperty(String name) {
        this(name, null);
    }

    protected AbstractProperty(String name, Class<? extends Property> parent) {
        _name = name;
        _parent = parent;
        String path = name;
        if (_parent != null) {
            try {
                path = _parent.newInstance().getPath() + "." + name;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        _path = path;
    }

    public String getName() {
        return _name;
    }

    public String getPath() {
        return _path;
    }

    public Class<? extends Property> getParent() {
        return _parent;
    }
}
