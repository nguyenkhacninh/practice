package io.tpi.currency.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@Entity
@Table(name = "currencies")
public class Currency {

    public Currency() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String currency;
    private String description;
    private Float exchangeRate;
    private String updatedDateStr;
}
