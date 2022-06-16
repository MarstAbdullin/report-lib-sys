package ru.tkoinform.reportlib.converter.byteas;

import ru.tkoinform.reportlib.ReportFormat;
import ru.tkoinform.reportlib.exception.ReportLibException;
import ru.tkoinform.reportlib.model.RLReportColumn;
import ru.tkoinform.reportlib.parser.Any2Any;
import ru.tkoinform.reportlib.converter.AbstractAny2AnyFactory;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class Blob2ByteAFactory extends AbstractAny2AnyFactory<Blob, byte[]> {

    public Blob2ByteAFactory(ReportFormat reportFormat, RLReportColumn column, Class<? extends Blob> sourceClass, Class<byte[]> destinationClass) {
        super(reportFormat, column, sourceClass, destinationClass);
    }

    @Override
    public Any2Any<Blob, byte[]> createParser() {
        return (value) -> {
            try {
                return value.getBinaryStream().readAllBytes();

            } catch (IOException | SQLException e) {
                throw new ReportLibException("Error on converting Blob to byte[]", e);
            }
        };
    }
}
