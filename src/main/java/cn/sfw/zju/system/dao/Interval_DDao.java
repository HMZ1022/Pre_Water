package cn.sfw.zju.system.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Interval_DDao {
	/**
	 * @param map{cst_id,beginDate,endDate}
	 * @return
	 */
	public List<Map<String, Object>> getByCstIdAndTime(Map<String, Object> map);
	
	public Map<String, Object> getMaxDateAndMinDateByCstId(String cst_id);
	
}
