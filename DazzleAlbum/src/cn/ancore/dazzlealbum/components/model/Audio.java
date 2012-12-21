package cn.ancore.dazzlealbum.components.model;

import cn.ancore.dazzlealbum.commons.Config;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 音频数据
 * @author magicruan
 * @version 1.0.0 2012-12-21
 */
@DatabaseTable(tableName=Config.DATABASE_TABLE_AUDIO)
public class Audio extends BaseModel{

	private static final long serialVersionUID = -6924456332657872367L;

	public static final String FIELD_NAME_AUDIO_DURATION = "duration";
	
	public static final String FIELD_NAME_AUDIO_DATA = "data";
	
	@DatabaseField(columnName=FIELD_NAME_AUDIO_DURATION, dataType=DataType.LONG)
	public long mDuration;
	
	@DatabaseField(columnName=FIELD_NAME_AUDIO_DATA, dataType=DataType.BYTE_ARRAY)
	public byte[] mData;
	
}
