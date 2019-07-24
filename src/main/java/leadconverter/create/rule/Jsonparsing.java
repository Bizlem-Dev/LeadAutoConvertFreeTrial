package leadconverter.create.rule;

import org.json.JSONArray;
import org.json.JSONObject;

public class Jsonparsing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		JSONObject externaljson = new JSONObject(
				"{\"Status\":\"Saved Successfully\",\"user_name\":\"carrotrule_xyz.com\",\"project_name\":\"June22F01\",\"primary_key\":\"siva_bizlem.com\",\"Message\":{\"SFDC_SelectData\":{},\"EXTERNAL_DATA\":[{\"output_name\":\"\",\"output_type\":\"\",\"output_length\":\"\"}],\"EXCEL_DATA\":[{\"field\":\"String1\"},{\"field\":\"String2\"},{\"field\":\"String3\"},{\"field\":\"String4\"},{\"field\":\"String5\"},{\"field\":\"String6\"},{\"field\":\"String7\"},{\"field\":\"String8\"},{\"field\":\"String9\"},{\"field\":\"String10\"},{\"field\":\"String11\"},{\"field\":\"String12\"},{\"field\":\"String13\"},{\"field\":\"String14\"},{\"field\":\"String15\"},{\"field\":\"String16\"},{\"field\":\"String17\"},{\"field\":\"String18\"},{\"field\":\"String19\"},{\"field\":\"String20\"},{\"field\":\"String21\"},{\"field\":\"String22\"},{\"field\":\"String23\"},{\"field\":\"String24\"},{\"field\":\"String25\"},{\"field\":\"String26\"},{\"field\":\"String27\"},{\"field\":\"String28\"},{\"field\":\"String29\"},{\"field\":\"String30\"},{\"field\":\"String31\"},{\"field\":\"String32\"},{\"field\":\"String33\"},{\"field\":\"String34\"},{\"field\":\"String35\"},{\"field\":\"String36\"},{\"field\":\"String37\"},{\"field\":\"String38\"},{\"field\":\"String39\"},{\"field\":\"String40\"},{\"field\":\"String41\"},{\"field\":\"String42\"},{\"field\":\"String43\"},{\"field\":\"String44\"},{\"field\":\"String45\"},{\"field\":\"String46\"},{\"field\":\"String47\"},{\"field\":\"String48\"},{\"field\":\"String49\"},{\"field\":\"String50\"},{\"field\":\"String51\"},{\"field\":\"String52\"},{\"field\":\"String53\"},{\"field\":\"String54\"},{\"field\":\"String55\"},{\"field\":\"String56\"},{\"field\":\"String57\"},{\"field\":\"String58\"},{\"field\":\"String59\"},{\"field\":\"String60\"},{\"field\":\"String61\"},{\"field\":\"String62\"},{\"field\":\"String63\"},{\"field\":\"String64\"},{\"field\":\"String65\"},{\"field\":\"String66\"},{\"field\":\"String67\"},{\"field\":\"String68\"},{\"field\":\"String69\"},{\"field\":\"String70\"},{\"field\":\"String71\"},{\"field\":\"String72\"},{\"field\":\"String73\"},{\"field\":\"String74\"},{\"field\":\"String75\"},{\"field\":\"String76\"},{\"field\":\"String77\"},{\"field\":\"String78\"},{\"field\":\"String79\"},{\"field\":\"String80\"},{\"field\":\"String81\"},{\"field\":\"String82\"},{\"field\":\"String83\"},{\"field\":\"String84\"},{\"field\":\"String85\"},{\"field\":\"String86\"},{\"field\":\"String87\"},{\"field\":\"String88\"},{\"field\":\"String89\"},{\"field\":\"String90\"},{\"field\":\"String91\"},{\"field\":\"String92\"},{\"field\":\"String93\"},{\"field\":\"String94\"},{\"field\":\"String95\"},{\"field\":\"String96\"},{\"field\":\"String97\"},{\"field\":\"String98\"},{\"field\":\"String99\"},{\"field\":\"String100\"},{\"field\":\"String101\"},{\"field\":\"String102\"},{\"field\":\"String103\"},{\"field\":\"String104\"},{\"field\":\"String105\"},{\"field\":\"String106\"},{\"field\":\"String107\"},{\"field\":\"String108\"},{\"field\":\"String109\"},{\"field\":\"String110\"},{\"field\":\"String111\"},{\"field\":\"String112\"},{\"field\":\"String113\"},{\"field\":\"String114\"},{\"field\":\"String115\"},{\"field\":\"String116\"},{\"field\":\"String117\"},{\"field\":\"String118\"},{\"field\":\"String119\"},{\"field\":\"String120\"},{\"field\":\"String121\"},{\"field\":\"String122\"},{\"field\":\"String123\"},{\"field\":\"String124\"},{\"field\":\"String125\"},{\"field\":\"String126\"},{\"field\":\"String127\"},{\"field\":\"String128\"},{\"field\":\"String129\"},{\"field\":\"String130\"},{\"field\":\"String131\"},{\"field\":\"String132\"},{\"field\":\"String133\"},{\"field\":\"String134\"},{\"field\":\"String135\"},{\"field\":\"String136\"},{\"field\":\"String137\"},{\"field\":\"String138\"},{\"field\":\"String139\"},{\"field\":\"String140\"},{\"field\":\"String141\"},{\"field\":\"String142\"},{\"field\":\"String143\"},{\"field\":\"String144\"},{\"field\":\"String145\"},{\"field\":\"String146\"},{\"field\":\"String147\"},{\"field\":\"String148\"},{\"field\":\"String149\"},{\"field\":\"String150\"},{\"field\":\"String151\"},{\"field\":\"String152\"},{\"field\":\"String153\"},{\"field\":\"String154\"},{\"field\":\"String155\"},{\"field\":\"String156\"},{\"field\":\"String157\"},{\"field\":\"String158\"},{\"field\":\"String159\"},{\"field\":\"String160\"},{\"field\":\"String161\"},{\"field\":\"String162\"},{\"field\":\"String163\"},{\"field\":\"String164\"},{\"field\":\"String165\"},{\"field\":\"String166\"},{\"field\":\"String167\"},{\"field\":\"String168\"},{\"field\":\"String169\"},{\"field\":\"String170\"},{\"field\":\"String171\"},{\"field\":\"String172\"},{\"field\":\"String173\"},{\"field\":\"String174\"},{\"field\":\"String175\"},{\"field\":\"String176\"},{\"field\":\"String177\"},{\"field\":\"String178\"},{\"field\":\"String179\"},{\"field\":\"String180\"},{\"field\":\"String181\"},{\"field\":\"String182\"},{\"field\":\"String183\"},{\"field\":\"String184\"},{\"field\":\"String185\"},{\"field\":\"String186\"},{\"field\":\"String187\"},{\"field\":\"String188\"},{\"field\":\"String189\"},{\"field\":\"String190\"},{\"field\":\"String191\"},{\"field\":\"String192\"},{\"field\":\"String193\"},{\"field\":\"String194\"},{\"field\":\"String195\"},{\"field\":\"String196\"},{\"field\":\"String197\"},{\"field\":\"String198\"},{\"field\":\"String199\"},{\"field\":\"String200\"},{\"field\":\"String201\"},{\"field\":\"String202\"},{\"field\":\"String203\"},{\"field\":\"String204\"},{\"field\":\"String205\"},{\"field\":\"String206\"},{\"field\":\"String207\"},{\"field\":\"String208\"},{\"field\":\"String209\"},{\"field\":\"String210\"},{\"field\":\"String211\"},{\"field\":\"String212\"},{\"field\":\"String213\"},{\"field\":\"String214\"},{\"field\":\"String215\"},{\"field\":\"String216\"},{\"field\":\"String217\"},{\"field\":\"String218\"},{\"field\":\"String219\"},{\"field\":\"String220\"},{\"field\":\"String221\"},{\"field\":\"String222\"},{\"field\":\"String223\"},{\"field\":\"String224\"},{\"field\":\"String225\"},{\"field\":\"String226\"},{\"field\":\"String227\"},{\"field\":\"String228\"},{\"field\":\"String229\"},{\"field\":\"String230\"},{\"field\":\"String231\"},{\"field\":\"String232\"},{\"field\":\"String233\"},{\"field\":\"String234\"},{\"field\":\"String235\"},{\"field\":\"String236\"},{\"field\":\"String237\"},{\"field\":\"String238\"},{\"field\":\"String239\"},{\"field\":\"String240\"},{\"field\":\"String241\"},{\"field\":\"String242\"},{\"field\":\"String243\"},{\"field\":\"String244\"},{\"field\":\"String245\"},{\"field\":\"String246\"},{\"field\":\"String247\"},{\"field\":\"String248\"},{\"field\":\"String249\"},{\"field\":\"String250\"},{\"field\":\"String251\"},{\"field\":\"String252\"},{\"field\":\"String253\"},{\"field\":\"String254\"},{\"field\":\"String255\"},{\"field\":\"String256\"}]}}\r\n"
						+ "");
		System.out.println("externaljson Value : "+externaljson);
		JSONObject createrawdatajson = new JSONObject();
		JSONObject transformmainjson=new JSONObject();
		JSONObject transformblankjson=new JSONObject();
		JSONArray transformblankjsonarray = new JSONArray();
		JSONArray rawdataarraytransform = new JSONArray();
		JSONObject transformfileblankobj=new JSONObject();
		JSONObject mainjson=new JSONObject();
		JSONObject inputnamejson = null;

		JSONObject messageexternaljson = externaljson.getJSONObject("Message");
		JSONArray exceldataarray = messageexternaljson.getJSONArray("EXCEL_DATA");
		for (int i = 0; i < exceldataarray.length(); i++) {
			inputnamejson = new JSONObject();
			JSONObject fielddata = exceldataarray.getJSONObject(i);
			String fieldvalue = fielddata.getString("field");
			inputnamejson.put("input_name", fieldvalue);
			inputnamejson.put("input_type", "");
rawdataarraytransform.put(inputnamejson);
		}
		transformblankjsonarray.put(transformblankjson);
		createrawdatajson.put("Raw_Data", rawdataarraytransform);
		createrawdatajson.put("Transform", transformblankjsonarray);
		transformmainjson.put("TRANSFORM", createrawdatajson);
		transformmainjson.put("user_name", "Pass your Username");
		transformmainjson.put("project_name", "Pass your Project Name");
transformmainjson.put("Transform_File_Data", transformfileblankobj);
		
		

		System.out.println("Transform Value : "+transformmainjson);
	}

}
