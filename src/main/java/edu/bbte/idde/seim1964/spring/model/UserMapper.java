package edu.bbte.idde.seim1964.spring.model;

import edu.bbte.idde.seim1964.spring.model.dto.incoming.UserCreationDto;
import edu.bbte.idde.seim1964.spring.model.dto.outgoing.UserResponseDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import java.util.Collection;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    public abstract User creationDtoToModel(UserCreationDto dto);

    public abstract UserResponseDto modelToDto(User user);

    @IterableMapping(elementTargetType = UserResponseDto.class)
    public abstract Collection<UserResponseDto> modelsToDtos(Collection<User> users);
}