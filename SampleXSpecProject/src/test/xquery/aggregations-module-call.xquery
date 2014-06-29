xquery version "1.0";
 
import module namespace comp="http://company.org" at "../../main/xquery/aggregations.xqm";

<root>
    <company>
        { comp:getAvgSalary(/company) }
    </company>
    <company>
        { comp:getEmployeeCount(/company) }
    </company>
    <company>
        { comp:getManagers(/company) }
    </company>
</root>