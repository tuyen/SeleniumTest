<HTML>
	<HEAD>
		<META http-equiv=Content-Type content='text/html; charset=shift_jis'>
		<Title>${testScriptName}</Title>
	</HEAD>
	<BODY>
		<TABLE BORDER=0 BGCOLOR=BLACK CELLPADDING=3 CELLSPACING=1 WIDTH=100%>
			<THEAD>
				<TR>
					<TH BGCOLOR=#66699  colspan=5>
						<FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Test Case Name: ${testScriptName}</B></FONT>
					</TH>                
				</TR>			
				<TR>
					<TH BGCOLOR=#FFCC99 WIDTH=5%>
						<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Index</B></FONT>
					</TH>
					<TH BGCOLOR=#FFCC99 WIDTH=20%>
						<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Action</B></FONT>
					</TH>
					<TH BGCOLOR=#FFCC99 WIDTH=45%>
						<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Object</B></FONT>
					</TH>
					<TH BGCOLOR=#FFCC99 WIDTH=20%>
						<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Parameters</B></FONT>
					</TH>
					<TH BGCOLOR=#FFCC99 WIDTH=10%>
						<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>Result</B></FONT>
					</TH>
				</TR>
			</THEAD>
			<TBODY>
				<#list listTstep as step>
					<#if step.result == 'Passed'>
						<TR BGCOLOR=#FFFFFF>
					<#else>
						<TR BGCOLOR=RED>
					</#if>
						<TD WIDTH=5%>
							<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>${step.rowIndex}</B></FONT>
						</TD>
						<TD WIDTH=20%>
							<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>${step.action}</B></FONT>
						</TD>
						<TD WIDTH=45%>
							<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>${step.object}</B></FONT>
						</TD>
						<TD WIDTH=20%>
							<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>${step.parameters}</B></FONT>
						</TD>
						<TD WIDTH=10%>
							<FONT FACE=VERDANA COLOR=BLACK SIZE=2><B>${step.result}</B></FONT>
						</TD>
					</TR>
					<#if step.result == 'Failed'>						
						<TR BGCOLOR=RED>
							<TD colspan=5>
								<FONT FACE=VERDANA COLOR=WHITE SIZE=2><B>Fail cause: ${step.cause}</B></FONT>
							</TD>
						</TR>
					</#if>
				</#list>	
			</TBODY>
		</TABLE>
	</BODY>
</HTML>