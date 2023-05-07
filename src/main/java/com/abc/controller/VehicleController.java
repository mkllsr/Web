package com.abc.controller;


import com.abc.domian.Vehicle;
import com.abc.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;
    //车辆认证
    @PostMapping
    public Result selectByName(@RequestBody Vehicle vehicle){
        int i = vehicleService.selectByName(vehicle.getName());
        int code=0;
        if(i==1){
            code=Code.GET_OK;
        }
        else {
            code=Code.GET_ERR;
        }
        return new Result(i,code);
    }
//添加车辆信息,zhuce
    @GetMapping
    public Result insertAll() {
        int i = vehicleService.insertAll();
        int code=0;
        if(i==1){
            code=Code.SAVE_OK;
        }
        else {
            code=Code.SAVE_ERR;
        }
        return new Result(i,code);
    }
    //发送消息
    @PutMapping
    public Result sendmessg(@RequestBody Vehicle vehicle) {
        int i = vehicleService.sendmessg(vehicle.getName(),vehicle.getPk1());
        int code=0;
        if(i==1){
            code=Code.UPDATE_OK;
        }
        else {
            code=Code.UPDATE_ERR;
        }
        return new Result(i,code);
    }

}
