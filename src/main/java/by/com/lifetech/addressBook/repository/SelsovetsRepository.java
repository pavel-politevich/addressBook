package by.com.lifetech.addressBook.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import by.com.lifetech.addressBook.dto.DictAdrSelsovet;

public interface SelsovetsRepository extends CrudRepository<DictAdrSelsovet, Long> {

	@Procedure("CONF.NCA_ADDR_API.add_nca_dict_adr_selsovet")
	void callProcedure(@Param("p_code") int selsovetCode,
			@Param("p_name") String selsovetName,
			@Param("p_district_code") int districtCode);
}
