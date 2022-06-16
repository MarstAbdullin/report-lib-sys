package ru.tkoinform.reportlib.converter;

import ru.tkoinform.reportlib.parser.Any2Any;

public interface Any2AnyFactory<SRC, DST> {

    Any2Any<SRC, DST> createParser();
}
