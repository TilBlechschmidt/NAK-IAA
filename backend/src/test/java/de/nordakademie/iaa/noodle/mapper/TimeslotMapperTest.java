package de.nordakademie.iaa.noodle.mapper;

import de.nordakademie.iaa.noodle.api.model.TimeslotDTO;
import de.nordakademie.iaa.noodle.model.Timeslot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.HashSet;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link TimeslotMapper}
 *
 * @author Noah Peeters
 */
class TimeslotMapperTest {
    private TimeslotMapper timeslotMapper;

    @BeforeEach
    public void setUp() {
        timeslotMapper = Mappers.getMapper(TimeslotMapper.class);
    }

    @Test
    void dateToOffsetDateTimeNull() {
        assertNull(timeslotMapper.dateToOffsetDateTime(null));
    }

    @Test
    void dateToOffsetDateTime() {
        Date date = new Date(1580605322000L);
        OffsetDateTime offsetDateTime = timeslotMapper.dateToOffsetDateTime(date);

        assertEquals(UTC, offsetDateTime.getOffset());
        assertEquals(2020, offsetDateTime.getYear());
        assertEquals(2, offsetDateTime.getMonth().getValue());
        assertEquals(2, offsetDateTime.getDayOfMonth());
        assertEquals(1, offsetDateTime.getHour());
        assertEquals(2, offsetDateTime.getMinute());
        assertEquals(2, offsetDateTime.getSecond());
    }

    @Test
    void offsetDateTimeToDateNull() {
        assertNull(timeslotMapper.offsetDateTimeToDate(null));
    }

    @Test
    void offsetDateTimeToDate() {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse("2020-02-02T02:02:02+01:00");
        Date date = timeslotMapper.offsetDateTimeToDate(offsetDateTime);

        assertEquals(1580605322000L, date.getTime());
    }

    @Test
    void offsetDateTimeToDateTimezones() {
        OffsetDateTime offsetDateTime1 = OffsetDateTime.parse("2020-02-02T02:02:02+01:00");
        OffsetDateTime offsetDateTime2 = OffsetDateTime.parse("2020-02-02T03:02:02+02:00");
        Date date1 = timeslotMapper.offsetDateTimeToDate(offsetDateTime1);
        Date date2 = timeslotMapper.offsetDateTimeToDate(offsetDateTime2);

        assertEquals(date1, date2);
    }

    @Test
    void testTimeslotToDTO() {
        Timeslot timeslot = mock(Timeslot.class);

        Date startDate = new Date(0);
        Date endDate = new Date(1);

        when(timeslot.getId()).thenReturn(42L);
        when(timeslot.getStart()).thenReturn(startDate);
        when(timeslot.getEnd()).thenReturn(endDate);

        TimeslotDTO timeslotDTO = timeslotMapper.timeslotToDTO(timeslot);

        assertEquals(42L, timeslotDTO.getId());
        assertEquals(startDate.toInstant().atOffset(UTC), timeslotDTO.getStart());
        assertEquals(endDate.toInstant().atOffset(UTC), timeslotDTO.getEnd());
    }

    @Test
    void testTimeslotsToDTOs() {
        Timeslot timeslot1 = mock(Timeslot.class);
        Timeslot timeslot2 = mock(Timeslot.class);

        Date startDate1 = new Date(0);
        Date startDate2 = new Date(1);

        when(timeslot1.getId()).thenReturn(42L);
        when(timeslot2.getId()).thenReturn(43L);
        when(timeslot1.getStart()).thenReturn(startDate1);
        when(timeslot2.getStart()).thenReturn(startDate2);

        List<TimeslotDTO> timeslotDTOs = timeslotMapper.timeslotsToDTOs(new HashSet<>(Arrays.asList(timeslot1,
            timeslot2)));

        assertEquals(2, timeslotDTOs.size());
        assertEquals(42L, timeslotDTOs.get(0).getId());
        assertEquals(43L, timeslotDTOs.get(1).getId());
    }
}
