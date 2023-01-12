package by.com.lifetech.addressBook.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import by.com.lifetech.addressBook.dto.DictAdrStreet;

public interface StreetRepository extends CrudRepository<DictAdrStreet, Long> {
	
	@Transactional
	@Procedure("CONF.NCA_ADDR_API.add_nca_dict_adr_street")
	void callProcedure(@Param("p_name") String streetName,
			@Param("p_street_type_name") String streetTypeName,
			@Param("p_city_name") String cityName,
			@Param("p_district_name") String districtName);

}
