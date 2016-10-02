package flyml.housestat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.druid.DruidPlugin;

import flyml.housestat.models.NJHouseSimpleSummary;
import flyml.housestat.models.NJHouseSupplySummary;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class NJHouseProcessor implements PageProcessor{

	private Site site = Site.me()
			.setCharset("gb2312")
			.setRetryTimes(3)
			.setSleepTime(100);
	
	private DruidPlugin druidPlugin = null;
	private ActiveRecordPlugin activeRecordPlugin = null;
	
	public Site getSite() {
		return site;
	}
	
	
	/**
	 * 头部走马灯的信息解析
	 * @param page
	 * @return
	 */
	private Map<String, Object> processSimpleSummary(Page page) {
		Map<String, Object> map = Maps.newHashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		map.put("当前时间", sdf.format(new Date()));
		map.put("update_time", sdf.format(new Date()));
		
		
		String css = "body > table:nth-child(9) > tbody > tr > td > table > tbody > tr > td:nth-child(2) > marquee";
		String text = page.getHtml().css(css).get();
		Document doc = Jsoup.parse(text);
		text = doc.text();
		
		// 认购套数数: 认购 563套
		String subscribe = StringUtils.substringBetween(text, "认购", "套，");
//		map.put("认购套数", subscribe);
		map.put("subscribe_unit", subscribe);
		
		String deal = StringUtils.substringBetween(text, "成交", "套");
//		map.put("成交套数", deal);
		map.put("deal_unit", deal);
		
		String newpublished = StringUtils.substringBetween(text, "存量房新上市房源", "套");
//		map.put("存量房新上市房源", newpublished);
		map.put("new_pub_2nd_house_unit", newpublished);
		
		return map;
	}
	
	/**
	 * 楼盘供应信息解析逻辑
	 * @param page  页面
	 * @param css   解析的css选择器，可以从chrome 上面获取
	 * @param replacedStrs	需要替换的字符串
	 * @param keys	返回的Map的keys
	 * @param sep	分割字符串的字符
	 * @return
	 */
	private Map<String, String> supplySummaryParser(Page page, String css, String[] replacedStrs, String[] keys, String sep) {
		Map<String, String> map = Maps.newHashMap();
		String tmpStr = page.getHtml().css(css).get();
		Document tmpDoc = Jsoup.parse(tmpStr);
		
		List<String> replacementList = Lists.newArrayList();
		for(String replacedStr : replacedStrs) {
			replacementList.add("");
		}
		String[] replacementStrs = replacementList.toArray(new String[replacementList.size()]);
		String text = StringUtils.replaceEach(tmpDoc.text(), replacedStrs, replacementStrs);
		
		String[] tmpStrArr = StringUtils.split(text, sep);
		if(tmpStrArr.length == keys.length) {
			for(int i = 0; i < keys.length; i++) 
				map.put(keys[i], tmpStrArr[i].trim());
		}
		return map;
	}
	
	private Map<String, Object> processSupplySummary(Page page) {
		Map<String, Object> map = Maps.newHashMap();
		
		// 全市入网项目总数/总套数/总面积
		String css = "body > table:nth-child(11) > tbody > tr >"
				+ " td:nth-child(2) > table > tbody > "
				+ " tr:nth-child(1) > td:nth-child(1)";
		String[] replacedStrs = {
			"全市入网项目总数/总套数/总面积：","万M2","    "
		};
//		String[] keys = "全市入网项目总数,全市入网总套数,全市入网总面积".split(",");
		String[] keys = "total_item_cnt,total_unit,total_area".split(",");
		Map<String, String> map1 = supplySummaryParser(page, css, replacedStrs, keys, "/");
		map.putAll(map1);
		
		// 可售套数/面积
		css = "body > table:nth-child(11) > tbody > tr >"
				+ " td:nth-child(2) > table > tbody >"
				+ " tr:nth-child(1) > td:nth-child(2)";
		replacedStrs = new String[]{
			"可售套数/面积：","万M2","    "
		};
//		keys = "全市可售套数,全市可售面积".split(",");
		keys = "onsale_unit,onsale_area".split(",");
		Map<String, String> map2 = supplySummaryParser(page, css, replacedStrs, keys, "/");
		map.putAll(map2);
		
		// 其中住宅可售套数/面积
		css = "body > table:nth-child(11) > tbody > tr >"
				+ " td:nth-child(2) > table >"
				+ " tbody > tr:nth-child(2) > td:nth-child(1)";
		replacedStrs = new String[]{
				"其中住宅可售套数/面积：","万M2","    "
		};
//		keys = "全市其中住宅可售套数,全市其中住宅可售面积".split(",");
		keys = "house_onsale_unit,house_onsale_area".split(",");
		Map<String,String> map3 = supplySummaryParser(page,css,replacedStrs,keys,"/");
		map.putAll(map3);
		
		// 非住宅可售套数/面积
		css = "body > table:nth-child(11) > tbody > tr >"
				+ " td:nth-child(2) > table > tbody > "
				+ "tr:nth-child(2) > td:nth-child(2)";
		replacedStrs = new String[]{
				"非住宅可售套数/面积：","万M2","    "
		};
//		keys = "全市非住宅可售套数,全市非住宅可售面积".split(",");
		keys = "non_house_onsale_unit,non_house_onsale_area".split(",");
		Map<String,String> map4 = supplySummaryParser(page,css,replacedStrs,keys,"/");
		map.putAll(map4);
		
		// 本年上市住宅套数/面积
		css = "body > table:nth-child(11) > tbody > tr"
				+ " > td:nth-child(2) > table > tbody >"
				+ " tr:nth-child(3) > td:nth-child(1)";
		replacedStrs = new String[]{
				"本年上市住宅套数/面积：","万M2","    "
		};
		keys = "全市本年上市住宅套数,全市本年上市住宅面积".split(",");
		keys = "yearly_published_unit,yearly_published_area".split(",");
		Map<String,String> map5 = supplySummaryParser(page,css,replacedStrs,keys,"/");
		map.putAll(map5);
		
	    // 本月上市住宅套数/面积
  		css = "body > table:nth-child(11) > tbody > tr"
  				+ " > td:nth-child(2) > table > tbody > "
  				+ "tr:nth-child(3) > td:nth-child(2)";
  		replacedStrs = new String[]{
  				"本月上市住宅套数/面积：","万M2","    "
  		};
//  	keys = "全市本月上市住宅套数,全市本月上市住宅面积".split(",");
  		keys = "monthly_published_house_unit,monthly_published_house_area".split(",");
  		Map<String,String> map6 = supplySummaryParser(page,css,replacedStrs,keys,"/");
  		map.putAll(map6);		
		
		return map;
	}
	
	
	public void process(Page page) {
		// 获取楼盘供应情况
		try {
			Map<String, Object> supplySummaryData = processSupplySummary(page);
			NJHouseSupplySummary.dao.put(supplySummaryData).save();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		// 头部走马灯的简要信息
		Map<String, Object> simpleSummaryData = processSimpleSummary(page);
		NJHouseSimpleSummary.dao.put(simpleSummaryData).save();
	}
	
	
	private void startDB() {
		druidPlugin = new DruidPlugin("jdbc:mysql://127.0.0.1/flyml_webdata?characterEncoding=utf-8", "root", "");
        druidPlugin.start();
        activeRecordPlugin = new ActiveRecordPlugin(druidPlugin);
        activeRecordPlugin.addMapping("njhouse_simple_summary", NJHouseSimpleSummary.class);
        activeRecordPlugin.addMapping("njhouse_supply_summary", NJHouseSupplySummary.class);
        activeRecordPlugin.start();
	}
	
	private void stopDB() {
		activeRecordPlugin.stop();
		activeRecordPlugin = null;
		druidPlugin.stop();
		druidPlugin = null;
	}
	
	public static void main(String[] args) {
		NJHouseProcessor processor = new NJHouseProcessor();
		
		processor.startDB();
		Spider.create(processor)
			.addUrl("http://www.njhouse.com.cn/index_tongji.php")
			.thread(1)
			.run();
		processor.stopDB();
	}
	

}
