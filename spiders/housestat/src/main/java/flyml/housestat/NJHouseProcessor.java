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
import com.google.gson.Gson;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class NJHouseProcessor implements PageProcessor{

	private Site site = Site.me()
			.setCharset("gb2312")
			.setRetryTimes(3)
			.setSleepTime(100);
	
	public Site getSite() {
		return site;
	}
	
	/**
	 * 头部走马灯的信息解析
	 * @param page
	 * @return
	 */
	private Map<String, String> processSimpleSummary(Page page) {
		Map<String, String> map = Maps.newHashMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("当前时间", sdf.format(new Date()));
		
		String css = "body > table:nth-child(9) > tbody > tr > td > table > tbody > tr > td:nth-child(2) > marquee";
		String text = page.getHtml().css(css).get();
		Document doc = Jsoup.parse(text);
		text = doc.text();
		
		// 认购套数数: 认购 563套
		String confirm = StringUtils.substringBetween(text, "认购", "套，");
		map.put("认购套数", confirm);
		
		String deal = StringUtils.substringBetween(text, "成交", "套");
		map.put("成交套数", deal);
		
		String newpublished = StringUtils.substringBetween(text, "存量房新上市房源", "套");
		map.put("存量房新上市房源", newpublished);
		
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
		if(tmpStrArr.length != keys.length) {
			return null;
		} 
		for(int i = 0; i < keys.length; i++) {
			map.put(keys[i], tmpStrArr[i].trim());
		}
		return map;
	}
	
	private Map<String, String> processSupplySummary(Page page) {
		Map<String, String> map = Maps.newHashMap();
		
		// 全市入网项目总数/总套数/总面积
		String css = "body > table:nth-child(11) > tbody > tr >"
				+ " td:nth-child(2) > table > tbody > "
				+ " tr:nth-child(1) > td:nth-child(1)";
		String[] replacedStrs = {
			"全市入网项目总数/总套数/总面积：","万M2","    "
		};
		String[] keys = "全市入网项目总数,全市入网总套数,全市入网总面积".split(",");
		Map<String, String> map1 = supplySummaryParser(page, css, replacedStrs, keys, "/");
		map.putAll(map1);
		
		// 可售套数/面积
		css = "body > table:nth-child(11) > tbody > tr >"
				+ " td:nth-child(2) > table > tbody >"
				+ " tr:nth-child(1) > td:nth-child(2)";
		replacedStrs = new String[]{
			"可售套数/面积：","万M2","    "
		};
		keys = "全市可售套数,全市可售面积".split(",");
		Map<String, String> map2 = supplySummaryParser(page, css, replacedStrs, keys, "/");
		map.putAll(map2);
		
		// 其中住宅可售套数/面积
		css = "body > table:nth-child(11) > tbody > tr >"
				+ " td:nth-child(2) > table >"
				+ " tbody > tr:nth-child(2) > td:nth-child(1)";
		replacedStrs = new String[]{
				"其中住宅可售套数/面积：","万M2","    "
		};
		keys = "全市其中住宅可售套数,全市其中住宅可售面积".split(",");
		Map<String,String> map3 = supplySummaryParser(page,css,replacedStrs,keys,"/");
		map.putAll(map3);
		
		// 非住宅可售套数/面积
		css = "body > table:nth-child(11) > tbody > tr >"
				+ " td:nth-child(2) > table > tbody > "
				+ "tr:nth-child(2) > td:nth-child(2)";
		replacedStrs = new String[]{
				"非住宅可售套数/面积：","万M2","    "
		};
		keys = "全市非住宅可售套数,全市非住宅可售面积".split(",");
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
		Map<String,String> map5 = supplySummaryParser(page,css,replacedStrs,keys,"/");
		map.putAll(map5);
		
	    // 本月上市住宅套数/面积
  		css = "body > table:nth-child(11) > tbody > tr"
  				+ " > td:nth-child(2) > table > tbody > "
  				+ "tr:nth-child(3) > td:nth-child(2)";
  		replacedStrs = new String[]{
  				"本月上市住宅套数/面积：","万M2","    "
  		};
  		keys = "全市本月上市住宅套数,全市本月上市住宅面积".split(",");
  		Map<String,String> map6 = supplySummaryParser(page,css,replacedStrs,keys,"/");
  		map.putAll(map6);
		
		return map;
	}
	
	
	public void process(Page page) {
		// 获取楼盘供应情况
		processSupplySummary(page);
		
		// 头部走马灯的简要信息
		processSimpleSummary(page);
	
		
	}
	
	public static void main(String[] args) {
		Spider.create(new NJHouseProcessor())
		.addUrl("http://www.njhouse.com.cn/index_tongji.php")
		.thread(1).run();
		
		}

}
