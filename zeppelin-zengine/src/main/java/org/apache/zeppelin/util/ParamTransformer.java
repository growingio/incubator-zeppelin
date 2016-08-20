package org.apache.zeppelin.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by xyz on 16/6/3.
 */
public class ParamTransformer {
  public static void main(String args[]) {
    Map<String, Object> maps = new HashMap<>();
    maps.put("a", "`js new Date()`");
    maps.put("b", "`sh date +%Y%m%d1600`");
    Map<String, Object> newMap = transform(maps);
    System.out.println(newMap.get("a"));
    System.out.println(newMap.get("b"));
  }


  // for parse js
  static ScriptEngine nashornEngine = new ScriptEngineManager().getEngineByName("nashorn");
  static int languagePrefixLength = "`js ".length() - 1;

  public static Map<String, Object> transform(Map<String, Object> rawParams) {
    Map<String, Object> params = new HashMap<>(rawParams);
    for (String param : rawParams.keySet()) {
      Object value = rawParams.get(param);
      if (value != null && value instanceof String && ((String) value).startsWith("`")) {
        String str = (String) value;
        String expressionStr = str.substring(languagePrefixLength, str.length() - 1);
        if (str.startsWith("`js ")) {
          value = evalJs(expressionStr);
        } else {
          value = evalSh(expressionStr);
        }
      }
      params.put(param, value);
    }
    return params;
  }

  protected static Object evalJs(String expressionStr) {
    String jsExpression = "{" + expressionStr + "}";
    Object value;
    try {
      value = nashornEngine.eval(jsExpression);
    } catch (ScriptException e) {
      value = e.getMessage();
    }
    return value;
  }

  protected static Object evalSh(String expressionStr) {
    Object value;
    try {
      ProcessBuilder processBuilder = 
        new ProcessBuilder("bash", "-c", expressionStr).redirectErrorStream(true);
      Process process = processBuilder.start();
//      ArrayList<String> output = new ArrayList<String>();
      StringBuilder output = new StringBuilder();
      BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
      String line;
      while ((line = br.readLine()) != null) {
        output.append(line);
      }

      if (!process.waitFor(1, TimeUnit.MINUTES)) {
        return "time out";
      }

      return output.toString();
    } catch (IOException | InterruptedException e) {
      value = e.getMessage();
    }
    return value;
  }
}

