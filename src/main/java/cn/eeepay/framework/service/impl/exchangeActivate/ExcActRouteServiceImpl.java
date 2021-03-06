package cn.eeepay.framework.service.impl.exchangeActivate;

import cn.eeepay.framework.daoExchange.exchangeActivate.ExcActRouteDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.exchangeActivate.ExcActRoute;
import cn.eeepay.framework.model.exchangeActivate.ExcActRouteGood;
import cn.eeepay.framework.service.exchangeActivate.ExcActRouteGoodService;
import cn.eeepay.framework.service.exchangeActivate.ExcActRouteService;
import cn.eeepay.framework.util.CellUtil;
import cn.eeepay.framework.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2018/5/8/008.
 * @author  liuks
 * banner serivce实现类
 */
@Service("excActRouteService")
public class ExcActRouteServiceImpl implements ExcActRouteService {

    @Resource
    private ExcActRouteDao excActRouteDao;

    @Resource
    private ExcActRouteGoodService excActRouteGoodService;

    @Override
    public List<ExcActRoute> selectAllList(ExcActRoute route, Page<ExcActRoute> page) {
        return excActRouteDao.selectAllList(route,page);
    }

    @Override
    public int addRoute(ExcActRoute route) {
        return excActRouteDao.addRoute(route);
    }

    @Override
    public ExcActRoute getRoute(int id) {
        return excActRouteDao.getRoute(id);
    }

    @Override
    public int updateRoute(ExcActRoute route) {
        return excActRouteDao.updateRoute(route);
    }

    @Override
    public int closeRoute(int id, String state) {
        return excActRouteDao.closeRoute(id,state);
    }

    @Override
    public boolean checkRoute(ExcActRoute route) {
        List<ExcActRoute> list=null;
        if(route.getId()==null){
            list=excActRouteDao.getRouteList(route);
        }else{
            list=excActRouteDao.getRouteListById(route);
        }
        if(list!=null&&list.size()>0){
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> importDiscount(MultipartFile file) throws Exception {
        Map<String, Object> msg = new HashMap<>();
        Workbook wb = WorkbookFactory.create(file.getInputStream());
        //读取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        // 遍历所有单元格，读取单元格
        int row_num = sheet.getLastRowNum();
        List<ExcActRouteGood> addList=new ArrayList<ExcActRouteGood>();
        Map<String,List<String>> map=new HashMap<String,List<String>>();//校验

        Map<String,String> goodModeMap=new HashMap<String,String>();
        goodModeMap.put("文字报单","1");
        goodModeMap.put("图片报单","2");

        for (int i = 1; i <= row_num; i++) {
            Row row = sheet.getRow(i);
            String channelNo = CellUtil.getCellValue(row.getCell(0));//核销渠道编号
            String goodTypeNo1 = CellUtil.getCellValue(row.getCell(1));//上游渠道id
            String channelGoodNo1 = CellUtil.getCellValue(row.getCell(2));//渠道商品编号
            String channelGoodName = CellUtil.getCellValue(row.getCell(3));//渠道商品名称
            String goodContentId1 = CellUtil.getCellValue(row.getCell(4));//渠道商品内容编号
            String pId1 = CellUtil.getCellValue(row.getCell(5));//对应商品ID
            String channelPrice = CellUtil.getCellValue(row.getCell(6));//核销价格
            String goodMode = CellUtil.getCellValue(row.getCell(7));//核销方式

            if(channelNo==null||"".equals(channelNo)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,核销渠道编号为空!");
                return msg;
            }
            ExcActRoute route = excActRouteDao.getRouteByNo(channelNo);
            if(route==null){
                msg.put("status", false);
                msg.put("msg", "导入失败,第"+(i+1)+"行,核销渠道编号不存在!");
                return msg;
            }
            if(goodTypeNo1==null||"".equals(goodTypeNo1)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,上游渠道id为空!");
                return msg;
            }
            String goodTypeNo=goodTypeNo1.split("\\.")[0];
            if(channelGoodNo1==null||"".equals(channelGoodNo1)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,渠道商品编号为空!");
                return msg;
            }
            String channelGoodNo=channelGoodNo1.split("\\.")[0];

            if(channelGoodName==null||"".equals(channelGoodName)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,渠道商品名称为空!");
                return msg;
            }
            if(goodContentId1==null||"".equals(goodContentId1)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,渠道商品内容编号为空!");
                return msg;
            }
            String goodContentId=goodContentId1.split("\\.")[0];
            if(pId1==null||"".equals(pId1)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,对应商品ID为空!");
                return msg;
            }
            String pId=pId1.split("\\.")[0];

            if(!StringUtil.isNumeric(pId)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,对应商品ID格式错误!");
                return msg;
            }

            ExcActRouteGood good =new ExcActRouteGood();
            good.setChannelNo(channelNo);
            good.setpId(Long.valueOf(pId));
            if(!excActRouteGoodService.checkRoute(good)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,该核销渠道("+channelNo+")下该对应商品ID("+pId+")已存在!");
                return msg;
            }
            if(map.get(channelNo)!=null){
                List<String> checkList=map.get(channelNo);
                if(checkList.contains(pId)){
                    msg.put("status", false);
                    msg.put("msg","导入失败,第"+(i+1)+"行,该核销渠道("+channelNo+")下对应商品ID("+pId+")重复!");
                    return msg;
                }
                checkList.add(pId);
            }else{
                List<String> checkList=new ArrayList<String>();
                checkList.add(pId);
                map.put(channelNo,checkList);
            }
            if(channelPrice==null||"".equals(channelPrice)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,核销价格为空!");
                return msg;
            }
            if(!StringUtil.isNumeric(channelPrice)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,核销价格格式错误!");
                return msg;
            }
            if(goodMode==null||"".equals(goodMode)){
                msg.put("status", false);
                msg.put("msg","导入失败,第"+(i+1)+"行,核销方式为空!");
                return msg;
            }

            good.setGoodTypeNo(goodTypeNo);
            good.setChannelGoodNo(channelGoodNo);
            good.setChannelGoodName(channelGoodName);
            good.setGoodContentId(goodContentId);
            good.setChannelPrice(new BigDecimal(channelPrice));
            good.setGoodMode(goodModeMap.get(goodMode));
            addList.add(good);
        }
        int num=0;
        for(ExcActRouteGood good:addList){
            num=num+excActRouteGoodService.addExcActRouteGood(good);
        }
        msg.put("status", true);
        msg.put("msg", "导入成功,总共"+addList.size()+"条数据,成功导入"+num+"条");
        return msg;
    }

    @Override
    public List<ExcActRoute> getRouteALLList() {
        return excActRouteDao.getRouteALLList();
    }
}
