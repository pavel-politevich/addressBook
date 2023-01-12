package by.com.lifetech.addressBook.dto;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name="NCA_DICT_ADR_CITY_HISTORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictAdrCityHistory {

	private Long code;
	
	private String name;
	
	@Column(name="CITY_TYPE_CODE")
	private String cityTypeCode;
	
	@Column(name="DISTRICT_CODE")
	private String districtCode;
	
	@Column(name="MAP_CITY_CODE", length=50, nullable=false, unique=false)
	private String mapCityCode;
	
	@Column(name="CREATE_DATE")
	private Instant createDate;
	
	@Column(name="UPDATE_DATE")
	private Instant updateDate;
	
	private String active;
}
