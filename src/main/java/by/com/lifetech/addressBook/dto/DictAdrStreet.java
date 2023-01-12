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
@Table(name="NCA_DICT_ADR_STREET")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictAdrStreet {

	@Id
	private Long code;
	
	private String name;
	
	@Column(name="STREET_TYPE_CODE")
	private String streetTypeCode;
	
	@Column(name="CITY_CODE")
	private String cityCode;
	
	@Column(name="MAP_STREET")
	private String mapStreetCode;
	
	@Column(name="CREATE_DATE")
	private Instant createDate;
	
	@Column(name="UPDATE_DATE")
	private Instant updateDate;
	
	private String active;
}
