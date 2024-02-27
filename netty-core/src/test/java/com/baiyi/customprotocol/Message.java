package com.baiyi.customprotocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @description: 消息对象
 * @author: liaozicai
 * @date: 2024/2/23 14:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {
	private String username;
	private String password;
}
