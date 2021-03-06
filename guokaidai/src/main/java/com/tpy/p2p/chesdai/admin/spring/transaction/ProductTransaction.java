package com.tpy.p2p.chesdai.admin.spring.transaction;

import java.text.ParseException;

import javax.annotation.Resource;

import com.tpy.base.spring.exception.ResponseExceptionFactory;
import com.tpy.base.spring.orm.hibernate.impl.HibernateSupport;
import com.tpy.base.view.AjaxResponseView;
import com.tpy.p2p.chesdai.constant.enums.ENUM_MONEY_EXCHANGE_STATE;
import com.tpy.p2p.chesdai.constant.enums.ENUM_PRODUCT_PAY_STATE;
import com.tpy.p2p.chesdai.entity.*;
import com.tpy.p2p.chesdai.exception.ProductPayException;
import com.tpy.p2p.chesdai.model.ProductPayInfo;
import com.tpy.p2p.pay.entity.BidInfo;
import org.springframework.stereotype.Service;

import com.tpy.base.spring.exception.ResponseException;
import com.tpy.base.spring.exception.ResponseExceptionFactory;
import com.tpy.base.spring.orm.hibernate.impl.HibernateSupport;
import com.tpy.base.view.AjaxResponseView;
import com.tpy.p2p.chesdai.constant.ErrorNumber;
import com.tpy.p2p.chesdai.constant.enums.ENUM_MONEY_EXCHANGE_STATE;
import com.tpy.p2p.chesdai.constant.enums.ENUM_PRODUCT_PAY_STATE;
import com.tpy.p2p.chesdai.entity.Product;
import com.tpy.p2p.chesdai.entity.ProductPayRecord;
import com.tpy.p2p.chesdai.entity.Userbasicsinfo;
import com.tpy.p2p.chesdai.exception.ProductPayException;
import com.tpy.p2p.chesdai.model.ProductPayInfo;
import com.tpy.p2p.pay.entity.BidInfo;

/**
 * 产品长事务
 * @author ldd
 *
 */
@Service
public class ProductTransaction {

    /**
     * ResponseExceptionFactory
     */
    @Resource
    ResponseExceptionFactory factory;
    
    /**
     * HibernateSupport
     */
    @Resource
    HibernateSupport dao;
    
    /**
     * ProductPayRecordTransaction
     */
    @Resource
    ProductPayRecordTransaction tranProductPayRecord;
    
    /**
     * CreditorPayRecordTransaction
     */
    @Resource
    CreditorPayRecordTransaction tranCreditorPayRecord;
    
    /**
     * CreditorLinkTransaction
     */
    @Resource
    CreditorLinkTransaction tranCreditorLink;
    
    /**
     * AjaxResponseView
     */
    @Resource
    private AjaxResponseView view;
    
    
    /**
     * 添加产品认购信息及其相关信息
     * @param product   产品
     * @param user  投资人
     * @param stateExChange 投资方式
     * @param info  产品认购信息
     * @param bid   环讯信息
     * @throws ResponseException    响应式异常
     * @throws java.text.ParseException   时间格式化异常
     */
    public void payProduct(Product product,
            Userbasicsinfo user, ENUM_MONEY_EXCHANGE_STATE stateExChange,
            ProductPayInfo info,BidInfo bid) throws ResponseException, ParseException{
        
        if (!product.isPayAble(info.getMoney())) {// 判断是否允许购买
            throw new ProductPayException("您所购买的产品不足 " + info.getMoney() + " 元，请减少您的购买金额，或选购其他产品！",view);
        }
        
        double sum = 0d;
        ENUM_PRODUCT_PAY_STATE statePay = ENUM_PRODUCT_PAY_STATE.EXECUTING;
        
        for (double val : info.getMoneys()) {
            sum += val;
        }

        //判断是否为待分配购买
        if (sum < info.getMoney() * product.getProductType().getDayDuring()) {
            statePay = ENUM_PRODUCT_PAY_STATE.WAITING;
        } else if (sum > info.getMoney() * product.getProductType().getDayDuring()) {
            throw factory.bornAjax(ErrorNumber.NO_4);
        }
        
        // 添加产品认购记录
        ProductPayRecord record = tranProductPayRecord.addProductRecord(
                product, statePay,info.getAlreadyDateIds(), stateExChange, info.getTimeStart(),info.getTimeEnd(), info.getMoney(), user,new Userbasicsinfo(info.getUserId2()),bid);

        // 添加债权认购记录
        tranCreditorPayRecord.addRecord(record.getId(), user, info.getCreditors(),
                info.getMoneys(), info.getTimeStarts(), info.getTimeEnds());

        // 修改creditorLink余额,并释放锁定
        tranCreditorLink.updateCreditorLink(info.getCreditorLinkIds(), info.getMoneys(),user);

        //增加产品投资金额
        product.pay(info.getMoney());
        dao.update(product);

        
    }
    
}
