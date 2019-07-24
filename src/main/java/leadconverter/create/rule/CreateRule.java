package leadconverter.create.rule;

import java.util.ResourceBundle;

import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

public class CreateRule {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String user_name=ResourceBundle.getBundle("config").getString("add_rule_engine_UserName");
		String add_rule_api=ResourceBundle.getBundle("config").getString("add_rule_api");
		try {
		String project_name="June24F06";
		String rule_engine_name=project_name+"_RE";
		String rule_name="rule";
		
		JSONObject  add_rule_main_json_obj=null;
		JSONArray   data_json_arr=new JSONArray();
		JSONObject  data_json_obj=null;
		
		JSONArray   sf_data_json_arr=new JSONArray();
		JSONObject  sf_data_json_obj=null;
		
		JSONArray   output_data_json_arr=new JSONArray();
		JSONObject  output_data_json_obj=null;
		
		sf_data_json_obj=new JSONObject();
		sf_data_json_obj.put("field", "SessionCount");
		sf_data_json_obj.put("type", "Integer");
		sf_data_json_obj.put("Category", "<=");
		sf_data_json_obj.put("symbol_category", "<=");
		sf_data_json_obj.put("value", "100");
		sf_data_json_arr.put("sf_data_json_obj");
		
		
		output_data_json_obj=new JSONObject();
		output_data_json_obj.put("field", "Output");
		output_data_json_obj.put("value", "Connect");
		output_data_json_arr.put(output_data_json_obj);
		
		
		data_json_obj=new JSONObject();
		data_json_obj.put("rulename", rule_name);
		data_json_obj.put("SFdata", sf_data_json_arr);
		data_json_obj.put("Outputdata", output_data_json_arr);
		data_json_arr.put(data_json_obj);
		
		add_rule_main_json_obj=new JSONObject();
		add_rule_main_json_obj.put("user_name", user_name);
		add_rule_main_json_obj.put("project_name", project_name);
		add_rule_main_json_obj.put("ruleengine_name", rule_engine_name);
		add_rule_main_json_obj.put("data", data_json_arr);
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
