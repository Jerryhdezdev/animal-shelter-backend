package com.jerryhdez.animalshelter.web.dto;

public class AnimalResponseDTO {

    private Long id;
    private String name;
    private String species;
    private String birthDate;
    private String size;

    public AnimalResponseDTO() {}

    public AnimalResponseDTO(Long id,
                             String name,
                             String species,
                             String birthDate,
                             String size) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.birthDate = birthDate;
        this.size = size;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public String getBirthDate() { return birthDate; }
    public String getSize() { return size; }
}