<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
    xmlns:c="http://www.w3.org/ns/xproc-step"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:t="http://www.jenitennison.com/xslt/xspec" 
    version="1.0"
    name="xspec" type="t:xspec">
    
    <p:documentation>
        <p>This pipeline executes an XSpec test.</p>
        <p><b>Primary input:</b> A XSpec test document.</p>
        <p><b>Primary output:</b> A formatted HTML XSpec report.</p>
    </p:documentation>
    
    <p:input port="source"  primary="true" />
    <p:input port="parameters" kind="parameter" primary="true" />
    <p:output port="result" primary="true" />
    
    <p:option name="xmlResult" required="true" />
    <p:option name="pathToIndexHtml" required="false" select="'index.html" />
    
    <p:serialization port="result" encoding="utf-8" method="html" />
    
    <p:xslt name="create-test-stylesheet">
        <p:input port="stylesheet">
            <p:document href="compiler/generate-xspec-tests.xsl"/>
        </p:input>
        <p:input port="source">
            <p:pipe port="source" step="xspec"/>
        </p:input>
    </p:xslt>
    
    <p:xslt name="run-tests" template-name="t:main">
        <p:input port="source">
            <p:pipe step="xspec" port="source"/>
        </p:input>  
        <p:input port="stylesheet">
            <p:pipe step="create-test-stylesheet" port="result"/>
        </p:input>
    </p:xslt>
    
    <p:store name="store-raw-xml" method="xml" encoding="utf-8" indent="true">
        <p:input port="source">
            <p:pipe port="result" step="run-tests"/>
        </p:input>        
        <p:with-option name="href" select="$xmlResult"/>        
    </p:store>
    
    <p:xslt name="format-report">
        <p:input port="stylesheet">
            <p:document href="reporter/format-xspec-report.xsl"/>
        </p:input>
        <p:input port="source">
            <p:pipe port="result" step="run-tests"/>
        </p:input>  
        <p:with-param name="pathToIndexHtml" select="$pathToIndexHtml"/>        
    </p:xslt>
</p:declare-step>