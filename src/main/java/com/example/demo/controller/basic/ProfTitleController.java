package com.example.demo.controller.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.domain.ProfTitle;
import com.example.demo.service.ProfTitleService;
import com.example.demo.util.JSONUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@RestController
public class ProfTitleController {
    @RequestMapping(value = "/profTitle.ctl", method = RequestMethod.GET)
    private String list(@RequestParam(value = "id", required = false) String id_str) {
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                return responseProfTitles();
            } else {
                int id = Integer.parseInt(id_str);
                return responseProfTitle(id);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
            //响应message到前端
            return message.toString();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
            //响应message到前端
            return message.toString();
        }
    }

    @RequestMapping(value = "/profTitle.ctl", method = RequestMethod.POST)
    private JSONObject add(HttpServletRequest request) throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //根据request对象，获得代表参数的JSON字串
        String ProfTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle ProfTitleToAdd = JSON.parseObject(ProfTitle_json, ProfTitle.class);
        //前台没有为id赋值，此处模拟自动生成id。Dao能实现数据库操作时，应删除此语句。
        ProfTitleToAdd.setId(4 + (int) (Math.random() * 100));
        //响应
        try {
            //增加ProfTitle对象
            ProfTitleService.getInstance().add(ProfTitleToAdd);
            //加入数据信息
            message.put("statusCode", "200");
            message.put("message", "增加成功");

        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message;
    }

    @RequestMapping(value = "/profTitle.ctl", method = RequestMethod.PUT)
    private JSONObject update(HttpServletRequest request) throws IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为ProfTitle对象
        ProfTitle ProfTitleToAdd = JSON.parseObject(profTitle_json, ProfTitle.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加ProfTitle对象
            ProfTitleService.getInstance().update(ProfTitleToAdd);
            //加入数据信息
            message.put("statusCode", "200");
            message.put("message", "更新成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message;
    }

    @RequestMapping(value = "/profTitle.ctl", method = RequestMethod.DELETE)
    private JSONObject delete(HttpServletRequest request) throws IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //到数据库表中删除对应的学院
            ProfTitleService.getInstance().delete(id);
            //加入数据信息
            message.put("statusCode", "200");
            message.put("message", "删除成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        }catch(Exception e){
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        return message;
    }

    private String responseProfTitle(int id) throws SQLException {
        //根据id查找学院
        ProfTitle ProfTitle = ProfTitleService.getInstance().find(id);
        String ProfTitle_json = JSON.toJSONString(ProfTitle);
        //响应message到前端
        return ProfTitle_json;
    }
    //响应所有学院对象
    private String responseProfTitles() throws SQLException {
        //获得所有学院
        Collection<ProfTitle> ProfTitles = ProfTitleService.getInstance().findAll();
        String ProfTitles_json = JSON.toJSONString(ProfTitles, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        return ProfTitles_json;
    }
}