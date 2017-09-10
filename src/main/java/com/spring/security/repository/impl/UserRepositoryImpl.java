/**
 * 
 */
package com.spring.security.repository.impl;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.spring.security.entity.Merchandiser;
import com.spring.security.entity.Status;
import com.spring.security.exception.ApplicationException;
import com.spring.security.mapper.MerchandiserResponseMapper;
import com.spring.security.repository.UserRepository;





/**
 * @author Shashank Gheek
 *
 */

@Component
public class UserRepositoryImpl implements UserRepository {
	
	

	 @Value("${otp.block.duration}")
	 private String otpValidDuration;
	 
//	 @Value("${wrongOtp.block.duration}")
//	 private String wrongOtpBlockduration;
	 

	 @Autowired
	 private JdbcTemplate jdbcTemplate;
	 

	public Merchandiser getMerchandiserDetail(String phoneNo) throws ApplicationException{
		
		StringBuilder sb = new StringBuilder("SELECT * FROM vm_merchandiser WHERE phoneNo=?");
		Merchandiser merchdetails = jdbcTemplate.queryForObject(sb.toString(), new String[]{phoneNo}, new MerchandiserResponseMapper());
		if(merchdetails!=null){
//			logger.debug(methodName+" merchdetails Registered");
			return merchdetails;
		}else{
			throw new ApplicationException(new Status(500, "Merchandiser not Registered"), "Merchandiser not registered for phoneNo :: "+phoneNo);
		}
	}
//
	public String saveOTPMerchandiser(String mobileNo,String otp,String requestToken,long otpGeneartionTime) throws ApplicationException{
//		String methodName="saveOTPMerchandiser";
		try{
//		logger.debug(methodName+" starts ");
		
		StringBuilder sbREquestId = new StringBuilder("select * from vm_merchandiser where phoneNo=? ");
		Merchandiser merchDetails =jdbcTemplate.queryForObject(sbREquestId.toString(),new String[]{mobileNo},new MerchandiserResponseMapper());
		
		if(merchDetails !=null){
			long validupto=otpGeneartionTime+Long.parseLong(otpValidDuration);
			int count = merchDetails.getRepetitioncount();
			
			StringBuilder sb = new StringBuilder("update vm_merchandiser set otp=?,otpgenerationtime=?,repetitioncount=?,requestID=?,validupto=? where phoneNo=?");
			
			jdbcTemplate.update(sb.toString(),new Object[]{otp,otpGeneartionTime,count+1,requestToken,validupto,mobileNo});
		}
		
		return mobileNo;
		}catch(Exception e){
//			logger.error(methodName + " error occured");
			throw new ApplicationException(new Status(500, "Something went wrong"), e);
		}
	}
	
	public void clearOTPOnLogoutMerchandiser(String phoneNo) throws ApplicationException{
//		String methodName="clearOTPOnLogoutAgency";
		try{
//		logger.debug(methodName+" starts ");
//		logger.debug(methodName+" phoneNo "+phoneNo);
		StringBuilder sb = new StringBuilder("update vm_merchandiser set otp=?,otpgenerationtime=?,repetitioncount=?,requestID=?,validupto=? where phoneNo=?");
		
		
		jdbcTemplate.update(sb.toString(),new Object[]{null,null,0,null,null,phoneNo});
		
		
		}catch(Exception e){
//			logger.error(methodName + " error occured");
			throw new ApplicationException(new Status(500, "Something went wrong"), e);
		}
	}
	
	public void selectAndDeleteToken(String mobileNo) throws ApplicationException{
		String methodName="saveToken";
		
		StringBuilder sb = new StringBuilder("SELECT count(*) FROM vm_token where mobileNo=?");
		int count = jdbcTemplate.queryForObject(sb.toString(), new String[]{mobileNo},Integer.class);
		if(count !=0){
			StringBuilder sbDelete = new StringBuilder("delete from vm_token where mobileNo=?");
			jdbcTemplate.update(sbDelete.toString(), new Object[]{mobileNo});
		}else{
			System.out.println(methodName+" Token already not exists");
		}
	}
	
	public void saveTokenStoreMerchandiser(String phoneNo,String token) throws ApplicationException{
		String methodName="saveToken";
		try{
		System.out.println(methodName+" starts ");
		System.out.println(methodName+" phoneNo "+phoneNo);
		System.out.println(methodName+" token "+token);
		
		Date date = new Date();
		long createon = date.getTime();
		long validupto=createon+Long.parseLong(otpValidDuration);
		
			StringBuilder sb = new StringBuilder("insert into vm_token(mobileNo,deviceId,token,isEnabled,tokengenerationtime,validupto)"
					+ " values(?,?,?,?,?,?)");
			jdbcTemplate.update(sb.toString(),phoneNo,null,token,"yes",createon,validupto);
		}catch(Exception e){
			System.out.println(methodName + " error occured");
			throw new ApplicationException(new Status(500, "Something went wrong"), e);
		}
	}
//	

	public int updateForWrongOTPSubmissionMerchandiser(String mobileNo) throws ApplicationException{
		String methodName="updateForWrongOTPSubmissionMerchandiser";
		int count=0;
		try{
			
			StringBuilder sbREquestId = new StringBuilder("select * from vm_merchandiser where phoneNo=? ");
			Merchandiser merchDetails =jdbcTemplate.queryForObject(sbREquestId.toString(),new String[]{mobileNo},new MerchandiserResponseMapper());
			
			if(merchDetails !=null){
				count = merchDetails.getWrongSubmissionCount();
				if(count>=3){
					Date date = new Date();
					
					
					long timeVlaue=merchDetails.getLastUpdationTime()+Long.parseLong(otpValidDuration);
					Date dateTillBlock = new Date(timeVlaue);
					
					
					if(date.getTime()>timeVlaue){
						//clear corrosponding otp details and start inserting new one
						count=1;
						clearOTPOnLogoutMerchandiser(mobileNo);
						Date dateOTP = new Date();
						
						StringBuilder sb = new StringBuilder("update vm_merchandiser set wrongSubmissionCount=?,lastupdationtime=? where phoneNo=?");
						jdbcTemplate.update(sb.toString(),new Object[]{1,dateOTP.getTime(),mobileNo});
						return count;
					}else{
						return count;
					}
				}else{
					Date updationDate = new Date();
					StringBuilder sb = new StringBuilder("update vm_merchandiser set wrongSubmissionCount=?,lastupdationtime=? where phoneNo=?");
					jdbcTemplate.update(sb.toString(),new Object[]{count+1,updationDate.getTime(),mobileNo});
					return count;
				}
			}
			
			}catch(Exception e){
				throw new ApplicationException(new Status(500, "Something went wrong"), e);
			}
		return count;
	}
}
