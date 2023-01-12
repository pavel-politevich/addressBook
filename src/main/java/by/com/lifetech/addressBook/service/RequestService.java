package by.com.lifetech.addressBook.service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import by.com.lifetech.addressBook.dto.AddressProcess;
import by.com.lifetech.addressBook.entity.AddressObjectResult;
import by.com.lifetech.addressBook.entity.AddressObjectRoot;
import by.com.lifetech.addressBook.entity.AteObjectResult;
import by.com.lifetech.addressBook.entity.AteObjectRoot;
import by.com.lifetech.addressBook.entity.RequestContext;
import by.com.lifetech.addressBook.entity.RequestType;
import by.com.lifetech.addressBook.entity.StreetDto;
import by.com.lifetech.addressBook.repository.AddressProcessRepository;
import by.com.lifetech.addressBook.repository.AddressesRepository;
import by.com.lifetech.addressBook.repository.CitiesRepository;
import by.com.lifetech.addressBook.repository.CityTypeRepository;
import by.com.lifetech.addressBook.repository.DistrictsRepository;
import by.com.lifetech.addressBook.repository.RegionsRepository;
import by.com.lifetech.addressBook.repository.SelsovetsRepository;
import by.com.lifetech.addressBook.repository.StreetRepository;
import by.com.lifetech.addressBook.repository.StreetTypeRepository;

@Service
public class RequestService {

	private final RegionsRepository regionsRepository;
	private final AddressProcessRepository addressProcessRepository;
	private final DistrictsRepository districtsRepository;
	private final SelsovetsRepository selsovetsRepository;
	private final CitiesRepository citiesRepository;
	private final StreetTypeRepository streetTypeRepository;
	private final CityTypeRepository cityTypeRepository;
	private final StreetRepository streetRepository;
	private final AddressesRepository addressesRepository;
	private final RequestContext requestContext;

	public RequestService(RegionsRepository districtRepository, AddressProcessRepository addressProcessRepository,
			RequestContext requestContext, DistrictsRepository districtsRepository, CitiesRepository citiesRepository,
			SelsovetsRepository selsovetsRepository, StreetTypeRepository streetTypeRepository,
			CityTypeRepository cityTypeRepository, StreetRepository streetRepository,
			AddressesRepository addressesRepository) {
		super();
		this.regionsRepository = districtRepository;
		this.addressProcessRepository = addressProcessRepository;
		this.requestContext = requestContext;
		this.districtsRepository = districtsRepository;
		this.selsovetsRepository = selsovetsRepository;
		this.citiesRepository = citiesRepository;
		this.streetTypeRepository = streetTypeRepository;
		this.cityTypeRepository = cityTypeRepository;
		this.streetRepository = streetRepository;
		this.addressesRepository = addressesRepository;
	}

	Logger logger = LoggerFactory.getLogger(RequestService.class);

	RestTemplate restTemplate = new RestTemplate();
	HttpHeaders headers = new HttpHeaders();
	ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	ObjectMapper objectMapper = new ObjectMapper();

	@Value("${nca.userid}")
	private String userid;
	@Value("${nca.url.regions}")
	private String urlRegions;
	@Value("${nca.url.regions.atete}")
	private String urlRegionsAteTe;
	@Value("${nca.url.districts.atete}")
	private String urlDistrictsAteTe;
	@Value("${nca.url.buildings}")
	private String urlBuildings;
	@Value("${app.folder.json}")
	private String tempfolder;
	@Value("${app.filename.regions}")
	private String regionsFileName;

	@Value("${app.filename.regions.atete}")
	private String regionsAteFileName;
	@Value("${app.filename.districts.atete}")
	private String districtsFileName;
	@Value("${app.filename.buildings}")
	private String buildings;

