package by.com.lifetech.addressBook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import by.com.lifetech.addressBook.dto.DictAdrDistrict;

public interface DistrictsRepository extends CrudRepository<DictAdrDistrict, Long> {
	
	@Procedure("CONF.NCA_ADDR_API.add_nca_dict_adr_district")
	void callProcedure(@Param("p_code") int districtCode,
			@Param("p_name") String districtName,
			@Param("p_region_code") int regionCode);
	
	@Query(value = "select t.code from CONF.NCA_DICT_ADR_CITY t where t.district_code in\r\n"
			+ "(\r\n"
			+ "  select d.code from CONF.NCA_DICT_ADR_DISTRICT d\r\n"
			+ "  where d.map_distr_code like 'UNDEF%'\r\n"
			+ ")", nativeQuery = true)
	List<Integer> findUndefCityCodes();
}
