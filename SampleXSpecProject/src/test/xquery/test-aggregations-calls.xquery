xquery version "1.0";
import module namespace comp = "http://company.org"
  at "../../main/xquery/aggregations.xqm";

let $company := doc('../xml/company.xml')/company
return comp:getManagers($company)