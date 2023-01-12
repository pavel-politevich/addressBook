package by.com.lifetech.addressBook.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import by.com.lifetech.addressBook.dto.DictAdrCityType;

public interface CityTypeRepository extends CrudRepository<DictAdrCityType, Long> {
	
	@Procedure("CONF.NCA_ADDR_API.add_nca_dict_adr_city_type")
	void callProcedure(@Param("p_code") String cityTypeName);

}
