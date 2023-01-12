package by.com.lifetech.addressBook.entity;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressObjectRoot {
	
	private ArrayList<AddressObjectResult> result;
	
	private Long addressProcessId;
	private int objectNumber;
}
