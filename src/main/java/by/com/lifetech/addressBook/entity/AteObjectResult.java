package by.com.lifetech.addressBook.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonRootName(value = "result")
public class AteObjectResult {
	
	@JsonProperty("objectNumber")
	private int objectNumber;
	
	@JsonProperty("nameObject")
	private String nameObject;

}
