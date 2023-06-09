package org.example.tasktracker.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import org.example.tasktracker.dto.project.ProjectCreateDto;
import org.example.tasktracker.dto.project.ProjectResponseDto;
import org.example.tasktracker.Project;

@Mapper
public interface ProjectMapper {

    ProjectResponseDto toProjectResponseDto(Project project);

    List<ProjectResponseDto> toProjectsResponseDto(List<Project> projects);

    Project toProject(ProjectCreateDto dto);
}
