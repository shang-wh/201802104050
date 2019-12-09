package com.example.demo.controller.basic;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.demo.domain.Teacher;
import com.example.demo.service.TeacherService;
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
public class TeacherController {
    @RequestMapping(value = "/teacher.ctl", method = RequestMethod.GET)
    private String list(@RequestParam(value = "id", required = false) String id_str) {
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学院对象，否则响应id指定的学院对象
            if (id_str == null) {
                return responseTeachers();
            } else {
                int id = Integer.parseInt(id_str);
                return responseTeacher(id);
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

    @RequestMapping(value = "/teacher.ctl", method = RequestMethod.POST)
    private JSONObject add(HttpServletRequest request) throws IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //根据request对象，获得代表参数的JSON字串
        String Teacher_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher TeacherToAdd = JSON.parseObject(Teacher_json, Teacher.class);
        //前台没有为id赋值，此处模拟自动生成id。Dao能实现数据库操作时，应删除此语句。
        TeacherToAdd.setId(4 + (int) (Math.random() * 100));
        //响应
        try {
            //增加Teacher对象
            TeacherService.getInstance().add(TeacherToAdd);
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

    @RequestMapping(value = "/teacher.ctl", method = RequestMethod.PUT)
    private JSONObject update(HttpServletRequest request) throws IOException {
        String profTitle_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Teacher对象
        Teacher TeacherToAdd = JSON.parseObject(profTitle_json, Teacher.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //增加Teacher对象
            TeacherService.getInstance().update(TeacherToAdd);
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

    @RequestMapping(value = "/teacher.ctl", method = RequestMethod.DELETE)
    private JSONObject delete(HttpServletRequest request) throws IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //到数据库表中删除对应的学院
            TeacherService.getInstance().delete(id);
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

    private String responseTeacher(int id) throws SQLException {
        //根据id查找学院
        Teacher Teacher = TeacherService.getInstance().find(id);
        String Teacher_json = JSON.toJSONString(Teacher);
        //响应message到前端
        return Teacher_json;
    }
    //响应所有学院对象
    private String responseTeachers() throws SQLException {
        //获得所有学院
        Collection<Teacher> Teachers = TeacherService.getInstance().findAll();
        String Teachers_json = JSON.toJSONString(Teachers, SerializerFeature.DisableCircularReferenceDetect);
        //响应message到前端
        return Teachers_json;
    }
}