package br.julio.mariano.hackathon_santo_digital.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Table(name = "roles", schema = "auth")
@Getter @EqualsAndHashCode @Builder
@NoArgsConstructor @AllArgsConstructor
public final class Role {
    
	@Id @EqualsAndHashCode.Exclude
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;
    
	@Column(length = 40, nullable = false, unique = true)
    private String name;

}
    