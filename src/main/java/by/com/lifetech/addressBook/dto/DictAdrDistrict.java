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
@Table(name="NCA_DICT_ADR_DISTRICT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictAdrDistrict {
	
	@Id
	private Long code;
	
	private String name;
	
	@Column(name="REGION_CODE")
	private String regionCode;
	
	@Column(name="MAP_DISTR_CODE", length=50, nullable=false, unique=false)
	private String mapDistrCode;
	
	@Column(name="CREATE_DATE")
	private Instant createDate;
	
	@Column(name="UPDATE_DATE")
	private Instant updateDate;
	
	private String active;

}
