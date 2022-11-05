package com.optidpp.common.image;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Service;

import com.optidpp.common.dao.Dao;
import com.optidpp.common.logger.Log;

@Service("ImageService")
public class ImageService {
	@Inject
	@Named("Dao")
	private Dao dao;

	public List<?> getDownloadImageInfo(Map<String, Object> paramMap) throws Exception {
		// TODO Auto-generated method stub
		Log.Debug("ImageService.getDownloadImageInfo Start");
		return (List<?>)dao.getList("getDownloadImageInfo", paramMap);
	}

}
