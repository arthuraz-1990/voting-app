package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

public class ElectionDto {

    @Schema(description = "Identificador da Eleição")
    private Long id;

    @Schema(description = "Nome da Eleição")
    @NotEmpty
    @Size(min = 3, max = 100)
    private String name;

    @Schema(description = "Data/Hora de Início da Eleição", format = "yyyy-MM-dd HH:mm:ss", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Schema(description = "Data/Hora do Final da Eleição", format = "yyyy-MM-dd HH:mm:ss", type = "string")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @AssertTrue(message = "Data fim deve ser maior ou igual a data de início")
    public boolean isValidDateRange() {
        if (this.getStartDate() == null || this.getEndDate() == null) {
            return true;
        }
        // Data fim deve ser maior ou igual a data de início...
        return this.getEndDate().compareTo(this.getStartDate()) >= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElectionDto that = (ElectionDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
