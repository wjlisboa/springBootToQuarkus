package com.desbravador.desafioJava.mapper;

import org.mapstruct.*;

@org.mapstruct.MapperConfig(
        componentModel = MappingConstants.ComponentModel.JAKARTA_CDI,
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MapperConfig {
}
