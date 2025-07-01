package com.example.backend.mapper;

import com.example.backend.dto.SkillDTO;
import com.example.backend.entity.Skill;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    
    SkillDTO toDTO(Skill skill);
    
    List<SkillDTO> toDTOList(List<Skill> skills);
    
    Skill toEntity(SkillDTO skillDTO);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(SkillDTO skillDTO, @MappingTarget Skill skill);
} 