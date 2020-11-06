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

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, componentModel = "spring")
public interface TimeslotMapper {

    default OffsetDateTime dateToOffsetDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atOffset(ZoneOffset.UTC);
    }

    default Date offsetDateTimeToDate(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return Date.from(offsetDateTime.toInstant());
    }

    default List<TimeslotDTO> timeslotsToDTOs(Set<Timeslot> timeslots) {
        return timeslots.stream()
            .map(this::timeslotToDTO)
            .sorted(Comparator.comparing(TimeslotDTO::getStart))
            .collect(Collectors.toList());
    }

    TimeslotDTO timeslotToDTO(Timeslot timeslot);

    List<TimeslotCreationData> timeslotCreationDTOsToData(List<TimeslotCreationDTO> timeslotCreationDTOs);

    TimeslotCreationData timeslotCreationDTOToData(TimeslotCreationDTO timeslotCreationDTO);
}
