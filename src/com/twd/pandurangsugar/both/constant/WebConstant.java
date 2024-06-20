package com.twd.pandurangsugar.both.constant;

import org.json.JSONArray;

import com.twd.convertismtouni.DemoConvert2;

public class WebConstant {
public static String projectName="Pandurang Sugar";

public static JSONArray SplitNplotList(String nplot_no) {

	JSONArray nplot_noArr=new JSONArray();
	int arrSize=900;
	int autoVar=1;
	String newnplot_no=null;
	boolean flag=false;
	String[] nplot = nplot_no.split(",");
	int len=nplot.length;
	if(len>arrSize) //2000>900
		flag=true;
	for(int i=0;i<len;i++) //2000
	{
		if(autoVar==arrSize) // +1 ==900
		{
			nplot_noArr.put(newnplot_no);
			newnplot_no=null;
			autoVar=1;
		}
		if(newnplot_no==null)
			newnplot_no=nplot[i];
		else
			newnplot_no+=","+nplot[i];
		autoVar++;
			
	}
	if(flag)
		nplot_noArr.put(newnplot_no);
	else 
		nplot_noArr.put(nplot_no);
	return nplot_noArr;



}
public static void main(String[] args) {
	System.out.println(DemoConvert2.ism_to_uni("ÝÖ»ÖÖÓ›êü †õÖµÖ ÃÖ×ŸÖ¿Ö"));
}
}