	public AteObjectRoot getAteObjectRoot(RequestType requestType, int objectNumber) {

		requestContext.setRequestType(requestType);

		switch (requestType) {
		case ATE_REGIONS:
			requestContext.setUrl(urlRegions);
			requestContext.setFileName(regionsFileName + objectNumber + ".json");
			break;
		case ATE_REGIONS_ATE_TE:
			requestContext.setUrl(urlRegionsAteTe);
			requestContext.setFileName(regionsAteFileName + objectNumber + ".json");
			break;
		case ATE_DISTRICTS_ATE_TE:
			requestContext.setUrl(urlDistrictsAteTe);
			requestContext.setFileName(districtsFileName + objectNumber + ".json");
			break;
		case ADDRESSES_BY_REGION:
			requestContext.setUrl(urlBuildings);
			requestContext.setFileName(buildings + objectNumber + ".json");
			break;
		}

		File myFile = new File(tempfolder + requestContext.getFileName());
		AteObjectRoot ateObjectRoot = null;
		if (myFile.exists()) {
			try {
				logger.info("Find file " + myFile.getAbsolutePath() + " with existing object");
				ateObjectRoot = objectMapper.readValue(myFile, AteObjectRoot.class);
				ateObjectRoot.setAddressProcessId(0L);
				ateObjectRoot.setObjectNumber(Integer.parseInt(requestContext.getFileName().substring(
						requestContext.getFileName().indexOf("_") + 1, requestContext.getFileName().indexOf("."))));
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Can not parse json file");
			}
		} else {
			logger.info("File " + myFile.getAbsolutePath() + " not exist");
			requestContext.getRequestBodyData().getUser().setUserId(userid);
			requestContext.getRequestBodyData().setObjectNumber(objectNumber);
			ateObjectRoot = processAteRequest(requestContext);
		}
		logger.debug("AteObjectRoot.getResultCode= " + ateObjectRoot.getResultCode());
		return ateObjectRoot;
	}

	public AddressObjectRoot getAddressObjectRoot(RequestType requestType, int objectNumber) {

		requestContext.setRequestType(requestType);
		requestContext.setUrl(urlBuildings);
		requestContext.setFileName(buildings + objectNumber + ".json");

		File myFile = new File(tempfolder + requestContext.getFileName());
		AddressObjectRoot addressObjectRoot = null;
		if (myFile.exists()) {
			try {
				logger.info("Find file " + myFile.getAbsolutePath() + " with existing object");
				addressObjectRoot = new AddressObjectRoot(
						objectMapper.readValue(myFile, new TypeReference<ArrayList<AddressObjectResult>>() {
						}), 0L,
						Integer.parseInt(
								requestContext.getFileName().substring(requestContext.getFileName().indexOf("_") + 1,
										requestContext.getFileName().indexOf("."))));
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Can not parse json file");
			}
		} else {
			logger.info("File " + myFile.getAbsolutePath() + " not exist");
			requestContext.getRequestBodyData().getUser().setUserId(userid);
			requestContext.getRequestBodyData().setObjectNumber(objectNumber);
			addressObjectRoot = processBuildingsRequest(requestContext);
		}
		return addressObjectRoot;
	}

	public AteObjectRoot processAteRequest(RequestContext requestContext) {

		String userJsonObject = "";
		try {
			if (requestContext.getRequestType() == RequestType.ATE_REGIONS) {
				userJsonObject = ow.writeValueAsString(requestContext.getRequestBodyDataWithoutParam());
			} else {
				userJsonObject = ow.writeValueAsString(requestContext.getRequestBodyData());
			}
		} catch (JsonProcessingException e) {
			logger.error("Can not create User object for request");
		}

		logger.info("Call request with context: " + requestContext);
		logger.info(
				"Call processAteRequest with  URL= " + requestContext.getUrl() + "; RequestBody = " + userJsonObject);

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(userJsonObject.toString(), headers);

		ResponseEntity<AteObjectRoot> responseEntityObj = restTemplate.postForEntity(requestContext.getUrl(), request,
				AteObjectRoot.class);
		AteObjectRoot ateObjectRoot = responseEntityObj.getBody();

		try {
			objectMapper.writeValue(new File(tempfolder + requestContext.getFileName()), ateObjectRoot);
			logger.info("File created: " + tempfolder + requestContext.getFileName());
		} catch (IOException e) {
			logger.error("Can not write object to json file: " + e.getMessage());
		}

		AddressProcess addressProcess = new AddressProcess();
		addressProcess.setMethod(requestContext.getRequestType().name());
		addressProcess.setStarttime(Instant.now());
		addressProcess.setInput(String.valueOf(requestContext.getRequestBodyData().getObjectNumber()));
		addressProcess.setResult(responseEntityObj.getStatusCode().toString());
		addressProcessRepository.save(addressProcess);

		ateObjectRoot.setAddressProcessId(addressProcess.getId());
		ateObjectRoot.setObjectNumber(requestContext.getRequestBodyData().getObjectNumber());

		return ateObjectRoot;
	}

