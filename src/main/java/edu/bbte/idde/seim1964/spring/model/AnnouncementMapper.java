package edu.bbte.idde.seim1964.spring.model;

import edu.bbte.idde.seim1964.spring.model.dto.incoming.AnnouncementCreationDto;
import edu.bbte.idde.seim1964.spring.model.dto.outgoing.AnnouncementResponseDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class AnnouncementMapper {
    public abstract Announcement creationDtoToModel(AnnouncementCreationDto dto);

    public abstract AnnouncementResponseDto modelToDto(Announcement announcement);

    @IterableMapping(elementTargetType = AnnouncementResponseDto.class)
    public abstract Collection<AnnouncementResponseDto> modelsToDtos(Collection<Announcement> announcements);
}

