package by.com.lifetech.addressBook.dto;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
//@Table(name="NCA_DICT_ADR_ADDRESS_HISTORY")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictAdrAddressHistory {

	private Long code;
	
	@Column(name="OBJECT_TYPE")
	private String objectType;
	
	@Column(name="REGION")
	private String region;
	
	@Column(name="DISTRICT")
	private String district;
	
	@Column(name="SELSOVET")
	private String selsovet;
	
	@Column(name="CITYTYPE")
	private String cityType;
	
	@Column(name="CITYNAME")
	private String cityName;
	
	@Column(name="STREETTYPE")
	private String streetType;
	
	@Column(name="STREETNAME")
	private String streetName;
	
	@Column(name="HOUSE")
	private String house;
	
	@Column(name="CORP")
	private String corp;
	
	@Column(name="IND")
	private String ind;
	
	@Column(name="KM")
	private String km;
	
	@Column(name="POSTINDEX")
	private String postIndex;
	
	@Column(name="REMARK")
	private String remark;
	
	@Column(name="UPDATE_DATE")
	private Instant updateDate;
}
