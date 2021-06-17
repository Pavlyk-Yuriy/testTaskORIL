package com.pavlyk.oril.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document(collection = "crypto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Cryptocurrency {
    @Id
    private String id;
    private String currencyName1;
    private String currencyName2;
    private Double price;
}
