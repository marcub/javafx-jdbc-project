package model.services;

import model.entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public List<Department> findAll() {
        List<Department> departmentList =  new ArrayList<>();
        departmentList.add(new Department(1, "Car"));
        departmentList.add(new Department(2, "Bike"));

        return departmentList;
    }
}
