package by.com.lifetech.addressBook.repository;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import by.com.lifetech.addressBook.dto.DictAdrAddress;


public interface AddressesRepository extends CrudRepository<DictAdrAddress, Long> {
	
	@Transactional
	@Procedure("CONF.NCA_ADDR_API.add_nca_dict_adr_address")
	void callProcedure(@Param("p_code") int addressCode,
			@Param("p_object_type") String addressObjType,
			@Param("p_region") String addressRegion,
			@Param("p_district") String addressDistrict,
			@Param("p_selsovet") String addressSelsovet,
			@Param("p_citytype") String addressCityType,
			@Param("p_cityname") String addressCityName,
			@Param("p_streettype") String addressStreetType,
			@Param("p_streetname") String addressStreetName,
			@Param("p_house") String addressHouse,
			@Param("p_corp") String addressCorp,
			@Param("p_ind") String addressInd,
			@Param("p_km") String addressKm,
			@Param("p_postindex") String addressPostIndex,
			@Param("p_remark") String addressRemark);

}
