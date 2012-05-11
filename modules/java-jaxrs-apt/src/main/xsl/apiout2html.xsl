<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/api">
    <html>
      <head>
        <title>Api documentation</title>
        <style type="text/css">
          h2 {background-color:#6464FF   }
          h3 {background-color:#42EE42   }
          th {font-weight:bold; font-size:120% }
          em {font-style:italic; font-size:110%; background-color:LightYellow}
        </style>
      </head>
      <body>
        <xsl:apply-templates>
            <xsl:sort select="class/@path"/>
        </xsl:apply-templates>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="class">
    <h2>/<xsl:value-of select="@path"/>
      <xsl:if test="@shortDesc">
      : <xsl:value-of select="@shortDesc"/>
      </xsl:if>
      <xsl:if test="@basePath">
        ( Base: <xsl:value-of select="@basePath"/> )
      </xsl:if>
    </h2>
    Class: <xsl:value-of select="@name"/><br/>
    <em><xsl:value-of select="@description"/></em>
    <br/>
    <xsl:if test="method">
      Methods:<br/>
      <xsl:apply-templates/>
    </xsl:if>
    <p/>
  </xsl:template>

  <xsl:template match="method">
    <h3><xsl:value-of select="@method"/><xsl:text xml:space="preserve"> </xsl:text><xsl:value-of select="../@path"/>/<xsl:value-of select="@path"/></h3>
    <em><xsl:value-of select="@description"/></em>
    <br/>
    <xsl:choose>
    <xsl:when test="param">
    Parameters:
    <table>
        <tr><th>Name</th><th>P.Type</th><th>Description</th><th>Required</th><th>Type</th><th>Allowed values</th><th>Default value</th></tr>
      <xsl:apply-templates select="param"/>
    </table>
    </xsl:when>
      <xsl:otherwise>
        This method has no parameters
      </xsl:otherwise>
    </xsl:choose>
    <br/>
    Return type: <xsl:value-of select="@returnType"/>
    <p/>
    <xsl:if test="error">
      Error codes:<br/>
      <table>
          <tr>
            <th>Code</th><th>Reason</th>
          </tr>
        <xsl:apply-templates select="error"/>
      </table>
    </xsl:if>
  </xsl:template>

  <xsl:template match="param">
    <tr>
      <td><xsl:value-of select="@name"/></td>
      <td><xsl:value-of select="@paramType"/></td>
      <td><xsl:value-of select="@description"/></td>
      <td><xsl:value-of select="@required"/></td>
      <td><xsl:value-of select="@type"/></td>
      <td><xsl:value-of select="@allowableValues"/></td>
      <td><xsl:value-of select="@defaultValue"/></td>
    </tr>
  </xsl:template>

  <xsl:template match="error">
    <tr>
        <td><xsl:value-of select="@code"/></td>
        <td><xsl:value-of select="@reason"/></td>
    </tr>
  </xsl:template>

</xsl:stylesheet>