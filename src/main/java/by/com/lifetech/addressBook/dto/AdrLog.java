package by.com.lifetech.addressBook.dto;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name="NCA_ADR_LOG")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdrLog {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private Instant date;
	
	private String table;
	
	private Long code;
	
	private String oldValue;
	
	private String newValue;

}
