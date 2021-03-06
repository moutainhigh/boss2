package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.daoExchange.WithdrawalsDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchange.TotalAmount;
import cn.eeepay.framework.model.exchange.Withdrawals;
import cn.eeepay.framework.service.WithdrawalsService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/18/018.
 * @author  liuks
 * 提现记录 service
 */
@Service("withdrawalsService")
public class WithdrawalsServiceImpl implements WithdrawalsService {

    private static final Logger log = LoggerFactory.getLogger(WithdrawalsServiceImpl.class);

    @Resource
    private WithdrawalsDao withdrawalsDao;
    @Override
    public List<Withdrawals> selectAllList(Withdrawals wi, Page<Withdrawals> page) {
        List<Withdrawals> list= withdrawalsDao.selectAllList(wi,page);
        dataProcessingList(page.getResult());
        return list;
    }

    @Override
    public List<Withdrawals> importDetailSelect(Withdrawals wi) {
        List<Withdrawals> list=  withdrawalsDao.importDetailSelect(wi);
        dataProcessingList(list);
        return list;
    }
    /**
     * 数据处理List
     */
    private void dataProcessingList(List<Withdrawals> list){
        if(list!=null&&list.size()>0){
            for(Withdrawals item:list){
                if(item!=null){
                    item.setMobileNo(StringUtil.sensitiveInformationHandle(item.getMobileNo(),0));
                }
            }
        }
    }
    @Override
    public void importDetail(List<Withdrawals> list, HttpServletResponse response) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
        String fileName = "用户提现记录"+sdf.format(new Date())+".xlsx" ;
        String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
        response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
        List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
        if(list.size()<1){
            Map<String, String> maps = new HashMap<String, String>();
            maps.put("orderNo",null);
            maps.put("merNo",null);
            maps.put("oemNo",null);
            maps.put("oemName",null);
            maps.put("serialNo",null);
            maps.put("nickname",null);
            maps.put("accName",null);
            maps.put("mobileNo",null);
            maps.put("accNo",null);
            maps.put("status",null);
            maps.put("amount",null);
            maps.put("amountWithdrawals",null);
            maps.put("fee",null);
            maps.put("createTime",null);
            data.add(maps);
        }else{
            Map<String, String> statusMap=new HashMap<String, String>();
            statusMap.put("0","初始化");
            statusMap.put("1","提现中");
            statusMap.put("2","提现成功");
            statusMap.put("3","提现失败");

            for (Withdrawals or : list) {
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderNo",or.getOrderNo()==null?null:or.getOrderNo());
                maps.put("merNo",or.getMerNo()==null?null:or.getMerNo());
                maps.put("oemNo",or.getOemNo()==null?null:or.getOemNo());
                maps.put("oemName",or.getOemName()==null?null:or.getOemName());
                maps.put("serialNo",or.getSerialNo()==null?null:or.getSerialNo());
                maps.put("nickname",or.getNickname()==null?null:or.getNickname());
                maps.put("accName",or.getAccName()==null?null:or.getAccName());
                maps.put("mobileNo",or.getMobileNo()==null?null:or.getMobileNo());
                maps.put("accNo",or.getAccNo()==null?null:or.getAccNo());
                if(or.getStatus()!=null){
                    maps.put("status",statusMap.get(or.getStatus()));
                }else{
                    maps.put("status","");
                }
                maps.put("amount",or.getAmount()==null?null:or.getAmount().toString());
                maps.put("amountWithdrawals",or.getAmountWithdrawals()==null?null:or.getAmountWithdrawals().toString());
                maps.put("fee",or.getFee()==null?null:or.getFee().toString());
                maps.put("createTime", or.getCreateTime()==null?"":sdf1.format(or.getCreateTime()));
                data.add(maps);
            }
        }
        ListDataExcelExport export = new ListDataExcelExport();
        String[] cols = new String[]{"orderNo","merNo","oemNo","oemName","serialNo","nickname",
                "accName","mobileNo","accNo","status","amount","amountWithdrawals","fee","createTime"
        };
        String[] colsName = new String[]{"订单ID","用户ID","组织ID","组织名称","出款通道的流水号","昵称",
                "姓名","手机号","提现账号","提现状态","提现金额","出款金额","出款手续费","创建时间"
        };
        OutputStream ouputStream =null;
        try {
            ouputStream=response.getOutputStream();
            export.export(cols, colsName, data, response.getOutputStream());
        }catch (Exception e){
            log.error("导出用户提现记录失败!",e);
        }finally {
            if(ouputStream!=null){
                ouputStream.close();
            }
        }
    }

    @Override
    public TotalAmount selectSum(Withdrawals wi, Page<Withdrawals> page) {
        return withdrawalsDao.selectSum(wi,page);
    }
}
