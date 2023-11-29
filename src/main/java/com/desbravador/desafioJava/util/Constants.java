package com.desbravador.desafioJava.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String PROJECT_NOT_FOUND = "Projeto '%s' não encontrado";
    public static final String PROJECT_NOT_FOUND_TO_UPDATE = "Projeto '%s' não encontrado para atualização";
    public static final String PROJECT_NOT_FOUND_TO_DELETE = "Projeto '%s' não encontrado para exclusão";
    public static final String PROJECT_CAN_NOT_DELETE = "Projeto '%s' não pode ser excluído status atual: '%s'";

    public static final String PERSON_NOT_FOUND = "Pessoa '%s' não encontrada";
    public static final String PERSON_NOT_FOUND_TO_UPDATE = "Pessoa '%s' não encontrada para atualização";
    public static final String PERSON_NOT_FOUND_TO_DELETE = "Pessoa '%s' não encontrada para exclusão";
    public static final String PERSON_NOT_MANAGER = "Pessoa do cpf: '%s' nome: '%s' não é gerente";
    public static final String PERSON_NOT_EMPLOYEE = "Pessoa do cpf: '%s' nome: '%s' não é funcionário";
    public static final String PERSON_ALREADY_EXISTING = "Pessoa de cpf: '%s' já cadastrada";

    public static final String MEMBER_ALREADY_EXISTING = "Pessoa '%s' já é funcionário do projeto '%s'";
    public static final String MEMBER_NOT_FOUND = "Pessoa e/ou projeto não encontrado";
}
