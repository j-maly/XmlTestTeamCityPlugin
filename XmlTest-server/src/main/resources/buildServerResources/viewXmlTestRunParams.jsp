<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="constants" class="cz.jakubmaly.xmltest.common.XmlTestConstants"/>

<c:choose>
  <c:when test="${propertiesBean.properties[constants.whatToInspectKey] == constants.whatToInspectProjectValue}">
    <div class="parameter">
      XmlTest project file: <strong><props:displayValue name="${constants.projectKey}" emptyValue="not specified"/></strong>
    </div>
  </c:when>
  <c:otherwise>
    <div class="parameter">
      Assemblies to inspect: <strong><props:displayValue name="${constants.filesKey}" emptyValue="not specified"/></strong>
    </div>
    <div class="parameter">
      Assemblies to exclude: <strong><props:displayValue name="${constants.filesExcludeKey}" emptyValue="not specified"/></strong>
    </div>
  </c:otherwise>
</c:choose>