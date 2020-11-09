package de.nordakademie.iaa.noodle.mapper;

import de.nordakademie.iaa.noodle.api.model.TimeslotCreationDTO;
import de.nordakademie.iaa.noodle.api.model.TimeslotDTO;
import de.nordakademie.iaa.noodle.model.Timeslot;
import de.nordakademie.iaa.noodle.services.model.TimeslotCreationData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for Timeslots.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface TimeslotMapper {

    /**
     * Maps a date to an OffsetDateTime
     * @param date The date to map.
     * @return The mapped OffsetDateTime.
     */
    default OffsetDateTime dateToOffsetDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }

    /**
     * Maps an OffsetDateTime to a Date
     * @param offsetDateTime The date to map.
     * @return The mapped Date.
     */
    default Date offsetDateTimeToDate(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return Date.from(offsetDateTime.toInstant());
    }

    /**
     * Maps a set of timeslots to timeslot DTOs.
     * @param timeslots The timeslots to map.
     * @return The mapped timeslots.
     */
    default List<TimeslotDTO> timeslotsToDTOs(Set<Timeslot> timeslots) {
        return timeslots.stream()
            .map(this::timeslotToDTO)
            .sorted(Comparator.comparing(TimeslotDTO::getStart))
            .collect(Collectors.toList());
    }

    /**
     * Maps a timeslot to timeslot DTO.
     * @param timeslot The timeslot to map.
     * @return The mapped timeslot.
     */
    TimeslotDTO timeslotToDTO(Timeslot timeslot);

    /**
     * Maps a list of TimeslotCreationDTO to TimeslotCreationData.
     * @param timeslotCreationDTOs The TimeslotCreationDTOs to map.
     * @return THe mapped TimeslotCreationData.
     */
    List<TimeslotCreationData> timeslotCreationDTOsToData(List<TimeslotCreationDTO> timeslotCreationDTOs);
}
