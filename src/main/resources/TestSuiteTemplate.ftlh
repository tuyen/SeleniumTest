<HTML>
   <HEAD>
      <META http-equiv=Content-Type content='text/html; charset=utf-8'>
   </HEAD>
   <BODY>      
      <TABLE BORDER=0 BGCOLOR=BLACK CELLPADDING=3 CELLSPACING=1 WIDTH=100%>
		  <TR>
			 <TD COLSPAN=6 BGCOLOR=#66699><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Test script Name</B></FONT></TD>
			 <TD BGCOLOR=#66699 WIDTH=27%><FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Status</B></FONT></TD>
		  </TR>
		  <#list listTScript as item>
			  <#if item.result == 'Passed'>
			  	<TR BGCOLOR=WHITE>
			  <#else>
			  	<TR BGCOLOR=RED>
			  </#if>
				 <TD COLSPAN=6 ><FONT FACE=VERDANA SIZE=2><B>${item.testScriptName}</B></FONT></TD>
				 <TD WIDTH=27%><FONT FACE=VERDANA SIZE=2><B>${item.result}</B></FONT></TD>
			  </TR>
		  </#list>
	  </TABLE>
	</BODY>
</HTML>