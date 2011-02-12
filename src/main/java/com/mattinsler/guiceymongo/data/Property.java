package com.mattinsler.guiceymongo.data;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 11/28/10
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Property<Type, ClauseType extends Clause<Type>> {
    String getName();
    String getPath();
    Class<? extends Property> getParent();
}
