package com.tpy.p2p.chesdai.util;

/**   
 * Filename:    SQLUtils.java   
 * Company:     太平洋金融  
 * @version:    1.0   
 * @since:  JDK 1.7.0_25  
 * Create at:   2014年3月27日 上午11:09:31   
 * Description:  
 *   
 * Modification History:   
 * 时间    			作者   	   	版本     		描述 
 * ----------------------------------------------------------------- 
 * 2014年3月27日 	LiNing      1.0     	1.0Version   
 */

/**
 * <p>
 * Title:SQLUtils
 * </p>
 * <p>
 * Description: sql工具类
 * </p>
 * <p>
 * Company: 太平洋金融
 * </p>
 * 
 * @author LiNing
 *         <p>
 *         date 2014年3月27日
 *         </p>
 */
public class SQLUtils {

    /** ALL_MONEY 统计全部代收本息总额 */
    public static final String ALL_MONEY = "SELECT SUM(money)+SUM(preRepayMoney) FROM repaymentrecord WHERE loanSign_id IN (SELECT loanSign_id FROM loanrecord LEFT JOIN loansign ON loansign.id=loanrecord.loanSign_id WHERE loanrecord.userbasicinfo_id=? AND loansign.loanstate<4 GROUP BY loanSign_id) AND (repayState = 1 OR repayState = 3)";
    /** NEXT_MONTH_MONEY 统计未来一个月 代收本息总额 */
    public static final String NEXT_MONTH_MONEY = "SELECT SUM(money)+SUM(preRepayMoney) FROM repaymentrecord WHERE loanSign_id IN (SELECT loanSign_id FROM loanrecord LEFT JOIN loansign ON loansign.id=loanrecord.loanSign_id WHERE loanrecord.userbasicinfo_id=? AND loansign.loanstate<4 GROUP BY loanSign_id) AND (repayState = 1 OR repayState = 3) AND preRepayDate BETWEEN '"
            + DateUtil.format("yyyy-MM-dd")
            + "' AND '"
            + DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), 1)
            + "'";

    /** NEXT_Three_MONTH_MONEY 统计未来三个月 代收本息总额 */
    public static final String NEXT_Three_MONTH_MONEY = "SELECT SUM(money)+SUM(preRepayMoney) FROM repaymentrecord WHERE loanSign_id IN (SELECT loanSign_id FROM loanrecord LEFT JOIN loansign ON loansign.id=loanrecord.loanSign_id WHERE loanrecord.userbasicinfo_id=? AND loansign.loanstate<4 GROUP BY loanSign_id) AND (repayState = 1 OR repayState = 3) AND preRepayDate BETWEEN '"
            + DateUtil.format("yyyy-MM-dd")
            + "' AND '"
            + DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), 3)
            + "'";

    /** NEXT_Three_MONTH_MONEY 统计未来六个月 代收本息总额 */
    public static final String NEXT_SIX_MONTH_MONEY = "SELECT SUM(money)+SUM(preRepayMoney) FROM repaymentrecord WHERE loanSign_id IN (SELECT loanSign_id FROM loanrecord LEFT JOIN loansign ON loansign.id=loanrecord.loanSign_id WHERE loanrecord.userbasicinfo_id=? AND loansign.loanstate<4 GROUP BY loanSign_id) AND (repayState = 1 OR repayState = 3) AND preRepayDate BETWEEN '"
            + DateUtil.format("yyyy-MM-dd")
            + "' AND '"
            + DateUtil.getSpecifiedMonthAfter(DateUtil.format("yyyy-MM-dd"), 6)
            + "'";

    
    /** MONEY_RECORD 回款计划明细*/
    public static final String MONEY_RECORD = "SELECT loanrecord.loanSign_id,SUM(loanrecord.tenderMoney),"
            + "SUM(loanrecord.tenderMoney)*loansign.rate,loansign.rate,loansign.`month`,"
            + "loansignbasics.loanNumber,loanrecord.isPrivilege,loansign.mfeeratio,loansign.vipMfeetop,"
            + "loansign.pmfeeratio,loansign.vipPmfeetop,loansign.issueLoan,loansign.loanType, "
            + "(SELECT repaymentrecord.preRepayDate FROM repaymentrecord WHERE  repaymentrecord.loanSign_id=loansign.id ORDER BY "
            + "repaymentrecord.id DESC LIMIT 0,1),loansign.useDay,loanrecord.tenderTime FROM loanrecord"
            + " INNER JOIN loansign ON loansign.id=loanrecord.loanSign_id INNER JOIN loansignbasics ON "
            + "loansign.id=loansignbasics.id WHERE loanrecord.loanSign_id IN (SELECT id FROM loansign WHERE "
            + "loanstate = 3) AND loanrecord.userbasicinfo_id = ? "
            + " AND (SELECT repaymentrecord.preRepayDate FROM repaymentrecord WHERE  repaymentrecord.loanSign_id=loansign.id ORDER BY repaymentrecord.id DESC LIMIT 0,1) BETWEEN ? AND ?"
            + " GROUP BY loanrecord.loanSign_id,loanrecord.isPrivilege  HAVING SUM(loanrecord.tenderMoney) BETWEEN ? AND ?";
    
    /** MONEY_RECORD_COUNT 回款计划明细数据条数统计*/
    public static final String MONEY_RECORD_COUNT="select SUM(num) from ( SELECT COUNT(DISTINCT loanrecord.loanSign_id) as 'num' FROM loanrecord INNER JOIN "
            + "loansign ON loansign.id=loanrecord.loanSign_id INNER JOIN loansignbasics ON "
            + "loansign.id=loansignbasics.id WHERE loanrecord.loanSign_id IN (SELECT id FROM "
            + "loansign WHERE loanstate = 3) AND loanrecord.userbasicinfo_id = ? "
            + "AND (SELECT repaymentrecord.preRepayDate FROM repaymentrecord WHERE "
            + "repaymentrecord.loanSign_id=loansign.id ORDER BY repaymentrecord.id DESC LIMIT 0,1)"
            + " BETWEEN ? AND ? GROUP BY loanrecord.loanSign_id,loanrecord.isPrivilege "
            + "HAVING SUM(loanrecord.tenderMoney) BETWEEN ? AND ? ) as a";
    
    /** INVESTMENT_COUNT 获取当前登录人的投资总笔数*/
    public static final String INVESTMENT_COUNT="SELECT COUNT(loanrecord.id) FROM loanrecord WHERE userbasicinfo_id=?";

    
    /** VIP_COUNT 统计特权会员数量*/
    public static final String VIP_COUNT="SELECT COUNT(users) FROM (SELECT user_id AS users FROM vipinfo GROUP BY user_id HAVING MAX(endtime)>NOW()) AS b";

    /** USER_COUNT 统计所有会员数量*/
    public static final String USER_COUNT="SELECT COUNT(id) FROM userbasicsinfo"; 
    
    
}
