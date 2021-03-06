package com.tpy.p2p.chesdai.spring.aspect;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.tpy.base.spring.exception.ResponseException;
import com.tpy.base.spring.exception.ResponseExceptionFactory;
import com.tpy.base.spring.orm.hibernate.impl.HibernateSupport;
import com.tpy.base.view.AjaxResponseView;
import com.tpy.p2p.chesdai.constant.ErrorNumber;
import com.tpy.p2p.chesdai.entity.Product;
import com.tpy.p2p.chesdai.exception.ProductPayException;
import com.tpy.p2p.chesdai.spring.annotation.ProductPayVerification;

/**
 * 日志记录
 * 
 * @author frank
 * 
 */
@Aspect
@Component
public class ProductPayAspect {

    /**
     * HibernateSupport
     */
    @Resource
    HibernateSupport dao;
    
    /**
     * ResponseExceptionFactory
     */
    @Resource
    ResponseExceptionFactory factory;
    
    /**
     * AjaxResponseView
     */
    @Resource
    AjaxResponseView view;
    
    /**
     * 执行之前
     * @param jp    切入点
     * @param p 产品
     * @throws ResponseException 
     */
    @Before("within(com.jubaopen.p2p.chesudai..*) && @annotation(p)")
    public void executeBefore(JoinPoint jp, ProductPayVerification p) throws ResponseException{
        
        
        
        Object[] objs = jp.getArgs();
        
        Product product = dao.get(Product.class,((Product)objs[0]).getId());
        double money = (Double)objs[1];
        
        
        if(money%1000!=0){//购买金额必须为1000的整数倍
            throw factory.bornAjax(ErrorNumber.NO_5);
        }
        
        if (money < product.getInvestOnlineMin()) {// 判断是否大于最小投资起点
            
            throw new ProductPayException("您所购买的金额小于投资起点 " + product.getInvestOnlineMin() + " 元！",view);
        }

        if (!product.isPayAble(money)) {// 判断是否允许购买
            throw new ProductPayException("您所购买的产品不足 " + money + " 元，请减少您的购买金额，或选购其他产品！",view);
        }
        
        
    }

}
