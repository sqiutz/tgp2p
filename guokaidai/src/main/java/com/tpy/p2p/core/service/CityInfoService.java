package com.tpy.p2p.core.service;

import java.util.List;

import javax.annotation.Resource;

import com.tpy.p2p.chesdai.entity.City;
import org.springframework.stereotype.Service;

import com.tpy.base.spring.orm.hibernate.HibernateSupportTemplate;
import com.tpy.p2p.chesdai.entity.Province;


/**
 * 省 市查询通用service
 * @author hsq
 *
 */
@Service
public class CityInfoService {
    /** dao*/
    @Resource
    private HibernateSupportTemplate dao;

    /**
     * 查询所有的省
     * @return  所有的省的集合
     */
    public List<Province> queryAllProvince(){
        List<Province> provinceList= dao.find("From Province");
        return provinceList;  
    }
    
    /**
    * <p>Title: queryCityByProvince</p>
    * <p>Description: 通过省找到市</p>
    * @param provinceId  省编号
    * @return list
    */
    public List<City> queryCityByProvince(long provinceId){
        StringBuffer sb=new StringBuffer("SELECT * from city where province_id=").append(provinceId);
        List<City> cityList= dao.findBySql(sb.toString(),City.class);
        return cityList;  
    }
    
    
}
