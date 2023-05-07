package com.abc.service;

import com.abc.domian.Vehicle;

import java.util.List;

public interface VehicleService {
    //车辆注册
    int insertAll();
    //车辆查询
    int selectByName(String name);

    int sendmessg(String id,String mesag);


}
