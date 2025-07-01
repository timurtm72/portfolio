package com.example.backend.mapper;

import com.example.backend.dto.ProjectDTO;
import com.example.backend.entity.Project;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {

    @Mapping(target = "categoryDisplayName", expression = "java(project.getCategory() != null ? project.getCategory().getDisplayName() : null)")
    @Mapping(target = "statusDisplayName", expression = "java(project.getStatus() != null ? project.getStatus().getDisplayName() : null)")
    ProjectDTO toDTO(Project project);

    List<ProjectDTO> toDTOList(List<Project> projects);

    Project toEntity(ProjectDTO projectDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ProjectDTO projectDTO, @MappingTarget Project project);
}