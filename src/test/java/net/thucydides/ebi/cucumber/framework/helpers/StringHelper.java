package net.thucydides.ebi.cucumber.framework.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StringHelper {
  
  public static boolean equalLists(List<String> listOne, List<String> listTwo){     
    if (listOne == null && listTwo == null){
        return true;
    }

    if((listOne == null && listTwo != null) 
      || listOne != null && listTwo == null
      || listOne.size() != listTwo.size()){
        return false;
    }
    listOne = new ArrayList<String>(listOne); 
    listTwo = new ArrayList<String>(listTwo);   

    Collections.sort(listOne);
    Collections.sort(listTwo);      
    return listOne.equals(listTwo);
}

  public static String getRandomValue() throws Exception {
    try{
      return UUID.randomUUID().toString().replaceAll("-", "");
    }catch (Exception e){
      throw new Exception(e.getMessage());
    }
  }

  public static int getTextOccunace(String str, String findStr) throws Exception {
    int count = 0;
    try{
      int lastIndex = 0;
      while (lastIndex != -1) {
        lastIndex = str.indexOf(findStr, lastIndex);
        if (lastIndex != -1) {
          count++;
          lastIndex += findStr.length();
        }
      }
      //System.out.println(count);
    }catch (Exception e){
      throw new Exception(e.getMessage());
    }
    return count;
  }

}
