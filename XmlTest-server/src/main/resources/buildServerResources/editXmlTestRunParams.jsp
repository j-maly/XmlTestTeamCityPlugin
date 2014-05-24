<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="admin" tagdir="/WEB-INF/tags/admin" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="bs" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<jsp:useBean id="constants" class="cz.jakubmaly.xmltest.server.XmlTestConstantsBean"/>

<l:settingsGroup title="Test kind">
  <tr>
    <c:set var="onclick">
      $('${constants.projectKey}').disabled = this.checked;
      $('${constants.filesKey}').disabled = !this.checked;
      $('${constants.filesExcludeKey}').disabled = !this.checked;
      $('${constants.filesKey}').focus();
      BS.VisibilityHandlers.updateVisibility('${constants.filesKey}');
      BS.VisibilityHandlers.updateVisibility('${constants.projectKey}');
    </c:set>

    <th>
      <props:radioButtonProperty name="${constants.whatToInspectKey}"
                                 value="${constants.whatToInspectFilesValue}"
                                 id="mod-files"
                                 onclick="${onclick}"
                                 checked="${propertiesBean.properties[constants.whatToInspectKey] == constants.whatToInspectFilesValue}"/>
      <label for="mod-files">XSpec scenarios: </label></th>
    <td><span>
      <props:multilineProperty name="${constants.filesKey}"
                               className="longField"
                               linkTitle="Type path to XSpec scenarios"
                               cols="55" rows="5"
                               expanded="true"
                               disabled="${propertiesBean.properties[constants.whatToInspectKey] != constants.whatToInspectFilesValue}"/>
      <props:multilineProperty name="${constants.filesExcludeKey}"
                               className="longField"
                               linkTitle="Excluded XSpec scenarios"
                               cols="55" rows="5"
                               expanded="false"
                               disabled="${propertiesBean.properties[constants.whatToInspectKey] != constants.whatToInspectFilesValue}"/>
      </span>
      <span class="smallNote">File names relative to the checkout root separated by spaces.<br/>
        Ant-like wildcards are supported.<br/>
        Example: bin*.dll</span>
      <span class="error" id="error_${constants.filesKey}"></span>
      <span class="error" id="error_${constants.filesExcludeKey}"></span>
    </td>
  </tr>

  <tr>
    <c:set var="onclick">
      $('${constants.filesKey}').disabled = this.checked;
      $('${constants.filesExcludeKey}').disabled = this.checked;
      $('${constants.projectKey}').disabled = !this.checked;
      $('${constants.projectKey}').focus();
      BS.VisibilityHandlers.updateVisibility('${constants.filesKey}');
      BS.VisibilityHandlers.updateVisibility('${constants.filesExcludeKey}');
      BS.VisibilityHandlers.updateVisibility('${constants.projectKey}');
    </c:set>
    <th>
      <props:radioButtonProperty name="${constants.whatToInspectKey}"
                                 value="${constants.whatToInspectProjectValue}"
                                 id="mod-project"
                                 onclick="${onclick}"
                                 checked="${propertiesBean.properties[constants.whatToInspectKey] == constants.whatToInspectProjectValue}"/>
      <label for="mod-project">XmlTest project file:</label></th>
    <td>
      <span>
        <props:textProperty name="${constants.projectKey}" className="longField"
                            disabled="${propertiesBean.properties[constants.whatToInspectKey] != constants.whatToInspectProjectValue}"/>
        <bs:vcsTree fieldId="${constants.projectKey}" treeId="${constants.projectKey}"/>
        </span>
      <span class="smallNote">TheXmlTest project file name relative to the checkout root</span>
      <span class="error" id="error_${constants.projectKey}"></span></td>
  </tr>

</l:settingsGroup>