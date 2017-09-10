
package com.spring.security.repository.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import com.spring.security.entity.Merchandiser;
import com.spring.security.entity.Status;
import com.spring.security.exception.ApplicationException;
import com.spring.security.mapper.MerchandiserResponseMapper;
import com.spring.security.repository.MerchandiserAPIRepository;

@Component
public class MerchandiserAPIRepositoryImpl implements MerchandiserAPIRepository{


	@Autowired  
	JdbcTemplate jdbcTemplate;

	/*@Value("${ServerKey}")*/
	private String CLIENT_TOKEN="AAAASQXYY84:APA91bFshcteqRrFnaNeXfuQ02EgwJT-j4IbyXPKFUrM-ffShQ_7WLF1M-mNVTV8HgFFT40Y5cPDPylG9apdewVHLVzL-8aad-OEQn0ETwzSKvJLOLdM3usUvy6VeoPMlgWENOwa7Six";

	/*	@Value("${requestURI}")*/
	private String requestURI="http://fcm.googleapis.com/fcm/send";


	public String retrieveMerchandiserName(String phoneno) throws ApplicationException{
		String methodName="retrieveMerchandiserName";

		StringBuilder queryString = new StringBuilder("select * from vm_merchandiser where phoneno=?");
		List<Merchandiser> responseList= jdbcTemplate.query(queryString.toString(),new String[]{phoneno},new MerchandiserResponseMapper());

		if(responseList !=null && responseList.size()>0){
			return responseList.get(0).getName();
		}
		else{
			throw new ApplicationException(new Status(500, "Merchandiser is not present"), "Merchandiser is not present !!");
		}
	}
}
