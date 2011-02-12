package com.mattinsler.guiceymongo.data.query;

import org.bson.BSON;

/**
 * Created by IntelliJ IDEA.
 * User: mattinsler
 * Date: 12/29/10
 * Time: 3:28 AM
 * To change this template use File | Settings | File Templates.
 */
public enum BSONType {
    Double(BSON.NUMBER),
    String(BSON.STRING),
    Object(BSON.OBJECT),
    Array(BSON.ARRAY),
    BinaryData(BSON.BINARY),
    ObjectId(BSON.OID),
    Boolean(BSON.BOOLEAN),
    Date(BSON.DATE),
    Null(BSON.NULL),
    RegularExpression(BSON.REGEX),
    Code(BSON.CODE),
    Symbol(BSON.SYMBOL),
    CodeWithScope(BSON.CODE_W_SCOPE),
    Integer(BSON.NUMBER_INT),
    Timestamp(BSON.TIMESTAMP),
    Long(BSON.NUMBER_LONG),
    MinKey(BSON.MINKEY),
    MaxKey(BSON.MAXKEY);

    private final byte _typeCode;

    BSONType(byte typeCode) {
        _typeCode = typeCode;
    }

    byte getTypeCode() {
        return _typeCode;
    }
}
