package ru.tkoinform.reportlib.parser;

public interface Any2Any<SRC, DST> {
    DST format(SRC value);
}
