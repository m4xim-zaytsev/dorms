package com.example.task_service_jwt.mapper;

import com.example.task_service_jwt.entity.User;
import com.example.task_service_jwt.web.model.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse userToResponse(User user);


}
