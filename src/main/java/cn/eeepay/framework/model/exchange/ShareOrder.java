package cn.eeepay.framework.model.exchange;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/17/017.
 * 分润
 * 对应表 rdmp_share_detail
 */
public class ShareOrder {

    private  long id;//

    private  String orderNo;//关联订单号

    private  String merNo;//贡献人商户号

    private  String merCapa;//贡献人商户号分润时身份

    private  String shareMerNo;//获得分润商户号

    private  String shareMerCapa;//获得分润时 商户身份

    private  String shareGrade;//分润级别

    private  String shareRate;//分润比例

    private  String shareType;//分润类型 A 激活  D 报单

    private  BigDecimal amount;//原始金额

    private  BigDecimal totalShareAmount;//当前订单的总分润金额

    private  BigDecimal shareAmount;//分润金额

    private  BigDecimal shareFee;//成本

    private  String shareStatus;//分润状态

    private  String checkStatus;//审核状态

    private Date checkTime;//审核时间

    private  String checkOper;//审核人

    private  String accStatus;//入账 状态 0 未入账 1 已入账

    private Date accTime;//入账时间
    private Date accTimeBegin;
    private Date accTimeEnd;

    private  String remark;//备注信息

    private Date createTime;//创建时间
    private Date createTimeBegin;
    private Date createTimeEnd;


    private String orderStatus;//订单状态

    private String mobileUsername;//贡献人手机号
    private String accountName;//贡献人真实姓名

    private String shareMobile;//收益人手机号
    private String shareName;//收益人姓名
    private String shareMerName;//收益人用户名

    private String oemNo;//收益人组织
    private String oemName;//收益人组织

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getMerCapa() {
        return merCapa;
    }

    public void setMerCapa(String merCapa) {
        this.merCapa = merCapa;
    }

    public String getShareMerNo() {
        return shareMerNo;
    }

    public void setShareMerNo(String shareMerNo) {
        this.shareMerNo = shareMerNo;
    }

    public String getShareMerCapa() {
        return shareMerCapa;
    }

    public void setShareMerCapa(String shareMerCapa) {
        this.shareMerCapa = shareMerCapa;
    }

    public String getShareGrade() {
        return shareGrade;
    }

    public void setShareGrade(String shareGrade) {
        this.shareGrade = shareGrade;
    }

    public String getShareRate() {
        return shareRate;
    }

    public void setShareRate(String shareRate) {
        this.shareRate = shareRate;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTotalShareAmount() {
        return totalShareAmount;
    }

    public void setTotalShareAmount(BigDecimal totalShareAmount) {
        this.totalShareAmount = totalShareAmount;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }

    public BigDecimal getShareFee() {
        return shareFee;
    }

    public void setShareFee(BigDecimal shareFee) {
        this.shareFee = shareFee;
    }

    public String getShareStatus() {
        return shareStatus;
    }

    public void setShareStatus(String shareStatus) {
        this.shareStatus = shareStatus;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckOper() {
        return checkOper;
    }

    public void setCheckOper(String checkOper) {
        this.checkOper = checkOper;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public Date getAccTime() {
        return accTime;
    }

    public void setAccTime(Date accTime) {
        this.accTime = accTime;
    }

    public Date getAccTimeBegin() {
        return accTimeBegin;
    }

    public void setAccTimeBegin(Date accTimeBegin) {
        this.accTimeBegin = accTimeBegin;
    }

    public Date getAccTimeEnd() {
        return accTimeEnd;
    }

    public void setAccTimeEnd(Date accTimeEnd) {
        this.accTimeEnd = accTimeEnd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getMobileUsername() {
        return mobileUsername;
    }

    public void setMobileUsername(String mobileUsername) {
        this.mobileUsername = mobileUsername;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getShareMobile() {
        return shareMobile;
    }

    public void setShareMobile(String shareMobile) {
        this.shareMobile = shareMobile;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getShareMerName() {
        return shareMerName;
    }

    public void setShareMerName(String shareMerName) {
        this.shareMerName = shareMerName;
    }

    public String getOemNo() {
        return oemNo;
    }

    public void setOemNo(String oemNo) {
        this.oemNo = oemNo;
    }

    public String getOemName() {
        return oemName;
    }

    public void setOemName(String oemName) {
        this.oemName = oemName;
    }
}