	public AddressObjectRoot processBuildingsRequest(RequestContext requestContext) {

		String userJsonObject = "";
		try {
			userJsonObject = ow.writeValueAsString(requestContext.getRequestBodyData());
		} catch (JsonProcessingException e) {
			logger.error("Can not create User object for request");
		}

		logger.info("Call request with context: " + requestContext);
		logger.info("Call processBuildingsRequest with  URL= " + requestContext.getUrl() + "; RequestBody = "
				+ userJsonObject);

		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(userJsonObject.toString(), headers);

		ResponseEntity<ArrayList<AddressObjectResult>> responseEntityObj = restTemplate.exchange(
				requestContext.getUrl(), HttpMethod.POST, request,
				new ParameterizedTypeReference<ArrayList<AddressObjectResult>>() {
				});

		AddressObjectRoot addressObjectRoot = new AddressObjectRoot(responseEntityObj.getBody(), 0L, 0);

		try {
			objectMapper.writeValue(new File(tempfolder + requestContext.getFileName()), addressObjectRoot);
			logger.info("File created: " + tempfolder + requestContext.getFileName());
		} catch (IOException e) {
			logger.error("Can not write object to json file: " + e.getMessage());
		}

		AddressProcess addressProcess = new AddressProcess();
		addressProcess.setMethod(requestContext.getRequestType().name());
		addressProcess.setStarttime(Instant.now());
		addressProcess.setInput(String.valueOf(requestContext.getRequestBodyData().getObjectNumber()));
		addressProcess.setResult(responseEntityObj.getStatusCode().toString());
		addressProcessRepository.save(addressProcess);

		addressObjectRoot.setAddressProcessId(addressProcess.getId());
		addressObjectRoot.setObjectNumber(requestContext.getRequestBodyData().getObjectNumber());

		return addressObjectRoot;
	}

	public int saveRegions(AteObjectRoot ateObjectRoot) {
		if (ateObjectRoot == null) {
			return -1;
		}
		for (AteObjectResult p : ateObjectRoot.getResult()) {
			if (p.getNameObject() != null) {
				logger.info("Call DB add_nca_dict_adr_region with: p_code= " + p.getObjectNumber() + ", p_name= "
						+ p.getNameObject());
				regionsRepository.callProcedure(p.getObjectNumber(), p.getNameObject());
			}
		}
		updateAddressProcess(ateObjectRoot.getAddressProcessId(), "SUCCESS");
		return 0;
	}

	public int saveSelsovets(AteObjectRoot ateObjectRoot) {
		if (ateObjectRoot == null) {
			return -1;
		}
		for (AteObjectResult p : ateObjectRoot.getResult()) {
			if (p.getNameObject() != null && ateObjectRoot.getObjectNumber() != 0) {
				logger.info("Call DB add_nca_dict_adr_selsovet with: p_code= " + p.getObjectNumber() + ", p_name= "
						+ p.getNameObject() + ", p_district_code= " + ateObjectRoot.getObjectNumber());
				selsovetsRepository.callProcedure(p.getObjectNumber(), p.getNameObject(),
						ateObjectRoot.getObjectNumber());
			}
		}
		updateAddressProcess(ateObjectRoot.getAddressProcessId(), "SUCCESS");
		return 0;
	}

	public int saveCities(AteObjectRoot ateObjectRoot) {
		if (ateObjectRoot == null) {
			return -1;
		}
		for (AteObjectResult p : ateObjectRoot.getResult()) {
			if (p.getNameObject() != null && ateObjectRoot.getObjectNumber() != 0) {
				logger.info("Call DB add_nca_dict_adr_city with: p_code= " + p.getObjectNumber() + ", p_name= "
						+ p.getNameObject() + ", p_selsovet_code= " + ateObjectRoot.getObjectNumber());
				citiesRepository.callProcedure(p.getObjectNumber(), p.getNameObject(), ateObjectRoot.getObjectNumber());
			}
		}
		updateAddressProcess(ateObjectRoot.getAddressProcessId(), "SUCCESS");
		return 0;
	}

	public int saveStreetTypes(AddressObjectRoot addressObjectRoot) {
		if (addressObjectRoot == null) {
			return -1;
		}

		List<String> elementTypeNames = addressObjectRoot.getResult().stream()
				.filter(typeName -> typeName.getElementTypeName() != null)
				.filter(typeName -> !typeName.getElementTypeName().isEmpty())
				.filter(distinctByKey(AddressObjectResult::getElementTypeName))
				.map(AddressObjectResult::getElementTypeName).collect(Collectors.toList());
		logger.info("StreetTypes= " + elementTypeNames);

		for (String p : elementTypeNames) {
			if (p != null) {
				logger.info("Call DB add_nca_dict_adr_street_type with: p_name= " + p);
				streetTypeRepository.callProcedure(p);
			}
		}
		updateAddressProcess(addressObjectRoot.getAddressProcessId(), "SUCCESS");
		return 0;
	}

