package ru.tkoinform.reportlib.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import ru.telecor.common.domain.IdentifiedEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TEST_ENTITY")
public class TestEntity implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "test_integer")
    private Integer integer;

    @Column(name = "test_float")
    private Float afloat;

    @Column(name = "test_double")
    private Double adouble;

    @Column(name = "test_bigdecimal")
    private BigDecimal bigdecimal;

    @Column(name = "test_date")
    private Date date;

    @Column(name = "test_time")
    private Date time;

    @Column(name = "test_datetime")
    private Date datetime;

    @Column(name = "test_datesql")
    private java.sql.Date datesql;

    @Column(name = "test_string")
    private String string;

    @Column(name = "test_boolean")
    private Boolean bool;

    @Column(name = "test_image", columnDefinition = "binary")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;

    @Column(name = "test_blob")
    @Lob
    private byte[] blob;
}

