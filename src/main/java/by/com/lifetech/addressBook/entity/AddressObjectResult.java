package by.com.lifetech.addressBook.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressObjectResult {

	private int adrNum;

	private String propTypeNameExt;

	private String region;

	private String district;

	private String village;

	private String categoryName;

	private String nameobject;

	private String elementTypeName;

	private String elementName;

	private String numHouse;

	private String numCorp;

	private String indHouse;

	private String km;

	private String index;

	private String remark;

}
