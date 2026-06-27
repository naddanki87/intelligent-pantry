package com.colruytgroup.intelligentpantry.entity;

import com.colruytgroup.intelligentpantry.enums.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "PANTRY_ITEM",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "ITEM_NAME"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PantryItem  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private String unit;

    private LocalDate purchaseDate;

    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private Category category;
}
