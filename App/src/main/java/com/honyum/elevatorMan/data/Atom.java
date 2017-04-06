package com.honyum.elevatorMan.data;

import com.honyum.elevatorMan.net.base.JacksonJsonUtil;

import java.io.Serializable;

public class Atom implements Serializable{

	/**
	 * 利用反射机制将json直接转换成bean
	 * @param classType
	 * @param json
	 * @return
	 */
	protected static Atom parseFromJson(Class<? extends Atom> classType, String json) {
		Atom atom = null;
		try {
			atom = classType.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			atom = (Atom) JacksonJsonUtil
					.jsonToBean(json, classType);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return atom;
	}
}
