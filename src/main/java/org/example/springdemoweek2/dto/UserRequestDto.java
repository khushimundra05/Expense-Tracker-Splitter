package org.example.springdemoweek2.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRequestDto {
    @NotNull(message = "ID cannot be null")
    private Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
}
