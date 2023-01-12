package by.com.lifetech.addressBook.dto;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name="NCA_DICT_ADR_STREET_TYPE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictAdrStreetType {

	@Id
	private Long code;
	
	private String name;
	
	@Column(name="ABBR_CITY_TYPE")
	private String abbrCityType;
	
	@Column(name="MAP_STREET_TYPE", length=50, nullable=false, unique=false)
	private String mapStreetType;
	
	@Column(name="CREATE_DATE")
	private Instant createDate;
	
	@Column(name="UPDATE_DATE")
	private Instant updateDate;
	
	private String active;
}
