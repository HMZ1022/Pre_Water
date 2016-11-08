package cn.sfw.zju.system.action;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import cn.sfw.zju.common.Message;
import cn.sfw.zju.common.util.DateUtil;
import cn.sfw.zju.system.service.Interval_DService;
import cn.sfw.zju.system.util.InstancesUtils;
import cn.sfw.zju.system.util.WekaUtils;
import weka.classifiers.Classifier;
import weka.classifiers.meta.CVParameterSelection;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;


@Controller
@RequestMapping("/system/modelEvaluation")
public class ModelEvaluationAction {
	
	private final static Log log = LogFactory.getLog(IntervalAction.class);
	@Autowired
	private Interval_DService interval_DService;
	
	private WekaUtils wekaUtils=WekaUtils.getInstance();
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/modelEvalution/", method = RequestMethod.POST)
	public Message getAllById(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map) {
		Message message = new Message();
		
		//获取train instances
		String cst_id =(String) map.get("cst_id");
		Long beginDate=DateUtil.convertTimeToLong((String)map.get("beginDate"));
		Long endDate=DateUtil.convertTimeToLong((String)map.get("endDate"));
		Map<String, Object> parmMap =new HashMap<>(); 
		parmMap.put("cst_id", cst_id);
		parmMap.put("beginDate", beginDate);
		parmMap.put("endDate", endDate);
		List<Map<String, Object>> list= interval_DService.getByCstIdAndTime(parmMap);
		Instances train=InstancesUtils.createPreDayInstances(list);
		Map<String, Object> testparmMap =new HashMap<>();
		testparmMap.put("cst_id", cst_id);
		testparmMap.put("beginDate", endDate);
		testparmMap.put("endDate", DateUtil.convertTimeToLong("2016-09-21 00:00:00"));
		List<Map<String, Object>> testList= interval_DService.getByCstIdAndTime(testparmMap);
		Instances test=InstancesUtils.createPreDayInstances(testList);
		//处理数据
		
		try {
			train = wekaUtils.toBinary(wekaUtils.normalizeV(wekaUtils.standardizeV(wekaUtils.replaceMissingV(train))));
			test = wekaUtils.toBinary(wekaUtils.normalizeV(wekaUtils.standardizeV(wekaUtils.replaceMissingV(test))));
		} catch (Exception e1) {
			log.error("instances 数据处理异常");
			e1.printStackTrace();
		}
		
		//算法选择
		String algorithm =(String)map.get("algorithm");
		Classifier classifier=null;
		CVParameterSelection cvParameterSelection = new CVParameterSelection();
		try {
			if(algorithm.equals("tree")){
				//m5p 根据所选训练集 进行参数训练
				String[] options ={"-M", "1", "-N"};
				classifier=wekaUtils.getM5PClassifer(train, options);
				cvParameterSelection.setClassifier(classifier);
				cvParameterSelection.addCVParameter("M 1 20 20");
				cvParameterSelection.setNumFolds(10);
				cvParameterSelection.buildClassifier(train);
				log.info("最优参数："+Utils.joinOptions(cvParameterSelection.getBestClassifierOptions()));
				classifier= wekaUtils.getM5PClassifer(train, cvParameterSelection.getBestClassifierOptions());
			}else if (algorithm.equals("smoreg")){
				//svoreg				
				String[] options={"-C","0.5","-N","0"};
				classifier= wekaUtils.getSMOregClassifer(train, options);
				cvParameterSelection.setClassifier(classifier);
				cvParameterSelection.addCVParameter("C 0.1 2 20");
				cvParameterSelection.setNumFolds(5);
				cvParameterSelection.buildClassifier(train);
				log.info("最优参数："+Utils.joinOptions(cvParameterSelection.getBestClassifierOptions()));
				classifier=wekaUtils.getSMOregClassifer(train, cvParameterSelection.getBestClassifierOptions());
			}else if(algorithm.equals("bp")){
				//bp
				cvParameterSelection= new CVParameterSelection();
			    String[] options={};
			    classifier= wekaUtils.getBPClassifer(train, options);
			    cvParameterSelection.setClassifier(classifier);
				cvParameterSelection.setNumFolds(10);
				cvParameterSelection.buildClassifier(train);
				System.out.println(Utils.joinOptions(cvParameterSelection.getBestClassifierOptions()));				
				classifier= wekaUtils.getBPClassifer(train, cvParameterSelection.getBestClassifierOptions());
			}else{
				log.info("没有该算法");
			}
		} catch (Exception e) {
			log.error("训练模型失败");
		}
		
		//定义预测结果。
		List<Map<String, Object>> resultList = new ArrayList<>();

		//进行预测。
		double sum=test.numInstances();
		NumberFormat fmt = NumberFormat.getPercentInstance();  
        fmt.setMaximumFractionDigits(2);//最多两位百分小数，如25.23%  
        double sumt=0;
		double num=0;
		for(int i=0;i<sum;i++){
			Map<String, Object>  resultMap= new HashMap<>();
			resultMap.put("id",cst_id);
			resultMap.put("name",cst_id);
			resultMap.put("time", testList.get(i).get("CDATE"));
			double predictValue;
			double actualValue;
			String deviation;
			try {
				predictValue=classifier.classifyInstance(test.instance(i));
				actualValue=test.instance(i).classValue();
				deviation=fmt.format((predictValue-actualValue)/actualValue);							
				sumt+=Math.abs(predictValue-actualValue);
				num+=test.instance(i).classValue();
				resultMap.put("actualValue",actualValue);
				resultMap.put("predictValue",predictValue);
				resultMap.put("deviation",deviation);
			} catch (Exception e) {
				e.printStackTrace();
			}
			resultList.add(resultMap);
		}
		System.out.println(fmt.format(sumt/num));
		message.setData(resultList);
		message.setCode("SUCCESS");
		return message;
	}
	
}
