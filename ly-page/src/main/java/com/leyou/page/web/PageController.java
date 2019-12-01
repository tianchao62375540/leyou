package com.leyou.page.web;

import com.leyou.page.service.PageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @Auther: tianchao
 * @Date: 2019/12/1 08:23
 * @Description:
 */
@Controller
@Slf4j
public class PageController {
    @Autowired
    private PageService pageService;


    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long spuId, Model model){
        log.info("[item页面id"+spuId+"]");
        Map<String, ?> attributes = pageService.loadModel(spuId);

        model.addAllAttributes(attributes);
        return "item";
    }
}