	public int saveCityTypes(AddressObjectRoot addressObjectRoot) {
		if (addressObjectRoot == null) {
			return -1;
		}

		List<String> categoryNames = addressObjectRoot.getResult().stream()
				.filter(distinctByKey(AddressObjectResult::getCategoryName)).map(AddressObjectResult::getCategoryName)
				.collect(Collectors.toList());
		logger.info("CityTypes= " + categoryNames);

		for (String p : categoryNames) {
			if (p != null) {
				logger.info("Call DB add_nca_dict_adr_city_type with: p_name= " + p);
				cityTypeRepository.callProcedure(p);
			}
		}
		updateAddressProcess(addressObjectRoot.getAddressProcessId(), "SUCCESS");
		return 0;
	}

	public int saveStreet(AddressObjectRoot addressObjectRoot) {
		if (addressObjectRoot == null) {
			return -1;
		}
		
		List<AddressObjectResult> distinctStreetList = addressObjectRoot.getResult().stream()
			      .filter(distinctByKeyClass(StreetDto::new))
			      .collect(Collectors.toList());
		
		//logger.info("Streets distinct= " + distinctStreetList);
		
		for (AddressObjectResult p : distinctStreetList) {
			if (p.getElementTypeName() != null && addressObjectRoot.getObjectNumber() != 0) {
				logger.info("Call DB add_nca_dict_adr_street with: p_name= " + p.getElementName()
						+ ", p_street_type_name=" + p.getElementTypeName() + ", p_city_name=" + p.getNameobject()
						+ ", p_district_name=" + p.getDistrict());
				streetRepository.callProcedure(p.getElementName(), p.getElementTypeName(), p.getNameobject(),
						p.getDistrict());
			}
		}
		updateAddressProcess(addressObjectRoot.getAddressProcessId(), "SUCCESS");
		return 0;
	}

	
	public int saveAddresses(AddressObjectRoot addressObjectRoot) {
		if (addressObjectRoot == null) {
			return -1;
		}
		for (AddressObjectResult p : addressObjectRoot.getResult()) {
			if (p.getNameobject() != null && p.getAdrNum() != 0 && addressObjectRoot.getObjectNumber() != 0) {
				logger.info("Call DB add_nca_dict_adr_address: p_code= " + p.getAdrNum());
				addressesRepository.callProcedure(p.getAdrNum(), p.getPropTypeNameExt(), p.getRegion(), p.getDistrict(),
						p.getVillage(), p.getCategoryName(), p.getNameobject(), p.getElementTypeName(),
						p.getElementName(), p.getNumHouse(), p.getNumCorp(), p.getIndHouse(), p.getKm(), p.getIndex(),
						p.getRemark());
			}
		}
		updateAddressProcess(addressObjectRoot.getAddressProcessId(), "SUCCESS");
		return 0;
	}
	
	

	public int saveAddressObjects(AddressObjectRoot addressObjectRoot) {
		if (addressObjectRoot == null) {
			return -1;
		}

		saveCityTypes(addressObjectRoot);
		saveStreetTypes(addressObjectRoot);
		saveStreet(addressObjectRoot);
		saveAddresses(addressObjectRoot);

		return 0;
	}

	public int saveDistricts(AteObjectRoot ateObjectRoot) {
		if (ateObjectRoot == null) {
			return -1;
		}
		for (AteObjectResult p : ateObjectRoot.getResult()) {
			if (p.getNameObject() != null && ateObjectRoot.getObjectNumber() != 0) {
				logger.info("Call DB add_nca_dict_adr_district with: p_code= " + p.getObjectNumber() + ", p_name= "
						+ p.getNameObject() + ", p_region_code= " + ateObjectRoot.getObjectNumber());
				districtsRepository.callProcedure(p.getObjectNumber(), p.getNameObject(),
						ateObjectRoot.getObjectNumber());
			}
		}
		updateAddressProcess(ateObjectRoot.getAddressProcessId(), "SUCCESS");
		return 0;
	}

	public void updateAddressProcess(Long processId, String processResult) {
		if (processId != 0L) {
			logger.info("Update ADDRESS_PROCESS for Id= " + processId + ". Result= " + processResult);
			AddressProcess apObject;
			apObject = addressProcessRepository.findById(processId).get();
			apObject.setSuccess(processResult);
			apObject.setEndtime(Instant.now());
			addressProcessRepository.save(apObject);
		}
	}

	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		Set<Object> seen = ConcurrentHashMap.newKeySet();
		return t -> seen.add(keyExtractor.apply(t));
	}
	

	public static <T> Predicate<T> 
	    distinctByKeyClass(final Function<? super T, Object> keyExtractor) 
	{
	    Map<Object, Boolean> seen = new ConcurrentHashMap<>();
	    return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

}
