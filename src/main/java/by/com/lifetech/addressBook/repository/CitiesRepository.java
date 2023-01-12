package by.com.lifetech.addressBook.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import by.com.lifetech.addressBook.dto.DictAdrCity;

public interface CitiesRepository extends CrudRepository<DictAdrCity, Long> {
	
	@Procedure("CONF.NCA_ADDR_API.add_nca_dict_adr_city")
	void callProcedure(@Param("p_code") int cityCode,
			@Param("p_name") String cityName,
			@Param("p_selsovet_code") int selsovetCode);

}
