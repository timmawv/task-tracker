package avlyakulov.timur.taskTrackerApi.mapper;

import avlyakulov.timur.taskTrackerApi.dto.SignUpDto;
import avlyakulov.timur.taskTrackerApi.dto.UserDto;
import avlyakulov.timur.taskTrackerApi.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(SignUpDto signUpDto);
}
