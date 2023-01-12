package by.com.lifetech.addressBook.entity;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AteObjectRoot {
	
	@JsonProperty("resultCode")
	private int resultCode;
	
	@JsonProperty("result") 
	private ArrayList<AteObjectResult> result;
	
	private Long addressProcessId;
	private int objectNumber;

}
