package com.desbravador.desafioJava.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MappingInheritanceStrategy;

@org.mapstruct.MapperConfig(
        componentModel = "cdi",
        mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MapperConfig {
}
