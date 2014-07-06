<?xml version="1.0" encoding="UTF-8"?>
<!-- ===================================================================== -->
<!--  File:       saxon-xquery-harness.xproc                               -->
<!--  Author:     Florent Georges                                          -->
<!--  Date:       2011-08-30                                               -->
<!--  URI:        http://xspec.googlecode.com/                             -->
<!--  Tags:                                                                -->
<!--    Copyright (c) 2011 Florent Georges (see end of file.)              -->
<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->


<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
            xmlns:c="http://www.w3.org/ns/xproc-step"
            xmlns:t="http://www.jenitennison.com/xslt/xspec"
            xmlns:pkg="http://expath.org/ns/pkg"
            xmlns:cx="http://xmlcalabash.com/ns/extensions"
            pkg:import-uri="http://www.jenitennison.com/xslt/xspec/saxon/harness/xquery.xproc"
            name="saxon-xquery-harness"
            type="t:saxon-xquery-harness"
            version="1.0">
	
   <p:documentation>
      <p>This pipeline executes an XSpec test suite with the Saxon embedded in Calabash.</p>
      <p><b>Primary input:</b> A XSpec test suite document.</p>
      <p><b>Primary output:</b> A formatted HTML XSpec report.</p>
      <p>The dir where you unzipped the XSpec archive on your filesystem is passed
        in the option 'xspec-home'.</p>
   </p:documentation>
   
   <p:input port="source"  primary="true" />
   <p:input port="parameters" kind="parameter" primary="true" />
   <p:output port="result" primary="true" />
   
   <p:option name="xmlResult" required="true" />
   <p:option name="pathToIndexHtml" required="false" select="'index.html" />
     
   <p:serialization port="result" indent="true" method="html" />

   <p:import href="harness-lib.xpl"/>
   <p:import href="http://xmlcalabash.com/extension/steps/library-1.0.xpl"/>
   
   <t:parameters name="params"/>   
   <p:group>      
      <p:variable name="xspec-home" select="
          /c:param-set/c:param[@name eq 'xspec-home']/@value">
         <p:pipe step="params" port="parameters"/>
      </p:variable>
      <p:variable name="utils-library-at" select="
          /c:param-set/c:param[@name eq 'utils-library-at']/@value">
         <p:pipe step="params" port="parameters"/>
      </p:variable>


      <!-- either no at location hint, or resolved from xspec-home if packaging not supported -->
      <p:variable name="utils-lib" select="
          if ( $utils-library-at ) then
            $utils-library-at
          else if ( $xspec-home ) then
            resolve-uri('compiler/generate-query-utils.xql', $xspec-home)
          else
            ''"/>

      <!-- compile the suite into a query -->
      <t:compile-xquery name="compile">
        <p:with-param name="utils-library-at" select="$utils-lib"/>         
      </t:compile-xquery>
      
      <!-- escape the query as text -->
      <p:escape-markup name="escape"/>

      <p:store href="/c:/q.xquery">
         <p:input port="source">
            <p:pipe port="result" step="compile"/>
         </p:input>
      </p:store>

      <!-- run it on saxon -->
      <p:xquery name="run">
         <p:input port="source">
            <p:empty/>
         </p:input>
         <p:input port="query">
            <p:pipe step="escape" port="result"/>
         </p:input>
         <p:input port="parameters">
            <p:empty/>
         </p:input>
      </p:xquery>

      <!-- format the report -->
      <!--<t:format-report>
         <p:with-option name="pathToIndexHtml" select="$pathToIndexHtml"/>
         <p:with-option name="xspec-home" select="$xspec-home" />
      </t:format-report>-->
      
      <p:store name="store-raw-xml" method="xml" encoding="utf-8" indent="true">
         <p:input port="source">
            <p:pipe port="result" step="run"/>
         </p:input>        
         <p:with-option name="href" select="$xmlResult"/>        
      </p:store>
      
      <p:xslt name="format-report">
         <p:input port="stylesheet">
            <p:document href="reporter/format-xspec-report.xsl"/>
         </p:input>
         <p:input port="source">
            <p:pipe port="result" step="run"/>
         </p:input>  
         <p:with-param name="pathToIndexHtml" select="$pathToIndexHtml"/>        
      </p:xslt>
   </p:group>

</p:declare-step>


<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
<!-- DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS COMMENT.             -->
<!--                                                                       -->
<!-- Copyright (c) 2011 Florent Georges                                    -->
<!--                                                                       -->
<!-- The contents of this file are subject to the MIT License (see the URI -->
<!-- http://www.opensource.org/licenses/mit-license.php for details).      -->
<!--                                                                       -->
<!-- Permission is hereby granted, free of charge, to any person obtaining -->
<!-- a copy of this software and associated documentation files (the       -->
<!-- "Software"), to deal in the Software without restriction, including   -->
<!-- without limitation the rights to use, copy, modify, merge, publish,   -->
<!-- distribute, sublicense, and/or sell copies of the Software, and to    -->
<!-- permit persons to whom the Software is furnished to do so, subject to -->
<!-- the following conditions:                                             -->
<!--                                                                       -->
<!-- The above copyright notice and this permission notice shall be        -->
<!-- included in all copies or substantial portions of the Software.       -->
<!--                                                                       -->
<!-- THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,       -->
<!-- EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF    -->
<!-- MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.-->
<!-- IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY  -->
<!-- CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  -->
<!-- TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE     -->
<!-- SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.                -->
<!-- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -->
