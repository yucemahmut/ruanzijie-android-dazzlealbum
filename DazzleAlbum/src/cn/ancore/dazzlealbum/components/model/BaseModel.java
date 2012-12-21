package cn.ancore.dazzlealbum.components.model;

import java.io.Serializable;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * 基础数据库实体类
 * @author magicruan
 * @version 1.0.0 2012-12-21
 */
public abstract class BaseModel implements Serializable{

	@DatabaseField(columnName="id", generatedId=true, dataType=DataType.INTEGER)
	public int id;

	protected BaseModel(){
	}
	
}
