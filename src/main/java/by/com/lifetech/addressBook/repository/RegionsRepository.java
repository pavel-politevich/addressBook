package by.com.lifetech.addressBook.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import by.com.lifetech.addressBook.dto.DictAdrRegion;

public interface RegionsRepository extends CrudRepository<DictAdrRegion, Long> {

	@Procedure("CONF.NCA_ADDR_API.add_nca_dict_adr_region")
	void callProcedure(@Param("p_code") int regionCode,
			@Param("p_name") String regionName);
}
