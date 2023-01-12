package by.com.lifetech.addressBook.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class RequestContext {

	@Autowired
	RequestBodyData requestBodyData;
	@Autowired
	RequestBodyDataWithoutParam requestBodyDataWithoutParam;
	RequestType requestType;
	String url;
	String fileName;
	
	public RequestBodyDataWithoutParam getRequestBodyDataWithoutParam() {
		requestBodyDataWithoutParam.setUser(requestBodyData.getUser());
		return requestBodyDataWithoutParam;
	}
}
