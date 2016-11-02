package cn.sfw.zju.system.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import cn.sfw.zju.system.service.IntervalService;
import net.sf.json.JSONArray;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.M5P;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;

@Controller
@RequestMapping("/system/interval")
public class IntervalAction {
	
	private final static Log log = LogFactory.getLog(IntervalAction.class);
	
	@Autowired
	private IntervalService intervalService;
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return Message 所有的测试点(CST_ID,CST_NAME)
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllCstId/", method = RequestMethod.POST)
	public Message getAllCstId(HttpServletRequest request,HttpServletResponse response) {
		Message message = new Message();
		List<Map<String, Object>> result = intervalService.getAllCstId();
		JSONArray array = JSONArray.fromObject(result);
		message.setData(array);
		message.setCode("SUCCESS");
		return message;
	}
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @return Message 所有的测试点(CST_ID,CST_NAME,timeInterval)
	 */
	@ResponseBody
	@RequestMapping(value = "/getAllById/", method = RequestMethod.POST)
	public Message getAllById(HttpServletRequest request,HttpServletResponse response,@RequestBody Map<String,Object> map) {
		Message message = new Message();
		
		String cst_id = (String) map.get("cst_id");
		Long timeInterval=Long.valueOf(String.valueOf(map.get("timeInterval")))*60000;
		//获取该站点最小的时间
		Long mdi_ts = intervalService.getMinTimeById(cst_id);
		System.out.println(mdi_ts);
		Map<String, Object> parmMap=new HashMap<String, Object>();
		parmMap.put("cst_id", cst_id);
		parmMap.put("mdi_ts", mdi_ts);
		parmMap.put("timeInterval", timeInterval);
		//获取该站点从开始到最后所有的数据(cst_id,cst_name,mdi_ts,now,bef)
		List<Map<String, Object>> result =intervalService.getAllById(parmMap);	
		try {
			M5P classifier= cartModel(createInstances(result));
			//预测未来一天
			Date date= new Date();
			List<Map<String, Object>> flist=createForcastDate(new Date(date.getYear(),date.getMonth(),date.getDate()), 1, timeInterval);
			Instances test= createInstances(flist);
			for(int i=0;i<test.size();i++){
				flist.get(i).replace("NOW",classifier.classifyInstance(test.get(i)) );
				System.out.println(flist.get(i).get("MDI_TS")+": "+flist.get(i).get("NOW"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result.size());
		
		return message;
	}
	
	public List<Map<String, Object>> createForcastDate(Date date,int length,long timeInterval){
		Date startDate=date;
		Date endDate= DateUtil.resetDate(startDate,length);
		System.out.println(startDate.toLocaleString());
		System.out.println(endDate.toLocaleString());
		Long n=(endDate.getTime()-startDate.getTime())/timeInterval;
		List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
		for(int i=0;i<n;i++){
			Map<String, Object> map= new HashMap<String, Object>();
			map.put("MDI_TS", endDate.getTime()+i*timeInterval);
			map.put("NOW",0);
			map.put("BEF",0);
			list.add(map);
		}
		return list;
	}
	
	public Instances createInstances(List<Map<String, Object>> list){
		//声明attributes
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		Attribute mdi_ts = new Attribute("MDI_TS");
		Attribute now = new Attribute("NOW");
		Attribute bef = new Attribute("BEF");
		atts.add(mdi_ts);
		atts.add(now);
		atts.add(bef);
		//
		Instances df = new Instances("Data", atts, 0);
		for(int i=0;i<list.size();i++){
			Instance inst = new DenseInstance(3); 
			// Set instance's values for the attributes "length", "weight", and "position"
			inst.setValue(mdi_ts, Double.valueOf(String.valueOf(list.get(i).get("MDI_TS")))); 
			inst.setValue(now, Double.valueOf(String.valueOf(list.get(i).get("NOW"))));
			inst.setValue(bef, Double.valueOf(String.valueOf(list.get(i).get("BEF"))));
			// Set instance's dataset to be the dataset "race" 
			inst.setDataset(df); 
			df.add(inst);
		}
		df.setClassIndex(1);
		return df;
	}
	
	public static Instances boostrapSample(Instances data) {
        String[] options = {"-S","1","-Z","50"};
        Resample convert = new Resample();
        try {
            convert.setOptions(options);
            convert.setInputFormat(data);
            data = Filter.useFilter(data, convert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
	
	/**
	 * Valid options are:
	 -N
	  Use unpruned tree/rules
	 
	 -U
	  Use unsmoothed predictions
	 
	 -R
	  Build regression tree/rule rather than a model tree/rule
	 
	 -M <minimum number of instances>
	  Set minimum number of instances per leaf
	  (default 4)
	 
	 -L
	  Save instances at the nodes in
	  the tree (for visualization purposes)
	  -s 
		向量机的种类，包括分类（C-SVC,nu-SVC），回归（epsilon-SVR,nu-SVR）以及分布估计（one-class SVM），c-svc中c的范围是1到正无穷 
		nu-svc中nu的范围是0到1，还有nu是错分样本所占比例的上界，支持向量所占比列的下界。 
		-k 
		核函数类型，多项式、线性、高斯、tanh函数。 
		-d 
		degree：核函数中的degree设置(针对多项式核函数)(默认3) 
		-g 
		核函数中的gamma函数设置(针对多项式/rbf/sigmoid核函数)(默认1/ k) 
		-r 
		coef0：核函数中的coef0设置(针对多项式/sigmoid核函数)((默认0) 
		-c 
		cost：设置C-SVC，e -SVR和v-SVR的参数(损失函数)(默认1) 
		-n 
		nu：设置v-SVC，一类SVM和v- SVR的参数(默认0.5) 
		-Z 
		对输入的数据进行normalization，默认是非开启 
		-J 
		数据全部都是numric的时候，进行二进制编码 
		-V 
		缺失值是否处理，默认是开启 
		-p 
		p：设置e -SVR 中损失函数p的值(默认0.1) 
		-m 
		cachesize：设置cache内存大小，以MB为单位(默认40) 
		-e 
		eps：设置允许的终止判据(默认0.001) 
		-h 
		shrinking：是否使用启发式，0或1(默认1) 
		-wi 
		weight：设置第几类的参数C为weight*C(C-SVC中的C)(默认1)
	 * @param data
	 * @throws Exception
	 */
	 public M5P cartModel(Instances data) throws Exception{
			M5P classifier =new M5P();
	        String[] options = {"-M","5","-R"};
	        classifier.setOptions(options);
	        classifier.buildClassifier(data);
	        Evaluation eval = new Evaluation(data);
	        //eval.evaluateModel(classifier, data);
			eval.crossValidateModel(classifier, data, 10, new Random(1));
			System.out.println(eval.toSummaryString("\nResult", false));
			//System.out.println(eval.toClassDetailsString());
			return classifier;
	 }
}
