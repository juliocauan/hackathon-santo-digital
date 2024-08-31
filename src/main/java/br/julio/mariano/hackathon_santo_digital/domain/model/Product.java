package br.julio.mariano.hackathon_santo_digital.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity @Table(schema = "production", name = "product")
@Data @EqualsAndHashCode @Builder
@NoArgsConstructor @AllArgsConstructor
public class Product {
    
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid", nullable = false)
    private Integer id;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "productnumber", nullable = false)
    private String productNumber;

    @Column(name = "color", nullable = true)
    private String color;

    @Column(name = "safetystocklevel", nullable = false)
    private Short safetyStockLevel;

    @Column(name = "reorderpoint", nullable = false)
    private Short reorderPoint;

    @Column(name = "standardcost", nullable = false)
    private BigDecimal standardCost;

    @Column(name = "listprice", nullable = false)
    private BigDecimal listPrice;

    @Column(name = "daystomanufacture", nullable = false)
    private Integer daysToManufacture;

    @Column(name = "sellstartdate", nullable = false)
    private LocalDate sellStartDate;
    
}
