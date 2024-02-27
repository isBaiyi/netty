package com.baiyi.codec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description:
 * @author: liaozicai
 * @date: 2024/2/22 14:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
	private String name;
	private int age;
}
