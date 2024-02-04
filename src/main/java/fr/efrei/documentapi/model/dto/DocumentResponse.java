package fr.efrei.documentapi.model.dto;

import java.util.Date;

public record DocumentResponse(Long id, String name, String type, Date submitionDate ) { }
