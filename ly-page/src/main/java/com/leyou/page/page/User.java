package com.leyou.page.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: tianchao
 * @Date: 2019/12/1 00:52
 * @Description:
 */
@Deprecated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private int age;
    private User friend;
}
