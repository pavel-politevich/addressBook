package by.com.lifetech.addressBook.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import by.com.lifetech.addressBook.dto.DictAdrRegion;
import by.com.lifetech.addressBook.entity.RequestType;
import by.com.lifetech.addressBook.repository.DistrictsRepository;
import by.com.lifetech.addressBook.repository.RegionsRepository;
import by.com.lifetech.addressBook.repository.SelsovetsRepository;
import by.com.lifetech.addressBook.service.RequestService;

@RestController
public class DictAdrController {

	public DictAdrController(RegionsRepository regionsRepository, RequestService requestService,
			DistrictsRepository districtsRepository, SelsovetsRepository selsovetsRepository) {
		super();
		this.regionsRepository = regionsRepository;
		this.requestService = requestService;
		this.districtsRepository = districtsRepository;
		this.selsovetsRepository = selsovetsRepository;
	}

	Logger logger = LoggerFactory.getLogger(DictAdrController.class);

	private final RegionsRepository regionsRepository;
	private final RequestService requestService;
	private final DistrictsRepository districtsRepository;
	private final SelsovetsRepository selsovetsRepository;

	@GetMapping("/getallRegions")
	Iterable<DictAdrRegion> getAllRegions() {
		return regionsRepository.findAll();
	}

	// save all regions
	@PostMapping("/regions")
	String fillAllRegions() {
		requestService.saveRegions(requestService.getAteObjectRoot(RequestType.ATE_REGIONS, 0));
		return "success";
	}

	// save all districts by regions and Minsk
	@PostMapping("/districts")
	String fillAllDistrictsByRegions() {

		regionsRepository.findAll().forEach(name -> {
			if (!name.getName().equals("-")) {
				requestService.saveDistricts(requestService.getAteObjectRoot(RequestType.ATE_REGIONS_ATE_TE,
						Math.toIntExact(name.getCode())));
				logger.debug(name.getName());
			}
		});
		// Minsk
		requestService.saveDistricts(requestService.getAteObjectRoot(RequestType.ATE_REGIONS_ATE_TE, 17030));

		return "success";

	}

	// save all selsovets by districts
	@PostMapping("/selsovets")
	String fillAllSelsovetsByDistricts() {

		districtsRepository.findAll().forEach(name -> {
			if (!name.getName().equals("-")) {
				requestService.saveSelsovets(requestService.getAteObjectRoot(RequestType.ATE_DISTRICTS_ATE_TE,
						Math.toIntExact(name.getCode())));
				logger.debug(name.getName());
			}
		});

		return "success";

	}

	// save all cities by selsovets
	@PostMapping("/cities")
	String fillAllCitiesBySelsovets() {

		selsovetsRepository.findAll().forEach(name -> {
			if (!name.getName().equals("-")) {
				requestService.saveCities(requestService.getAteObjectRoot(RequestType.ATE_DISTRICTS_ATE_TE,
						Math.toIntExact(name.getCode())));
				logger.debug(name.getName());
			}
		});

		return "success";
	}

	// save all addresses by districts, regional cities, cities regional centers
	// and Minsk
	@PostMapping("/addresses")
	String fillAllAddressesByDistricts() {

		// by districts
		districtsRepository.findAll().forEach(name -> {
			if (!name.getName().equals("-")) {
				requestService.saveAddressObjects(requestService.getAddressObjectRoot(RequestType.ADDRESSES_BY_REGION,
						Math.toIntExact(name.getCode())));
				logger.debug("District name = " + name.getName());
			}
		});

		// regional cities, cities regional centers and Minsk
		districtsRepository.findUndefCityCodes().forEach(code -> {

			requestService.saveAddressObjects(
					requestService.getAddressObjectRoot(RequestType.ADDRESSES_BY_REGION, Math.toIntExact(code)));
			logger.debug("City code = " + code);

		});
		return "success";
	}

	// save districts by region
	@PostMapping("/districts/{id}")
	String fillDistrictsByRegion(@PathVariable("id") int id) {
		requestService.saveDistricts(requestService.getAteObjectRoot(RequestType.ATE_REGIONS_ATE_TE, id));
		return "success";
	}

	// save selsovets by district
	@PostMapping("/selsovets/{id}")
	String fillSelsovetsByDistrict(@PathVariable("id") int districtId) {
		requestService.saveSelsovets(requestService.getAteObjectRoot(RequestType.ATE_DISTRICTS_ATE_TE, districtId));
		return "success";
	}

	// save cities by selsovet
	@PostMapping("/cities/{id}")
	String fillCitiesBySelsovet(@PathVariable("id") int selsovetId) {
		requestService.saveCities(requestService.getAteObjectRoot(RequestType.ATE_DISTRICTS_ATE_TE, selsovetId));
		return "success";
	}

	// save addresses by district
	@PostMapping("/addresses/{id}")
	String fillAddressesByDistrict(@PathVariable("id") int districtId) {
		requestService
				.saveAddressObjects(requestService.getAddressObjectRoot(RequestType.ADDRESSES_BY_REGION, districtId));
		return "success";
	}

}
