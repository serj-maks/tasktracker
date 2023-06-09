package org.example.tasktracker.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import org.example.tasktracker.dto.project.ProjectCreateDto;
import org.example.tasktracker.dto.project.ProjectResponseDto;
import org.example.tasktracker.Project;

public class ProjectMapperImplTest {

    ProjectMapper projectMapper = Mappers.getMapper(ProjectMapper.class);

    @Test
    void toProjectResponseDto() {
        Project project = new Project()
            .setId(1L)
            .setName("Sales")
            .setDescription("sales desc");

        ProjectResponseDto dto = projectMapper.toProjectResponseDto(project);

        assertEquals(project.getId(), dto.id());
        assertEquals(project.getName(), dto.name());
        assertEquals(project.getDescription(), dto.description());
    }

    @Test
    void toProjectsResponseDto() {
        Project project = new Project()
            .setId(1L)
            .setName("Sales")
            .setDescription("sales desc");
        List<ProjectResponseDto> actualDtos = projectMapper.toProjectsResponseDto(List.of(project));

        ProjectResponseDto expectedDto = new ProjectResponseDto(1L, "Sales", "sales desc");
        List<ProjectResponseDto> expectedDtos = List.of(expectedDto);

        assertEquals(expectedDtos, actualDtos);
    }

    @Test
    void toProject() {
        ProjectCreateDto dto = new ProjectCreateDto("Sales", "sales desc");

        Project project = projectMapper.toProject(dto);

        assertEquals(project.getName(), dto.name());
        assertEquals(project.getDescription(), dto.description());
    }
}
