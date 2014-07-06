xquery version "1.0";
module namespace comp="http://company.org";   

declare function comp:getAvgSalary($company as element()) as element(department)* {
    for $d in $company/departments/descendant-or-self::department
    return 
        <department>
            {$d/name}
            <avg-salary>
            {avg($d/employees/employee/salary)}
            </avg-salary>
        </department>
};

declare function comp:getEmployeeCount($company as element()) as element(department)* {
    for $d in $company/departments/descendant-or-self::department
    return 
        <department>
            {$d/name}
            <employee-count>
            {count($d/employees/employee)}
            </employee-count>
        </department>
};


declare function comp:getManagers($company as element()) as element(department)* {
    for $d in $company/departments/descendant-or-self::department
    return 
        <department>
            {$d/name}
            <manager>
                {$d/manager/firstName}
                {$d/manager/lastName}
                {$d/manager/id}
            </manager>            
        </department>
};

