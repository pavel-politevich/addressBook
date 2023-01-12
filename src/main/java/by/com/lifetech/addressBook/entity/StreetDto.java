package by.com.lifetech.addressBook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StreetDto {
	
	String name;
	String typeName;
	String cityName;
	String districtName;
	
	public StreetDto(final AddressObjectResult p) {
		 this(p.getElementName(), p.getElementTypeName(), p.getNameobject(), p.getDistrict());
	}

}
