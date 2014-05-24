<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>
    <xsl:output encoding="UTF-8"/>
    <xsl:output indent="yes"/>
    <xsl:template match="/xspec-reports">        
        <html lang="en">
            <head>            
                <title>Summary report</title>
                <link rel="stylesheet" href="http://localhost:4000/plugins/XmlTest/test-report.css" />                    
            </head>
            <body>
                <h1>Summary report</h1>
                <xsl:apply-templates select="report"/>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="report">
        <h2 class="{@result}">
            <xsl:value-of select="@inputFile"/>
            <span class="scenario-totals">
                <xsl:value-of select="@passed" />
                <xsl:text>/</xsl:text>
                <xsl:value-of select="@pending" />
                <xsl:text>/</xsl:text>
                <xsl:value-of select="@failed" />
                <xsl:text>/</xsl:text>
                <xsl:value-of select="@total" />
            </span>
        </h2>
        <table class="xspec">
            <tr>
                <th>Report file</th>
                <th>format</th>                               
            </tr>
            <tr>
                <td>
                    <a href="{@outputHtmlFile}">
                        <xsl:value-of select="@outputHtmlFile"/>
                    </a>                    
                </td>
                <td>html</td>                
            </tr>
            <tr>
                <td>
                    <a href="{@outputXmlFile}">
                        <xsl:value-of select="@outputXmlFile"/>
                    </a>                    
                </td>
                <td>xml</td>                
            </tr>            
        </table>
    </xsl:template>
</xsl:stylesheet>