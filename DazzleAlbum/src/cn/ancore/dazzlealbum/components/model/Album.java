package cn.ancore.dazzlealbum.components.model;

import cn.ancore.dazzlealbum.commons.Config;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 相册数据
 * @author magicruan
 * @version 1.0.0 2012-12-25
 */
@DatabaseTable(tableName=Config.DATABASE_TABLE_ALBUM)
public class Album extends BaseModel{

	private static final long serialVersionUID = -7617006912042852392L;

	public static final String FIELD_NAME_ALBUM_KEY = "key";
	
	public static final String FIELD_NAME_ALBUM_NAME = "name";
	
	public static final String FIELD_NAME_ALBUM_THUMB = "thumb";
	
	public static final String FIELD_NAME_PHOTO_COUNT = "photo_count";
	
	@DatabaseField(columnName=FIELD_NAME_ALBUM_KEY, dataType=DataType.STRING, index=true)
	public String mAlbumKey;
	
	@DatabaseField(columnName=FIELD_NAME_ALBUM_NAME, dataType=DataType.STRING)
	public String mAlbumName;
	
	@DatabaseField(columnName=FIELD_NAME_ALBUM_THUMB, dataType=DataType.BYTE_ARRAY)
	public byte[] mThumbData;

	@DatabaseField(columnName=FIELD_NAME_PHOTO_COUNT, dataType=DataType.INTEGER)
	public int mPhotoCount;
	
	public Album(){
		super();
	}
	
}
