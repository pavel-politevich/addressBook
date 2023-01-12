package by.com.lifetech.addressBook.dto;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="ADDRESS_PROCESS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressProcess {

	@Id
	@GeneratedValue(generator="InvSeq")
	@SequenceGenerator(name="InvSeq",sequenceName="ADDRESS_PROCESS_SEQ", allocationSize=1)
	private Long id;
	
	private String method;
	
	private String input;
	
	private Instant starttime;
	
	private String result;
	
	private Instant endtime;
	
	private String success;
}
